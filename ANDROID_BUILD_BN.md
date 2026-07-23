# PDF Studio — Android অ্যাপ (Play Store) বানানোর নির্দেশনা

এই ফোল্ডারে PDF Studio-র **Android মোড়ক** তৈরি করা আছে — [Capacitor](https://capacitorjs.com/)
দিয়ে সেই একই অফলাইন web-app (`dist/index.html`)-কে একটা native Android WebView-এ
বসানো হয়েছে। এখান থেকে **signed AAB** বানিয়ে Google Play Store-এ আপলোড করা যাবে।

> সৎ কথা: এই মোড়কটা কোনো আসল Android ডিভাইস/এমুলেটরে এখনো build বা টেস্ট করা হয়নি
> (cloud env-এ Android SDK নেই)। প্রথম `Build` তোমার Android Studio-তেই আসল পরীক্ষা।
> কোনো সমস্যা হলে জানিও, ঠিক করে দেব।

---

## এক নজরে গঠন
- `dist/index.html` — আসল অ্যাপ (এক ফাইল, সব লজিক এখানেই; ব্রাউজার/ডেস্কটপ/Android — তিন জায়গায় একই কোড)
- `capacitor.config.json` — appId `com.everflow.pdfstudio`, webDir `dist`
- `android/` — Android Studio-তে খোলার মতো Gradle প্রজেক্ট (এখান থেকেই AAB বেরোয়)
- `package.json` — Capacitor CLI/নির্ভরতা

---

## লাগবে (একবারই)
1. **Android Studio** (সর্বশেষ) — এতে Android SDK, JDK সব থাকে। https://developer.android.com/studio
2. **Node.js** (আগে থেকেই আছে)
3. **Google Play Console** অ্যাকাউন্ট (এককালীন $25 ফি)

---

## ধাপ ১ — প্রজেক্ট প্রস্তুত করা (টার্মিনালে, এই ফোল্ডারে)
```bash
npm install          # Capacitor নির্ভরতা (একবার)
npx cap sync         # dist/ → android-এর ভেতরে কপি করে (web app বদলালেই আবার চালাও)
```

## ধাপ ২ — Android Studio-তে খোলা
```bash
npx cap open android
```
অথবা Android Studio-তে সরাসরি `android/` ফোল্ডারটা "Open"। প্রথমবার Gradle sync হতে
কয়েক মিনিট লাগবে (নির্ভরতা নামাবে)। "Install missing SDK" বললে টিক দিয়ে এগোও।

চালিয়ে দেখতে: উপরে একটা ইমুলেটর/ফোন বেছে **Run ▶**। Open বোতাম দিয়ে একটা PDF/EPUB
খুলে পড়ে দেখো।

## ধাপ ৩ — version ঠিক করা
প্রতিবার Play-তে নতুন আপলোডের আগে `android/app/build.gradle`-এ:
```gradle
versionCode 1        // প্রতিবার +1 (2, 3, ...) — Play এটা দিয়ে নতুন build চেনে
versionName "3.10.3" // মানুষের-পড়ার version (web app-এর সাথে মিলিয়ে রাখো)
```

## ধাপ ৪ — Signing key (একবার, যত্নে রাখো)
Play-তে দিতে হলে AAB **sign** করা লাগবে। একটা keystore বানাও (একবারই):
```bash
keytool -genkey -v -keystore pdfstudio-release.keystore \
  -alias pdfstudio -keyalg RSA -keysize 2048 -validity 10000
```
- একটা password দেবে — **সংরক্ষণ করো** (Tauri signing key-এর মতোই; হারালে একই appId-তে
  আপডেট দেওয়া কঠিন হয়ে যায়)।
- `pdfstudio-release.keystore` ফাইলটা নিরাপদে রাখো, git-এ commit কোরো না।

> সুপারিশ: Play Console-এ **Play App Signing** চালু রাখো (ডিফল্ট) — তখন Google তোমার
> চূড়ান্ত signing key নিজে সামলায়, তুমি শুধু upload key দিয়ে AAB sign করো। key হারানোর
> ঝুঁকি অনেক কমে।

## ধাপ ৫ — Signed AAB বানানো
**Android Studio-তে (সহজ):** Build → **Generate Signed Bundle / APK** → **Android App Bundle**
→ keystore (ধাপ ৪) বেছে/বানিয়ে → **release** → Finish।
ফাইল পাবে: `android/app/release/app-release.aab`

**অথবা কমান্ডে:** `android/app/build.gradle`-এ signingConfig বসিয়ে
```bash
cd android && ./gradlew bundleRelease
```

## ধাপ ৬ — Play Console-এ আপলোড
1. **Create app** → নাম "PDF Studio", ভাষা, App/Game = App, Free।
2. **Store listing** — সংক্ষিপ্ত ও পূর্ণ বিবরণ (বাংলা+English), আইকন (512×512 — `resources/icon.png` কাজে লাগবে),
   feature graphic (1024×500), অন্তত ২টা ফোন screenshot (ট্যাবলেটেরও দিলে ভালো)।
3. **Content rating** — প্রশ্নমালা পূরণ (এটা একটা productivity/reader অ্যাপ)।
4. **Data safety** — এই অ্যাপ **কোনো ব্যক্তিগত ডেটা সংগ্রহ/শেয়ার করে না** (সব লোকালি)।
   ব্যতিক্রম: OCR ব্যবহার করলে Tesseract অনলাইনে লোড হয় — কিন্তু ব্যবহারকারীর ডেটা পাঠায় না।
5. **Privacy policy** — একটা URL দিতে হবে (Play-র নিয়ম)। একটা সাধারণ পলিসি পেজ লাগবে
   (বললে আমি একটা লিখে দেব)।
6. **Target audience**, **App content** ঘোষণা পূরণ।
7. **Production** (বা আগে **Internal testing**) track → নতুন release → `app-release.aab` আপলোড →
   review-তে দাও। প্রথমবার রিভিউ কয়েকদিন লাগতে পারে।

---

## এই Android v1-এ যা কাজ করে ও যা এখনো নয় (সৎ তালিকা)
**কাজ করে:**
- PDF ও EPUB **খোলা ও পড়া** (Open বোতাম → Android ফাইল-পিকার), reflow, থিম, নিমগ্ন মোড, টাচ পড়া
- **লাইব্রেরি/বুকশেল্ফ**, resume, reading goal/streak (IndexedDB — অ্যাপে সংরক্ষিত থাকে)
- **highlight/নোট, পেজ-টার্ন, অভিধান, বাংলা-search** — সব
- OCR (ব্যবহার করলে ইন্টারনেট লাগে — Tesseract অনলাইন থেকে লোড হয়)

**এখনো নয় (ভবিষ্যতের কাজ):**
- **Save/Export ফাইল ডিভাইসে** — এডিট করা PDF বা Word/JPG এক্সপোর্ট WebView-এর সাধারণ
  ডাউনলোড দিয়ে Android-এ নাও সেভ হতে পারে। এর জন্য Capacitor **Filesystem** প্লাগিন যোগ করে
  web-কোডে একটা ছোট শাখা বসাতে হবে (Android হলে Filesystem দিয়ে সেভ)। বললে করে দেব।
- **অন্য অ্যাপ থেকে .pdf/.epub শেয়ার/ওপেন** (intent-filter) — এখন in-app Open দিয়েই খুলতে হয়।
- বাংলা ফন্ট: Android-এ Windows-এর "Nirmala UI" নেই, কিন্তু Android-এর নিজস্ব Noto বাংলা দিয়ে
  ঠিকই render হবে (font-stack fallback করে)।

---

## web app বদলালে
`dist/index.html` আপডেট হলেই:
```bash
npx cap sync      # নতুন web-কোড android-এ কপি
```
তারপর Android Studio-তে আবার build/Run। (Play-তে দিতে হলে versionCode +1 করে নতুন AAB)।

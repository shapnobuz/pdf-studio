# Changelog

PDF Studio-র প্রতিটা ভার্সনে কী বদলালো তার তালিকা।
ফরম্যাট: [Keep a Changelog](https://keepachangelog.com/), ভার্সনিং: [SemVer](https://semver.org/lang/bn/)।

<!--
  নতুন ভার্সন ছাড়ার আগে:
  1. নিচে "## [Unreleased]"-এর তলার পরিবর্তনগুলো একটা নতুন
     "## [x.y.z] - YYYY-MM-DD" সেকশনে সরিয়ে দাও।
  2. "## [Unreleased]" সেকশনটা খালি রেখে দাও পরের বারের জন্য।
  বিভাগগুলো (যেটা লাগে সেটাই রাখো): Added / Changed / Fixed / Removed / Security
  release.yml বিল্ডের সময় এই ফাইল থেকে ঠিক version-এর সেকশনটা তুলে
  GitHub Release-এর নোটে বসিয়ে দেয়।
-->

## [Unreleased]

<!-- পরের ভার্সনের পরিবর্তন এখানে যোগ করতে থাকো -->

## [3.10.3] - 2026-07-23

বই পড়ার জন্য বড় আপডেট — PDF Studio এখন EPUB-ও পড়ে, নিজের লাইব্রেরি রাখে।
(v3.10.1-এর পর এই রিলিজে v3.10.2-এর **বাংলা-সহনশীল সার্চ** ও **নিজের বুকমার্ক**-ও রয়েছে।)

### Added
- **EPUB রিডার** — নিজস্ব হালকা ইঞ্জিন (বাইরের লাইব্রেরি ছাড়া): `.epub` খোলা, অধ্যায়-ভিত্তিক reflow পড়া, সূচিপত্র, font-size / প্রস্থ / লাইন-ফাঁক / সেপিয়া কন্ট্রোল, progress ও resume।
- **লাইব্রেরি / বুকশেল্ফ** — খোলা বই (PDF ও EPUB) cover সহ তাকে জমা হয় (IndexedDB); "পড়ছি / পড়েছি / পড়ব" তাক, progress bar, continue-reading, আর **দৈনিক reading goal + 🔥 streak**।
- **Surface / টাচ পড়া** — নিমগ্ন (immersive) full-screen মোড, কেন্দ্রে ট্যাপে chrome হাইড, tap-zone ও swipe-এ অধ্যায়/পাতা ওল্টানো, TOC drawer, Windows বাংলা ফন্ট।
- **বইয়ে highlight ও নোট** (EPUB) — চার রঙে দাগানো, নোট যুক্ত করা; reflow-এ টিকে থাকে, drawer-এ তালিকা, per-book সংরক্ষিত।
- **পেজ-টার্ন মোড** — স্ক্রল বা পাতা-ওল্টানো, দুটোর যেকোনোটা বেছে নেওয়া (tap / swipe / arrow)।
- **ট্যাপে অভিধান** — শব্দ select করে বাংলা অর্থ (অফলাইন starter glossary + Wiktionary fallback)।

### Changed
- হেডারের version badge এখন `v3.10.3`।

## [3.10.2] - 2026-07-23

### Added
- **বাংলা-সহনশীল সার্চ** — বাংলা ও ইংরেজি অঙ্ক এক করে খোঁজে: `১২৩` লিখলে `123`-ও মেলে, উল্টোটাও (দাগ, দলিল, মামলা নম্বর, তারিখের জন্য)। সাথে Unicode NFC normalize আর zero-width joiner উপেক্ষা — text ও OCR দুই ক্ষেত্রেই।
- **নিজের নাম-দেওয়া বুকমার্ক** — যেকোনো পাতা bookmark করা যায় (নাম সহ), যেখানে PDF-এর নিজস্ব আউটলাইন নেই (স্ক্যান করা দলিলে) সেখানেও। ডাবল-ক্লিকে নাম বদলানো, ক্লিকে জাম্প, মুছে ফেলা — ডকুমেন্ট-প্রতি সংরক্ষিত। Bookmarks ট্যাব এখন সবসময় খোলা থাকে।

### Changed
- হেডারের version badge এখন প্রকৃত রিলিজ ভার্সন দেখায় (`v3.10.2`)।

## [3.10.1] - 2026-07-22

প্রথম ডেস্কটপ (Windows) রিলিজ, auto-update সহ।

### Added
- **ডেস্কটপ অ্যাপ (Tauri v2)** — Windows NSIS ইনস্টলার, ডাবল-ক্লিকে `.pdf` খোলা, single-instance।
- **Save-in-place** — Adobe-র মতো মূল ফাইলেই সেভ হয় (নতুন কপি Downloads-এ নয়)।
- **Auto-update** — নতুন ভার্সন এলে অ্যাপ নিজেই জানায়, এক ক্লিকে ডাউনলোড + ইনস্টল + রিস্টার্ট (GitHub Releases + স্বাক্ষরিত `latest.json`)।
- **বাংলা + ইংরেজি OCR** (Tesseract), পেজ-ভিত্তিক ক্যাশ, in-app সার্চ ও Word এক্সপোর্টে যুক্ত।
- **PDF → Word (.docx)** এডিটযোগ্য ড্রাফট এক্সপোর্ট (জ্যামিতি-ভিত্তিক লাইন/প্যারাগ্রাফ পুনর্গঠন, বাংলা-সচেতন শব্দ-ফাঁক)।
- অ্যানোটেশন — স্টিকি নোট ও হাইলাইট আসল PDF annotation object হিসেবে সেভ ও পুনরায় পড়া।
- এডিটিং — টেক্সট এডিট, কভার/রিড্যাক্ট, পেজ সাজানো/মোছা/ঘোরানো, পেজ এক্সট্র্যাক্ট, ছবি→PDF, PDF→JPG, মার্জ/অ্যাপেন্ড, পাসওয়ার্ড-সুরক্ষিত PDF খোলা।
- পড়ার অভিজ্ঞতা — কি-বোর্ডে পেজ ওল্টানো, resume-reading, বুকমার্ক/আউটলাইন প্যানেল।

[Unreleased]: https://github.com/shapnobuz/pdf-studio/compare/v3.10.3...HEAD
[3.10.3]: https://github.com/shapnobuz/pdf-studio/compare/v3.10.1...v3.10.3
[3.10.2]: https://github.com/shapnobuz/pdf-studio/compare/v3.10.1...v3.10.2
[3.10.1]: https://github.com/shapnobuz/pdf-studio/releases/tag/v3.10.1

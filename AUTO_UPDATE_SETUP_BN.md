# PDF Studio — Auto-Update সেটআপ (একবার করলে, ভবিষ্যতের প্রতিটা ভার্সন নিজে থেকেই পৌঁছে যাবে)

## এটা আসলে কীভাবে কাজ করে (৩০ সেকেন্ডে)

Auto-update-এর জন্য একটা প্রকাশ্য "চ্যানেল" লাগে যেখানে নতুন ভার্সন থাকবে — Anthropic-এর কোনো সার্ভার এই কাজ করতে পারে না, তাই আমরা **GitHub Releases** ব্যবহার করছি (বিনামূল্যে, আর VS Code/Discord-ও মূলত এই একই ধাঁচে কাজ করে)। প্রতিবার তুমি একটা নতুন ভার্সন ট্যাগ করে push করবে → GitHub নিজে থেকেই Windows installer বানাবে, নিরাপদে sign করবে, আর release হিসেবে প্রকাশ করবে। যাদের কম্পিউটারে PDF Studio খোলা আছে, তারা পরের বার app চালু করলেই "নতুন ভার্সন আছে" toast দেখবে — ক্লিক করলেই ডাউনলোড+ইনস্টল+রিস্টার্ট, সব স্বয়ংক্রিয়।

**গুরুত্বপূর্ণ বোনাস:** এই সেটআপের পর তোমার আর Windows মেশিনে বসে `cargo tauri build` চালানোর দরকারই নেই — GitHub নিজেই বিনামূল্যে একটা Windows মেশিনে build করে দেবে।

---

## ধাপ ১ — GitHub repo বানাও (যদি না থাকে)

1. https://github.com/new — নাম দাও যেমন `pdf-studio`, Private/Public যেকোনোটা (Private-ও বিনামূল্যে কাজ করবে)
2. এই পুরো `pdf-studio-desktop` ফোল্ডারটা push করো:
```powershell
cd pdf-studio-desktop
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/<তোমার-ইউজারনেম>/pdf-studio.git
git push -u origin main
```

## ধাপ ২ — Signing key জেনারেট করো (নিজের মেশিনে, একবারই)

এটা **তোমার নিজের কম্পিউটারে** চালাও — private key কখনো আমাকে বা কাউকে দেখানোর দরকার নেই:
```powershell
npx @tauri-apps/cli@latest signer generate -w $HOME/.tauri/pdfstudio.key
```
- একটা **password** চাইবে — মনে রাখো/সংরক্ষণ করো (password manager-এ)।
- শেষে টার্মিনালে একটা **public key** (লম্বা base64 string) প্রিন্ট হবে — সেটা কপি করো।
- `$HOME/.tauri/pdfstudio.key` ফাইলটাই তোমার **private key** — এটা কখনো GitHub-এ commit করবে না, share করবে না।

## ধাপ ৩ — Config-এ বসাও

`src-tauri/tauri.conf.json`-এ দুটো জায়গা বদলাও:
```json
"pubkey": "REPLACE_WITH_YOUR_GENERATED_PUBLIC_KEY",
"endpoints": ["https://github.com/REPLACE_GITHUB_USER/REPLACE_REPO_NAME/releases/latest/download/latest.json"]
```
→ `pubkey`-তে ধাপ ২-এর public key বসাও, `endpoints`-এ তোমার আসল GitHub username আর repo-এর নাম বসাও। তারপর commit+push করো।

## ধাপ ৪ — GitHub-এ Secrets যোগ করো

তোমার repo-তে যাও → **Settings → Secrets and variables → Actions → New repository secret** — দুটো secret বানাও:
| নাম | মান |
|---|---|
| `TAURI_SIGNING_PRIVATE_KEY` | `$HOME/.tauri/pdfstudio.key` ফাইলের **পুরো ভেতরের লেখা** (cat করে কপি করো) |
| `TAURI_SIGNING_PRIVATE_KEY_PASSWORD` | ধাপ ২-এ দেওয়া password |

`GITHUB_TOKEN` টা GitHub নিজে থেকেই দেয়, আলাদা করে কিছু করতে হবে না।

## ধাপ ৫ — প্রথম release বানাও

```powershell
git tag v3.10.1
git push --tags
```
GitHub repo-র **Actions** ট্যাবে গিয়ে দেখো build চলছে (~১০-১৫ মিনিট প্রথমবার)। শেষ হলে **Releases**-এ `v3.10.1` দেখা যাবে, সাথে installer (`.exe`) আর `latest.json`। এই installer-টাই এখন সবাইকে দাও — এর পর থেকে সব auto-update এখান থেকেই চলবে।

---

# ভবিষ্যতে নতুন ভার্সন ছাড়ার নিয়ম (প্রতিবার এইটুকুই)

আমি (Claude) যখন নতুন `pdf-studio.html` দেব:
1. সেটা `pdf-studio-desktop/dist/index.html`-এ বসাও (পুরনোটা বদলে দাও)
2. `src-tauri/tauri.conf.json` আর `src-tauri/Cargo.toml`-এ version number বাড়াও (যেমন `3.10.1` → `3.10.2`) — **দুই জায়গাতেই বাড়াতে হবে**
3. `CHANGELOG.md`-এ নতুন একটা সেকশন যোগ করো — `## [Unreleased]`-এর তলার লেখাগুলো `## [3.10.2] - YYYY-MM-DD` সেকশনে সরিয়ে দাও (কী কী বদলালো লেখো)। build-এর সময় এই সেকশনটাই release-এর নোটে বসে যাবে।
4.
```powershell
git add .
git commit -m "v3.10.2"
git tag v3.10.2
git push
git push --tags
```
ব্যস — GitHub বাকি সব (build, sign, publish, release-নোট) নিজে থেকেই করবে। কারো কম্পিউটারে হাত দিতে হবে না — পরের বার তারা app খুললেই update-toast দেখবে।

> tag push এই environment-এ আটকালে বিকল্প: GitHub → **Actions → release → Run workflow** → version (`v3.10.2`) দাও। একই কাজ হবে।

---

## নিরাপত্তা নোট
- Private key ফাইল বা password কখনো chat/email/git-এ শেয়ার কোরো না — শুধু GitHub Secrets-এই থাকবে।
- Private key হারিয়ে গেলে নতুন key জেনারেট করে সব ব্যবহারকারীকে ম্যানুয়ালি নতুন installer দিতে হবে (auto-update চেইন ভেঙে যাবে) — তাই backup রাখা ভালো (password manager বা encrypted USB)।

## যদি GitHub Actions সেটআপ করতে না চাও (সহজ বিকল্প)
CI ছাড়াও চলবে — শুধু "auto" অংশটা হাত দিয়ে করতে হবে: প্রতিবার নিজের Windows মেশিনে `cargo tauri build` চালিয়ে, `npx @tauri-apps/cli signer sign` দিয়ে sign করে, GitHub Releases-এ নিজে হাতে আপলোড করলেই একই auto-update কাজ করবে (শুধু ধাপ ৫-এর "প্রথমবার"-টা প্রতিবার করতে হবে)। CI-টা শুধু এই ম্যানুয়াল অংশটুকু বাদ দেয়।

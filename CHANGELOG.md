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

[Unreleased]: https://github.com/shapnobuz/pdf-studio/compare/v3.10.1...HEAD
[3.10.1]: https://github.com/shapnobuz/pdf-studio/releases/tag/v3.10.1

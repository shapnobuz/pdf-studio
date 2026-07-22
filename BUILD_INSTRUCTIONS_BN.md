# PDF Studio Desktop — Windows Installer বানানোর নির্দেশনা

## লাগবে (একবারই)
1. **Rust** — https://rustup.rs থেকে rustup-init.exe চালাও (ডিফল্ট অপশনেই Enter)
2. **Visual Studio Build Tools** — rustup নিজেই বলবে; "Desktop development with C++" টিক দাও
3. **WebView2** — Windows 11-এ আগে থেকেই আছে; Win10-এ Edge থাকলেই আছে

## Build (৫টা কমান্ড, প্রজেক্ট-ফোল্ডারে PowerShell খুলে)
```powershell
cargo install tauri-cli --version "^2"     # প্রথমবারই শুধু
cargo tauri icon src-tauri/logo.png        # আইকন-সেট তৈরি হবে
cargo tauri build                          # মূল build (প্রথমবার ১৫-৩০ মিনিট)
```
Installer পাবে এখানে:
`src-tauri\target\release\bundle\nsis\PDF Studio_2.5.0_x64-setup.exe`

## ইনস্টলের পর
- যেকোনো PDF-এ **right-click → Open with → PDF Studio**
- ডাবল-ক্লিকেই খুলতে চাইলে: Settings → Apps → Default apps → .pdf → PDF Studio
- **Save** এখন মূল ফাইলেই লেখে (Adobe-র মতো) — নতুন কপি Downloads-এ নয়

## Dev-মোড (build ছাড়া চালিয়ে দেখা)
```powershell
cargo tauri dev
```

## App আপডেট করতে
নতুন `pdf-studio.html`-কে `dist\index.html` নামে বসিয়ে আবার `cargo tauri build`

## নোট
- Unsigned installer-এ SmartScreen সতর্ক করবে: **More info → Run anyway**
- বাইরে বিতরণ করলে code-signing certificate নিও (~$100-400/বছর) — SmartScreen চুপ হয়ে যাবে
- একই প্রজেক্ট Mac/Linux-এও build হয় (`cargo tauri build` সেই OS-এ চালালেই)

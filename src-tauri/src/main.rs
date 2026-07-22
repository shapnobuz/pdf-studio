#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]

use base64::prelude::*;
use std::sync::Mutex;
use tauri::{Emitter, Manager, State};
use tauri_plugin_updater::UpdaterExt;

struct LaunchFile(Mutex<Option<String>>);

fn pdf_arg(args: &[String]) -> Option<String> {
    args.iter()
        .skip(1)
        .find(|a| a.to_lowercase().ends_with(".pdf"))
        .cloned()
}

#[tauri::command]
fn get_launch_file(state: State<'_, LaunchFile>) -> Option<String> {
    state.0.lock().unwrap().take()
}

#[tauri::command]
fn read_file(path: String) -> Result<String, String> {
    std::fs::read(&path)
        .map(|b| BASE64_STANDARD.encode(b))
        .map_err(|e| e.to_string())
}

#[tauri::command]
fn save_file(path: String, data: String) -> Result<(), String> {
    let bytes = BASE64_STANDARD
        .decode(data.as_bytes())
        .map_err(|e| e.to_string())?;
    std::fs::write(&path, bytes).map_err(|e| e.to_string())
}

// Returns Some(version) if a newer build is published at the configured
// update endpoint (see tauri.conf.json -> plugins.updater), else None.
#[tauri::command]
async fn check_for_update(app: tauri::AppHandle) -> Result<Option<String>, String> {
    let updater = app.updater().map_err(|e| e.to_string())?;
    match updater.check().await {
        Ok(Some(update)) => Ok(Some(update.version)),
        Ok(None) => Ok(None),
        Err(e) => Err(e.to_string()),
    }
}

// Downloads and installs the pending update, then restarts the app.
// On Windows the installer typically exits the process itself partway
// through this call, so the final restart() may never be reached there
// — that is expected, not an error.
#[tauri::command]
async fn install_update(app: tauri::AppHandle) -> Result<(), String> {
    let updater = app.updater().map_err(|e| e.to_string())?;
    if let Some(update) = updater.check().await.map_err(|e| e.to_string())? {
        update
            .download_and_install(|_chunk_len, _total| {}, || {})
            .await
            .map_err(|e| e.to_string())?;
    }
    app.restart();
}

fn main() {
    let args: Vec<String> = std::env::args().collect();
    let initial = pdf_arg(&args);
    tauri::Builder::default()
        .manage(LaunchFile(Mutex::new(initial)))
        .plugin(tauri_plugin_single_instance::init(|app, argv, _cwd| {
            if let Some(p) = pdf_arg(&argv) {
                let _ = app.emit("open-file", p);
            }
            if let Some(w) = app.get_webview_window("main") {
                let _ = w.set_focus();
            }
        }))
        .plugin(tauri_plugin_process::init())
        .setup(|app| {
            #[cfg(desktop)]
            app.handle()
                .plugin(tauri_plugin_updater::Builder::new().build())?;
            Ok(())
        })
        .invoke_handler(tauri::generate_handler![
            get_launch_file,
            read_file,
            save_file,
            check_for_update,
            install_update
        ])
        .run(tauri::generate_context!())
        .expect("error while running PDF Studio");
}

use tauri::{
  plugin::{Builder, TauriPlugin},
  Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::OpenPermissionSettings;
#[cfg(mobile)]
use mobile::OpenPermissionSettings;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the open-permission-settings APIs.
pub trait OpenPermissionSettingsExt<R: Runtime> {
  fn open_permission_settings(&self) -> &OpenPermissionSettings<R>;
}

impl<R: Runtime, T: Manager<R>> crate::OpenPermissionSettingsExt<R> for T {
  fn open_permission_settings(&self) -> &OpenPermissionSettings<R> {
    self.state::<OpenPermissionSettings<R>>().inner()
  }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
  Builder::new("open-permission-settings")
    .invoke_handler(tauri::generate_handler![
        commands::open_settings
    ])
    .setup(|app, api| {
      #[cfg(mobile)]
      let open_permission_settings = mobile::init(app, api)?;
      #[cfg(desktop)]
      let open_permission_settings = desktop::init(app, api)?;
      app.manage(open_permission_settings);
      Ok(())
    })
    .build()
}

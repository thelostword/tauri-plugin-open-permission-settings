use serde::de::DeserializeOwned;
use tauri::{
  plugin::{PluginApi, PluginHandle},
  AppHandle, Runtime,
};

use crate::models::*;

#[cfg(target_os = "ios")]
tauri::ios_plugin_binding!(init_plugin_open_permission_settings);

// initializes the Kotlin or Swift plugin classes
pub fn init<R: Runtime, C: DeserializeOwned>(
  _app: &AppHandle<R>,
  api: PluginApi<R, C>,
) -> crate::Result<OpenPermissionSettings<R>> {
  #[cfg(target_os = "android")]
  let handle = api.register_android_plugin("com.plugin.open_permission_settings", "OpenPermissionSettingsPlugin")?;
  #[cfg(target_os = "ios")]
  let handle = api.register_ios_plugin(init_plugin_open_permission_settings)?;
  Ok(OpenPermissionSettings(handle))
}

/// Access to the open-permission-settings APIs.
pub struct OpenPermissionSettings<R: Runtime>(PluginHandle<R>);

impl<R: Runtime> OpenPermissionSettings<R> {
  pub fn open_settings(&self, payload: OpenSettingsRequest) -> crate::Result<OpenSettingsResponse> {
    self
      .0
      .run_mobile_plugin("openSettings", payload)
      .map_err(Into::into)
  }
}

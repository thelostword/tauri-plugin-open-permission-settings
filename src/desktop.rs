use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
  app: &AppHandle<R>,
  _api: PluginApi<R, C>,
) -> crate::Result<OpenPermissionSettings<R>> {
  Ok(OpenPermissionSettings(app.clone()))
}

/// Access to the open-permission-settings APIs.
pub struct OpenPermissionSettings<R: Runtime>(AppHandle<R>);

impl<R: Runtime> OpenPermissionSettings<R> {
  pub fn open_settings(&self, _payload: OpenSettingsRequest) -> crate::Result<OpenSettingsResponse> {
    // Desktop platforms don't support opening permission settings directly
    // Return a response indicating this is not supported
    Ok(OpenSettingsResponse {
      success: false,
      message: Some("Opening permission settings is not supported on desktop platforms".to_string()),
    })
  }
}

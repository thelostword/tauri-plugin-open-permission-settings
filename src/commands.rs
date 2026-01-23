use tauri::{AppHandle, command, Runtime};

use crate::models::*;
use crate::Result;
use crate::OpenPermissionSettingsExt;

#[command]
pub(crate) async fn open_settings<R: Runtime>(
    app: AppHandle<R>,
    payload: OpenSettingsRequest,
) -> Result<OpenSettingsResponse> {
    app.open_permission_settings().open_settings(payload)
}

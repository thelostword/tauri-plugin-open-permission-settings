use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct OpenSettingsRequest {
  // 可以为空，未来可以扩展支持打开特定权限设置
  pub permission_type: Option<String>,
}

#[derive(Debug, Clone, Default, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct OpenSettingsResponse {
  pub success: bool,
  pub message: Option<String>,
}

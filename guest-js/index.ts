import { invoke } from '@tauri-apps/api/core'

/**
 * Android 权限类型
 * - app_details: 应用详情页面（默认）
 * - battery_optimization: 电池优化设置页面（跳转到设置）
 * - notification: 通知设置
 * - overlay: 悬浮窗权限
 * - usage_access: 使用统计权限
 * - write_settings: 写入系统设置权限
 */
export type PermissionType =
  | 'app_details'
  | 'battery_optimization'
  | 'notification'
  | 'overlay'
  | 'usage_access'
  | 'write_settings';

/**
 * Opens the app's permission settings page.
 * On Android, supports multiple settings pages based on permissionType.
 * On iOS, this opens the app's settings page in the Settings app.
 *
 * @param permissionType - Optional. Specific permission type to open
 * @returns Promise with success status and message
 *
 * @example
 * // 打开应用详情页面（默认）
 * await openSettings();
 *
 * @example
 * // 打开电池优化设置页面
 * await openSettings('battery_optimization');
 *
 * @example
 * // 打开通知设置
 * await openSettings('notification');
 */
export async function openSettings(permissionType?: PermissionType): Promise<{success: boolean; message?: string}> {
  return await invoke<{success: boolean; message?: string}>('plugin:open-permission-settings|open_settings', {
    payload: {
      permissionType,
    },
  });
}

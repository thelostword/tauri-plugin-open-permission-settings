/**
 * Android 权限类型
 * - app_details: 应用详情页面（默认）
 * - battery_optimization: 电池优化设置页面（跳转到设置）
 * - request_battery_optimization: 请求电池优化权限（弹出对话框，特殊操作）
 * - notification: 通知设置
 * - app_permissions: 应用权限页面
 * - overlay: 悬浮窗权限
 * - accessibility: 无障碍服务
 * - usage_access: 使用统计权限
 * - vpn: VPN 设置
 * - write_settings: 写入系统设置权限
 * - default_apps: 默认应用设置
 */
export type PermissionType = 'app_details' | 'battery_optimization' | 'request_battery_optimization' | 'notification' | 'app_permissions' | 'overlay' | 'accessibility' | 'usage_access' | 'vpn' | 'write_settings' | 'default_apps';
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
 * // 请求电池优化权限（弹出对话框）
 * await openSettings('request_battery_optimization');
 *
 * @example
 * // 打开通知设置
 * await openSettings('notification');
 */
export declare function openSettings(permissionType?: PermissionType): Promise<{
    success: boolean;
    message?: string;
}>;

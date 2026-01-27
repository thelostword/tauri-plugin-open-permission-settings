'use strict';

var core = require('@tauri-apps/api/core');

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
async function openSettings(permissionType) {
    return await core.invoke('plugin:open-permission-settings|open_settings', {
        payload: {
            permissionType,
        },
    });
}

exports.openSettings = openSettings;

'use strict';

var core = require('@tauri-apps/api/core');

/**
 * Opens the app's permission settings page.
 * On Android, this opens the app details page in system settings.
 * On iOS, this opens the app's settings page in the Settings app.
 *
 * @param permissionType - Optional. Specific permission type (for future extensions)
 * @returns Promise with success status and message
 */
async function openSettings(permissionType) {
    return await core.invoke('plugin:open-permission-settings|open_settings', {
        payload: {
            permissionType,
        },
    });
}

exports.openSettings = openSettings;

import { invoke } from '@tauri-apps/api/core'

/**
 * Opens the app's permission settings page.
 * On Android, this opens the app details page in system settings.
 * On iOS, this opens the app's settings page in the Settings app.
 *
 * @param permissionType - Optional. Specific permission type (for future extensions)
 * @returns Promise with success status and message
 */
export async function openSettings(permissionType?: string): Promise<{success: boolean; message?: string}> {
  return await invoke<{success: boolean; message?: string}>('plugin:open-permission-settings|open_settings', {
    payload: {
      permissionType,
    },
  });
}

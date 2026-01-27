# Tauri Plugin Open Permission Settings Page

A Tauri plugin for opening the app's permission settings page on Android and iOS.

| Platform | Supported |
| -------- | --------- |
| Linux    | ✗         |
| Windows  | ✗         |
| macOS    | ✗         |
| Android  | ✓         |
| iOS      | ✓         |


## Install

_This plugin requires a Rust version of at least **1.77.2**_

Install the Core plugin by adding the following to your `Cargo.toml` file:

`src-tauri/Cargo.toml`

```toml
[dependencies]
# tauri-plugin-open-permission-settings = "0.2.0"
# alternatively with Git:
tauri-plugin-open-permission-settings = { git = "https://github.com/thelostword/tauri-plugin-open-permission-settings", branch = "main" }
```

You can install the JavaScript Guest bindings using your preferred JavaScript package manager:

```sh
bun add https://github.com/thelostword/tauri-plugin-open-permission-settings
# or
pnpm add https://github.com/thelostword/tauri-plugin-open-permission-settings
# or
npm add https://github.com/thelostword/tauri-plugin-open-permission-settings
# or
yarn add https://github.com/thelostword/tauri-plugin-open-permission-settings
```

## Usage

First you need to register the core plugin with Tauri:

`src-tauri/src/lib.rs`

```rust
#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_open_permission_settings::init())
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
```

Second, add the required permissions in the project:

`src-tauri/capabilities/default.json`

```json
{
  "permissions": [
    "open-permission-settings:default"
  ]
}
```

Afterwards all the plugin's APIs are available through the JavaScript guest bindings:

```typescript
import { openSettings } from 'tauri-plugin-open-permission-settings';

// Open the app's default settings page
async function handleOpenSettings() {
  try {
    const result = await openSettings();
    if (result.success) {
      console.log('Settings opened successfully');
    } else {
      console.error('Failed to open settings:', result.message);
    }
  } catch (error) {
    console.error('Error opening settings:', error);
  }
}

// Open specific permission settings (Android specific, iOS opens unified settings)
async function handleOpenNotificationSettings() {
  const result = await openSettings('notification');
  console.log(result);
}
```

## Permission Types

| Permission Type | Description |
|----------------|-------------|
| `app_details` | App details/settings page (default) |
| `battery_optimization` | Battery optimization settings |
| `notification` | Notification settings |
| `app_permissions` | App permissions page |
| `overlay` | Display over other apps |
| `accessibility` | Accessibility services |
| `usage_access` | Usage stats permission |
| `vpn` | VPN settings |
| `write_settings` | Modify system settings |
| `default_apps` | Default apps settings |

> **Note:** iOS opens the unified app settings page for all types. Android opens specific permission pages. Minimum Android version: API 24 (Android 7.0).

## Credits and Thanks

- [plugins-workspace](https://github.com/tauri-apps/plugins-workspace) - For showing me how to build Tauri Plugins
- This plugin was developed with the assistance of AI technology (GitHub Copilot / Claude)


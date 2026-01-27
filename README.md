# Tauri Plugin Open Permission Settings Page

A Tauri plugin for opening the app's permission settings page on Android and iOS.

| Platform | Supported |
| -------- | --------- |
| Linux    | ✗         |
| Windows  | ✗         |
| macOS    | ✗         |
| Android  | ✓         |
| iOS      | ✓         |

## Features

- ✅ **Android Support** - Opens specific permission settings pages
- ✅ **iOS Support** - Opens app settings page in Settings app
- 🚀 **Simple API** - Single function call with optional permission type
- 📦 **TypeScript Support** - Full type definitions included
- 🎯 **11 Permission Types** - Fine-grained control on Android
- 🔋 **Special Actions** - Battery optimization request dialog on Android

## Install

_This plugin requires a Rust version of at least **1.77.2**_

Install the Core plugin by adding the following to your `Cargo.toml` file:

`src-tauri/Cargo.toml`

```toml
[dependencies]
tauri-plugin-open-permission-settings = "0.1.0"
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

// Request battery optimization permission (Android shows dialog, iOS ignores)
async function handleRequestBatteryOptimization() {
  const result = await openSettings('request_battery_optimization');
  console.log(result);
}
```

## API

### `openSettings(permissionType?: PermissionType)`

Opens the app's permission settings page.

**Parameters:**
- `permissionType` (optional): Specific permission type to open (see Permission Types table below)

**Returns:**
```typescript
Promise<{
  success: boolean;
  message?: string;
}>
```

## Permission Types

| Permission Type | Behavior | Description |
|----------------|----------|-------------|
| `app_details` | Jump to settings | App details/settings page (default) |
| `battery_optimization` | Jump to settings | Battery optimization settings |
| `request_battery_optimization` | Dialog (Android) / Ignored (iOS) | Request battery optimization permission |
| `notification` | Jump to settings | Notification settings |
| `app_permissions` | Jump to settings | App permissions page |
| `overlay` | Jump to settings | Display over other apps |
| `accessibility` | Jump to settings | Accessibility services |
| `usage_access` | Jump to settings | Usage stats permission |
| `vpn` | Jump to settings | VPN settings |
| `write_settings` | Jump to settings | Modify system settings |
| `default_apps` | Jump to settings | Default apps settings |

> **Note:** iOS opens the unified app settings page for all types. Android opens specific permission pages. Minimum Android version: API 24 (Android 7.0).

## Platform Behavior

- **Android (API 24+)**: Opens specific permission settings pages. Supports 11 permission types.
- **iOS**: Opens unified app settings page. All permission types work identically (except `request_battery_optimization` is ignored).
- **Desktop**: Not supported, returns error.

## Common Use Cases

### 1. Permission Denial Guidance

```typescript
import { openSettings } from 'tauri-plugin-open-permission-settings';

async function requestPermission() {
  const hasPermission = await checkCameraPermission();
  
  if (!hasPermission) {
    const shouldOpen = confirm(
      'Camera permission is required. Would you like to open settings?'
    );
    
    if (shouldOpen) {
      await openSettings();
    }
  }
}
```

## Credits and Thanks

- [plugins-workspace](https://github.com/tauri-apps/plugins-workspace) - For showing me how to build Tauri Plugins
- This plugin was developed with the assistance of AI technology (GitHub Copilot / Claude)


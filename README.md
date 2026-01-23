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

- ✅ **Android Support** - Opens app details page in system settings
- ✅ **iOS Support** - Opens app settings page in Settings app
- 🚀 **Simple API** - Single function call to open settings
- 📦 **TypeScript Support** - Full type definitions included

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

// Open the app's permission settings page
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
```

## API

### `openSettings(permissionType?: string)`

Opens the app's permission settings page.

**Parameters:**
- `permissionType` (optional): Specific permission type (reserved for future extensions)

**Returns:**
```typescript
Promise<{
  success: boolean;
  message?: string;
}>
```

## Platform Behavior

### Android
Opens the app's detail page in system settings (`Settings.ACTION_APPLICATION_DETAILS_SETTINGS`) where users can manage all app permissions.

### iOS
Opens the app's settings page in the Settings app (`UIApplication.openSettingsURLString`) where users can manage app permissions.

### Desktop
Returns `{ success: false, message: "Opening permission settings is not supported on desktop platforms" }`.

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


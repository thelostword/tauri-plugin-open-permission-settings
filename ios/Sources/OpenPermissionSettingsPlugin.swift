import SwiftRs
import Tauri
import UIKit
import WebKit

class OpenSettingsArgs: Decodable {
  let permissionType: String?
}

class OpenPermissionSettingsPlugin: Plugin {
  @objc public func openSettings(_ invoke: Invoke) throws {
    let args = try invoke.parseArgs(OpenSettingsArgs.self)

    // 在主线程打开设置页
    DispatchQueue.main.async {
      if let settingsUrl = URL(string: UIApplication.openSettingsURLString) {
        if UIApplication.shared.canOpenURL(settingsUrl) {
          UIApplication.shared.open(settingsUrl, options: [:]) { success in
            invoke.resolve([
              "success": success,
              "message": success ? "Successfully opened permission settings" : "Failed to open permission settings"
            ])
          }
        } else {
          invoke.resolve([
            "success": false,
            "message": "Cannot open settings URL"
          ])
        }
      } else {
        invoke.resolve([
          "success": false,
          "message": "Invalid settings URL"
        ])
      }
    }
  }
}

@_cdecl("init_plugin_open_permission_settings")
func initPlugin() -> Plugin {
  return OpenPermissionSettingsPlugin()
}

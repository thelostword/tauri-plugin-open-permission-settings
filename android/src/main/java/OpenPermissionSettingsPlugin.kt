package com.plugin.open_permission_settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke

@InvokeArg
class OpenSettingsArgs {
  var permissionType: String? = null
}

@TauriPlugin
class OpenPermissionSettingsPlugin(private val activity: Activity): Plugin(activity) {

    @Command
    fun openSettings(invoke: Invoke) {
        try {
            val args = invoke.parseArgs(OpenSettingsArgs::class.java)

            // 创建打开应用设置页面的 Intent
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            activity.startActivity(intent)

            val ret = JSObject()
            ret.put("success", true)
            ret.put("message", "Successfully opened permission settings")
            invoke.resolve(ret)

        } catch (e: Exception) {
            val ret = JSObject()
            ret.put("success", false)
            ret.put("message", "Failed to open permission settings: ${e.message}")
            invoke.resolve(ret)
        }
    }
}

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
            val permissionType = args.permissionType ?: "app_details"

            val intent = when (permissionType.lowercase()) {
                // 应用详情页面（默认）
                "app_details" -> createAppDetailsIntent()

                // 电池优化 - 打开设置页面（与其他权限行为一致）
                "battery_optimization" -> createBatteryOptimizationSettingsIntent()

                // 通知设置
                "notification" -> createNotificationIntent()

                // 悬浮窗权限
                "overlay" -> createOverlayIntent()

                // 使用统计权限
                "usage_access" -> createUsageAccessIntent()

                // 写入系统设置权限
                "write_settings" -> createWriteSettingsIntent()

                else -> createAppDetailsIntent()
            }

            activity.startActivity(intent)

            val ret = JSObject()
            ret.put("success", true)
            ret.put("message", "Successfully opened $permissionType settings")
            invoke.resolve(ret)

        } catch (e: Exception) {
            val ret = JSObject()
            ret.put("success", false)
            ret.put("message", "Failed to open permission settings: ${e.message}")
            invoke.resolve(ret)
        }
    }

    private fun createAppDetailsIntent(): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun createBatteryOptimizationSettingsIntent(): Intent {
        // 打开电池优化设置列表页，用户需要手动找到应用
        return Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun createNotificationIntent(): Intent {
        // Android 8.0+ 引入了通知渠道管理，使用专用 Action
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } else {
            // Android 8.0 以下降级到应用详情页
            createAppDetailsIntent()
        }
    }

    private fun createOverlayIntent(): Intent {
        // 直接跳转到悉浮窗权限设置页
        return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            data = Uri.fromParts("package", activity.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun createUsageAccessIntent(): Intent {
        // 尝试直接跳转到应用的使用统计设置页
        // 部分设备支持直接定位，部分只能打开列表
        return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            try {
                data = Uri.fromParts("package", activity.packageName, null)
            } catch (e: Exception) {
                // 忽略错误，会自动打开列表页
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun createWriteSettingsIntent(): Intent {
        // 直接跳转到写入系统设置权限页
        return Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}

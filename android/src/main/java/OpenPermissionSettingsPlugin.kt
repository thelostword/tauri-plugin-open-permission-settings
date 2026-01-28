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

                // 应用权限页面
                "app_permissions" -> createAppPermissionsIntent()

                // 悬浮窗权限
                "overlay" -> createOverlayIntent()

                // 无障碍服务
                "accessibility" -> createAccessibilityIntent()

                // 使用统计权限
                "usage_access" -> createUsageAccessIntent()

                // VPN 设置
                "vpn" -> createVpnIntent()

                // 写入系统设置权限
                "write_settings" -> createWriteSettingsIntent()

                // 默认应用设置
                "default_apps" -> createDefaultAppsIntent()

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

    private fun createAppPermissionsIntent(): Intent {
        // Android 11+ 支持直接跳转到应用的权限管理页面
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_APPS_PERMISSION_SETTINGS)
                intent.data = Uri.fromParts("package", activity.packageName, null)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent
            } catch (e: Exception) {
                // 某些设备可能不支持，降级到应用详情页
                createAppDetailsIntent()
            }
        } else {
            // 低版本降级到应用详情页
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

    private fun createAccessibilityIntent(): Intent {
        // 尝试直接跳转到应用的无障碍服务设置
        // 注意：大多数设备不支持直接跳转，会自动降级到列表页
        return try {
            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } catch (e: Exception) {
            // 降级到无障碍服务列表页
            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
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

    private fun createVpnIntent(): Intent {
        // 尝试直接跳转到应用的 VPN 设置
        // 注意：大多数设备不支持直接跳转，会自动打开 VPN 列表
        return Intent(Settings.ACTION_VPN_SETTINGS).apply {
            try {
                data = Uri.fromParts("package", activity.packageName, null)
            } catch (e: Exception) {
                // 忽略错误，会打开 VPN 列表页
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

    private fun createDefaultAppsIntent(): Intent {
        // 尝试直接跳转到应用的默认应用设置
        return Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
            try {
                data = Uri.fromParts("package", activity.packageName, null)
            } catch (e: Exception) {
                // 忽略错误，会打开默认应用列表页
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}

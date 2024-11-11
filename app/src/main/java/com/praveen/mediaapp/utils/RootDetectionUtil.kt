package com.praveen.mediaapp.utils

import android.content.Context
import android.content.pm.PackageManager
import java.io.File

object RootDetectionUtil {

    // Check if 'su' binary is present on the device
    private fun isSuBinaryPresent(): Boolean {
        val suPaths = listOf("/system/xbin/su", "/system/bin/su", "/sbin/su", "/vendor/bin/su")
        for (path in suPaths) {
            val file = File(path)
            if (file.exists()) {
                return true
            }
        }
        return false
    }

    // Check if root management apps are installed
    private fun isRootManagementAppInstalled(context: Context): Boolean {
        val packages =
            listOf("eu.chainfire.supersu", "com.topjohnwu.magisk", "com.kingroot.kinguser")
        for (packageName in packages) {
            try {
                context.packageManager.getPackageInfo(packageName, 0)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                // App is not installed
            }
        }
        return false
    }

    // Check if the system partition is writable
    private fun isSystemPartitionWritable(): Boolean {
        val file = File("/system")
        return file.exists() && file.canWrite()
    }

    // Check for root-related apps (e.g. Termux, Root Explorer)
    private fun isDangerousAppInstalled(context: Context): Boolean {
        val dangerousApps =
            listOf("com.android.termux", "com.stericson.rootexplorer", "com.zachospy.shell")
        for (app in dangerousApps) {
            try {
                context.packageManager.getPackageInfo(app, 0)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                // App not installed
            }
        }
        return false
    }

    // Check root related file paths (e.g., Superuser.apk, su binary)
    private fun isRootedByCheckingPaths(): Boolean {
        val paths = listOf(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su",
            "/data/data/com.noshufou.android.su"
        )
        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }

    // Check for root-related dangerous permissions
    private fun hasDangerousPermissions(context: Context): Boolean {
        val dangerousPermissions = listOf(
            "android.permission.ACCESS_SUPERUSER",
            "android.permission.ACCESS_ROOT",
            "android.permission.SU"
        )
        for (permission in dangerousPermissions) {
            if (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    // Combine all root checks into one function
    fun isDeviceRooted(context: Context): Boolean {
        return isSuBinaryPresent() || isRootManagementAppInstalled(context) || isSystemPartitionWritable()
                || isDangerousAppInstalled(context) || isRootedByCheckingPaths() || hasDangerousPermissions(
            context
        )
    }
}

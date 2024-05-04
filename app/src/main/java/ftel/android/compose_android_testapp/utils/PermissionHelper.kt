package ftel.android.compose_android_testapp.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker

object PermissionHelper {
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, permission: String, permissionCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            permissionCode
        )
    }

    fun showPermissionDialog(
        activity: Activity,
        permission: String,
        permissionCode: Int,
        title: String = "Permission Required",
        message: String = "This app requires permission to function properly.",
        callback: (Boolean) -> Unit
    ) {
        if (isPermissionGranted(activity, permission)) {
            // Nếu quyền đã được cấp, gọi hàm callback với tham số true và kết thúc hàm
            callback(true)
            return
        }

        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Allow") { dialogInterface: DialogInterface, i: Int ->
                callback(true)
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    permissionCode
                )
                dialogInterface.dismiss()
            }
            .setNegativeButton("Deny") { dialogInterface: DialogInterface, i: Int ->
                callback(false)
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
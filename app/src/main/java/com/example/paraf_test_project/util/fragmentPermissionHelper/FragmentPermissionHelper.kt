package com.example.paraf_test_project.util.fragmentPermissionHelper

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

class FragmentPermissionHelper {
    fun startPermissionRequest(fr: FragmentActivity, fp: FragmentPermissionInterface, permissionType:String) {
        val launcher: ActivityResultLauncher<String> =
            fr.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                fp.onGranted(it)
            }
        launcher.launch(permissionType)

    }
}
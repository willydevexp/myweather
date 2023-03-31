package com.example.myweather.model

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PermissionChecker(activity: AppCompatActivity, private val permission: String) {

    private var onRequest: (Boolean) -> Unit = {}
    private val launcher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        onRequest(isGranted)
    }

    suspend fun request(): Boolean =
        suspendCancellableCoroutine { continuation ->
            onRequest = {
                continuation.resume(it)
            }
            launcher.launch(permission)
        }
}
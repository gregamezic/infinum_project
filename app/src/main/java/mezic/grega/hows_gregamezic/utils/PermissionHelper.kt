package mezic.grega.hows_gregamezic.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val context: Context) {

    /**
     * CAMERA PERMISSION
     */
    // check
    fun checkCameraPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED)
    }


    /**
     * READ EXTERNAL STORAGE PERMISSION
     */
    // check
    fun checkReadExternalPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)
    }
}
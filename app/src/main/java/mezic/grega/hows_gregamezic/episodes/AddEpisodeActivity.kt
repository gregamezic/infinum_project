package mezic.grega.hows_gregamezic.episodes

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_add_episode.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.utils.PermissionHelper
import mezic.grega.hows_gregamezic.utils.Util
import mezic.grega.hows_gregamezic.utils.Util.Companion.PERMISSION_CAMERA_REQUEST_CODE
import mezic.grega.hows_gregamezic.utils.Util.Companion.PERMISSION_READ_EXTERNAL_REQUEST_CODE
import mezic.grega.hows_gregamezic.utils.Util.Companion.REQUEST_IMAGE_CAPTURE
import org.jetbrains.anko.appcompat.v7.Appcompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import mezic.grega.hows_gregamezic.utils.Util.Companion.REQUEST_IMAGE_GALLERY
import org.jetbrains.anko.*


class AddEpisodeActivity: MainBaseActivity() {

    private val TAG : String = AddEpisodeActivity::class.java.name
    private var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        // set toolbar title
        setupToolbar(getString(R.string.add_episode))

        // set view on click listeners
        setupViewsListeners()
    }

    /**
     * view on click listeners
     */
    private fun setupViewsListeners() {
        // set text listeners for enabling button button
        btn_save_episode.isEnabled = false
        input_episode_name.addTextChangedListener(AddingEpisodeTextWatcher())
        input_episode_desciption.addTextChangedListener(AddingEpisodeTextWatcher())

        // save button
        btn_save_episode.setOnClickListener {
            val name = input_episode_name.text.toString()
            val description = input_episode_desciption.text.toString()

            val intent = Intent()
            intent.putExtra(Util.EPISODE_NAME, name)
            intent.putExtra(Util.EPISODE_DESCRIPTION, description)
            intent.putExtra(Util.EPISODE_IMAGE_PATH_KEY, mCurrentPhotoPath)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // image view take picture
        linear_camera.setOnClickListener {
            openSelector()
        }
    }


    /**
     * PERMISSION RESULTS
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    takePicture()
                } else {
                    toast("Permission denied! Can not open camera!")
                }
            }

            PERMISSION_READ_EXTERNAL_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    readPicture()
                } else {
                    toast("Permission denied! Can not open the gallery!")
                }
            }
        }
        return
    }

    /**
     * ACTIVITY RESULTS
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // IMAGE FROM GALLERY
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                linear_camera.visibility = View.GONE
                linear_episode_image_camera.visibility = View.VISIBLE

                val bitmap: Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                img_episode_camera.setImageBitmap(bitmap)

                linear_episode_image_camera.setOnClickListener {
                    openSelector()
                }
            } else {
                loge(TAG, "Request image capture failed: $resultCode")
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                linear_camera.visibility = View.GONE
                linear_episode_image_camera.visibility = View.VISIBLE

                try {
                    val selectedImage: Uri? = data.data
                    mCurrentPhotoPath = selectedImage?.path

                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                    img_episode_camera.setImageBitmap(bitmap)

                    linear_episode_image_camera.setOnClickListener {
                        openSelector()
                    }

                } catch (e: IOException) {
                    loge(TAG, "request image from gallery failed: $resultCode.", e)
                }
            }
        }
    }



    private fun isValid(): Boolean {
        return input_episode_name.text.isNotEmpty() && input_episode_desciption.text.isNotEmpty()
    }

    override fun onBackPressed() {
        if (input_episode_name.text.isNotEmpty() || input_episode_desciption.text.isNotEmpty())
            exitDialog()
        else
            finish()
    }

    private fun exitDialog() {
        alert(Appcompat, getString(R.string.dialog_exit_message), getString(R.string.dialog_exit_title)) {
            yesButton {
                finish()
            }
            noButton { dialog ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun openSelector() {
        val options = listOf("Camera", "Gallery")
        val permissionHelper = PermissionHelper(this)
        selector("Select", options) { _, i ->
            when(i) {
                0 -> if (permissionHelper.checkCameraPermission()) takePicture() else permissionHelper.requestCameraPermission()
                1 -> if (permissionHelper.checkReadExternalPermission()) readPicture() else permissionHelper.requestReadExternalPermission()
            }
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "mezic.grega.hows_gregamezic.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun readPicture() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY)
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }




    /**
     * Class that is checking for input text changes
     */
    inner class AddingEpisodeTextWatcher: TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            btn_save_episode.isEnabled = isValid()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}
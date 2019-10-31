package mezic.grega.hows_gregamezic.episodes

import android.app.Activity
import android.content.Context
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.utils.PermissionHelper
import mezic.grega.hows_gregamezic.utils.Util
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddEpisodeFragment: Fragment() {

    companion object {
        fun newIntent(showId : Int?) : AddEpisodeFragment {
            val args = Bundle()
            showId?.let { args.putInt(Util.SHOW_ID_KEY, it) }
            return AddEpisodeFragment()
        }

        var addEpisodeCallback: AddEpisodeCallback? = null
    }

    // my val's
    private val TAG : String = AddEpisodeFragment::class.java.name
    private var mCurrentPhotoPath: String? = null
    private var showId: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddEpisodeCallback) {
            addEpisodeCallback = context
        } else {
            throw RuntimeException("Please implement ShowCallback")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nullableShowId = arguments?.getInt(Util.SHOW_ID_KEY, 0)
        nullableShowId?.let { showId = nullableShowId }

        // set toolbar title
        my_toolbar.title = getString(R.string.add_episode)
        my_toolbar.setNavigationOnClickListener {
            if (isValid()) {
                exitDialog(context as MainFragmentActivity)
            } else
                (context as MainFragmentActivity).supportFragmentManager.popBackStack()
        }

        // set view on click listeners
        setupViewsListeners()
    }

    private fun exitDialog(context: Context) {
        context.alert(Appcompat, context.getString(R.string.dialog_exit_message), context.getString(R.string.dialog_exit_title)) {
            yesButton {
                (context as MainFragmentActivity).supportFragmentManager.popBackStack()
            }
            noButton { dialog ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun setupViewsListeners() {
        // set text listeners for enabling button button
        btn_save_episode.isEnabled = false
        input_episode_name.addTextChangedListener(AddingEpisodeTextWatcher())
        input_episode_desciption.addTextChangedListener(AddingEpisodeTextWatcher())

        // save button
        btn_save_episode.setOnClickListener {
            val name = input_episode_name.text.toString()
            val description = input_episode_desciption.text.toString()

            addEpisodeCallback?.onEpisodeSave(name, description, mCurrentPhotoPath, showId)
        }

        // image view take picture
        linear_camera.setOnClickListener {
            openSelector()
        }
    }

    private fun openSelector() {
        val options = listOf("Camera", "Gallery")
        val permissionHelper = context?.let { PermissionHelper(it) }
        selector("Select", options) { _, i ->
            when(i) {
                0 -> if (permissionHelper != null) {
                    if (permissionHelper.checkCameraPermission()) takePicture() else permissionHelper.requestCameraPermission()
                }
                1 -> if (permissionHelper != null) {
                    if (permissionHelper.checkReadExternalPermission()) readPicture() else permissionHelper.requestReadExternalPermission()
                }
            }
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri? = context?.let {
            FileProvider.getUriForFile(
                it,
                "mezic.grega.hows_gregamezic.fileprovider",
                file
            )
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, Util.REQUEST_IMAGE_CAPTURE)
    }

    private fun readPicture() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, Util.REQUEST_IMAGE_GALLERY)
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
     * PERMISSION RESULTS
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Util.PERMISSION_CAMERA_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    takePicture()
                } else {
                    toast("Permission denied! Can not open camera!")
                }
            }

            Util.PERMISSION_READ_EXTERNAL_REQUEST_CODE -> {
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
        if (requestCode == Util.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                linear_camera.visibility = View.GONE
                linear_episode_image_camera.visibility = View.VISIBLE

                val bitmap: Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                img_episode_camera.setImageBitmap(bitmap)

                linear_episode_image_camera.setOnClickListener {
                    openSelector()
                }
            } else {
                Log.e(TAG, "Request image capture failed: $resultCode")
            }
        } else if (requestCode == Util.REQUEST_IMAGE_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                linear_camera.visibility = View.GONE
                linear_episode_image_camera.visibility = View.VISIBLE

                try {
                    val selectedImage: Uri? = data.data
                    mCurrentPhotoPath = selectedImage?.path

                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, selectedImage)
                    img_episode_camera.setImageBitmap(bitmap)

                    linear_episode_image_camera.setOnClickListener {
                        openSelector()
                    }

                } catch (e: IOException) {
                    Log.e(TAG, "request image from gallery failed: $resultCode.", e)
                }
            }
        }
    }


    private fun isValid(): Boolean {
        return input_episode_name.text.isNotEmpty() && input_episode_desciption.text.isNotEmpty()
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


interface AddEpisodeCallback {
    fun onEpisodeSave(name: String, description: String, imgPath: String?, showId: Int)
}
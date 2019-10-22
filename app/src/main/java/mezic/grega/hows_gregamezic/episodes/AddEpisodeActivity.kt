package mezic.grega.hows_gregamezic.episodes

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.Util

class AddEpisodeActivity: MainBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        // set toolbar title
        setupToolbar()
        setToolbarName("Add episode")
        my_toolbar.setNavigationOnClickListener {
            onBackPressed() }


        // set text listeners for enabeling button button
        btn_save_episode.isEnabled = false
        input_episode_name.addTextChangedListener(mTextWatcher())
        input_episode_desciption.addTextChangedListener(mTextWatcher())



        btn_save_episode.setOnClickListener {
            val name = input_episode_name.text.toString()
            val description = input_episode_desciption.text.toString()

            val intent = Intent()
            intent.putExtra(Util().EPISODE_NAME, name)
            intent.putExtra(Util().EPISODE_DESCRIPTION, description)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun isValid(): Boolean {
        return !input_episode_name.text.isEmpty() && !input_episode_desciption.text.isEmpty()
    }

    override fun onBackPressed() {
        if (!input_episode_name.text.isEmpty() || !input_episode_desciption.text.isEmpty())
            exitDialog()
        else
            finish()
    }

    private fun exitDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Cancel adding episode")
            .setMessage("Are you sure you want to cancel? All of your data will not be saved")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            finish()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, _ ->
                dialog.cancel()
        }

        builder.show()
    }

    inner class mTextWatcher() : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            btn_save_episode.isEnabled = isValid()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}
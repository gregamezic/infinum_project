package mezic.grega.hows_gregamezic.utils

import android.content.Context
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.MainFragmentActivity

class ToolbarHelper(private val context: Context) {

    fun setupToolbar(name: String?) {
        (context as MainFragmentActivity).my_toolbar.title = name
        context.my_toolbar.setNavigationOnClickListener {
            context.supportFragmentManager.popBackStack()
        }
    }
}
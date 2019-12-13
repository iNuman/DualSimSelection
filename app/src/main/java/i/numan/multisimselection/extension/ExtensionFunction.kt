package i.numan.multisimselection.extension

import android.content.Context
import android.widget.Toast

class ExtensionFunction {

    companion object {
        fun Context.toast(
            context: Context = applicationContext,
            message: String,
            length: Int = Toast.LENGTH_SHORT
        ) {
            Toast.makeText(context, message, length).show()
        }
    }
}

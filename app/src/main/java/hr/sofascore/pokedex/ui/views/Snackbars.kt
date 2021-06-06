package hr.sofascore.pokedex.ui.views

import android.content.Context
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.snackbar.Snackbar
import hr.sofascore.pokedex.R

class Snackbars {
    companion object {
        fun Snackbar.configNormal(context: Context) {
            val actionButton =
                this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
            actionButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close, 0, 0, 0)
            this.view.background =
                AppCompatResources.getDrawable(context, R.drawable.snackbar_neutral_background)
        }

        fun Snackbar.configError(context: Context) {
            val actionButton =
                this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
            actionButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close, 0, 0, 0)
            this.view.background =
                AppCompatResources.getDrawable(context, R.drawable.snackbar_error_background)
        }
    }
}
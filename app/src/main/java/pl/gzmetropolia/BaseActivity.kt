package pl.gzmetropolia

import android.annotation.SuppressLint
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import pl.gzmetropolia.knox.KnoxActivity
import java.util.concurrent.TimeUnit

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private val handler: Handler = Handler()

    override fun onBackPressed() {
        if (!KnoxActivity.isSealedModeState) {
            super.onBackPressed()
        } else {
            handler.removeCallbacksAndMessages(null)
            alertDialog()
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("RestrictedApi")
    private fun alertDialog() {
        val alert = AlertDialog.Builder(this).apply {
            title = "SEALED MODE"
            setMessage("Proszę podać hasło")
        }
        val passwordEditText = EditText(this).apply { inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD }
        alert.setView(passwordEditText, 20, 0, 20, 0)
        alert.setPositiveButton("OK") { dialog, _ ->
            val password = passwordEditText.text.toString()
            if (password == "switch123") {
                dialog.dismiss()
                finish()
            }
        }
        alert.setNegativeButton("Anuluj") { dialog, _ -> dialog.dismiss() }
        val dialog = alert.show()
        handler.postDelayed({
            try {
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, TimeUnit.MINUTES.toMillis(1))
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
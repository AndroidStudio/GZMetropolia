package pl.gzmetropolia

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import pl.gzmetropolia.knox.KnoxActivity

class MainActivity : AppCompatActivity() {

    private lateinit var typeface: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        knoxButton.setOnClickListener { showKnoxDialog(); }
        videoButton.setOnClickListener { launchActivity<VideoActivity> {} }
        galleryButton.setOnClickListener { launchActivity<GalleryActivity> {} }
        audioButton.setOnClickListener { launchActivity<AudioActivity> {} }

        typeface = Typeface.createFromAsset(assets, "ArcaMajora3-Bold.otf")
        titleTextView.typeface = typeface
        videoButton.typeface = typeface
        galleryButton.typeface = typeface
        audioButton.typeface = typeface
        logoTitle.typeface = typeface
    }

    override fun onBackPressed() {
        if (!KnoxActivity.isSealedModeState) {
            finish()
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("RestrictedApi")
    private fun showKnoxDialog() {
        val alert = AlertDialog.Builder(this)
        val passwordEditText = EditText(this)
        passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        alert.setTitle("SEALED MODE")
        alert.setMessage("Proszę podać hasło")
        alert.setView(passwordEditText, 20, 0, 20, 0)
        alert.setPositiveButton("OK") { dialog, _ ->
            val password = passwordEditText.text.toString()
            if (password == "switch123") {
                dialog.dismiss()
                launchActivity<KnoxActivity> { }
            }
        }
        alert.setNegativeButton("Anuluj") { dialog, _ -> dialog.dismiss() }
        alert.show()
    }
}

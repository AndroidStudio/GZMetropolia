package pl.gzmetropolia

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import pl.gzmetropolia.knox.KnoxActivity

const val SINGLE_AUDIO = "single_audio"

class MainActivity : AppCompatActivity() {

    private lateinit var typeface: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        knoxButton.setOnClickListener { showKnoxDialog(); }
        videoButton.setOnClickListener { launchActivity<VideoActivity> {} }
        galleryButton.setOnClickListener { launchActivity<GalleryActivity> {} }
        audioButton.setOnClickListener { launchActivity<AudioActivity> { putExtra(SINGLE_AUDIO, false) } }
        natureAudio.setOnClickListener { launchActivity<AudioActivity> { putExtra(SINGLE_AUDIO, true) } }

        typeface = Typeface.createFromAsset(assets, "ArcaMajora3-Bold.otf")
        titleTextView.typeface = typeface
        videoButton.typeface = typeface
        galleryButton.typeface = typeface
        audioButton.typeface = typeface
        logoTitle.typeface = typeface
        natureAudio.typeface = typeface

        if (!checkPermissions()) {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
        )
    }

    private fun checkPermissions(): Boolean {
        val readStorage = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return readStorage == PackageManager.PERMISSION_GRANTED
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

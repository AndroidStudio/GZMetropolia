package pl.gzmetropolia

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import kotlinx.android.synthetic.main.video_layout.*
import timber.log.Timber
import java.io.File

class VideoActivity : BaseActivity() {

    private val videoList: ArrayList<String> = ArrayList()

    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_layout)
        prepareVideoList()
    }

    private fun prepareVideoList() {
        try {
            val folder = Environment.getExternalStorageDirectory().toString() + "/mz_metropolia/video/"
            val directory = File(folder)
            val files = directory.listFiles()
            for (file in files) {
                val path = folder + "/" + file.name
                videoList.add(path)
                Timber.d("item path: %s", path)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "brak plikow", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadVideo()
    }

    override fun onPause() {
        super.onPause()
        videoView.stopPlayback()
    }

    private fun loadVideo() {
        try {
            if (videoList.size == 0){
                return
            }
            if (index == videoList.size) {
                index = 0
            }

            val path = videoList[index]
            videoView.setVideoURI(Uri.parse(path))
            videoView.setOnCompletionListener { loadVideo() }
            videoView.start()
            index++
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
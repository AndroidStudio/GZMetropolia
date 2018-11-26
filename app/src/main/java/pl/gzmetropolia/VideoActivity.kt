package pl.gzmetropolia

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.video_layout.*


class VideoActivity : BaseActivity() {

    private val videoList by lazy {
        listOf(Environment.getExternalStorageDirectory().absolutePath + "/mz_metropolia/bedzin.mp4")
    }

    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_layout)
        startVideo()
    }

    private fun startVideo() {
        if (index == videoList.size) {
            index = 0
        }

        val path = videoList[index]
        videoView.setVideoURI(Uri.parse(path))
        videoView.setOnCompletionListener { startVideo() }
        videoView.start()
        index++
    }
}
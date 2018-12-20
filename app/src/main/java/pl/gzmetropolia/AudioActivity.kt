package pl.gzmetropolia

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.audio_layout.*
import kotlinx.android.synthetic.main.audio_list.view.*
import java.io.IOException

class AudioActivity : BaseActivity(), SelectAudioCallback {

    private lateinit var audioList: List<AudioModel>

    private val mediaPlayer: MediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_layout)
        createAudioList()

        audioListView.adapter = AudioListAdapter(this, audioList, this)
        stop.setOnClickListener { stopAudio() }
        play.setOnClickListener { startAudio() }
    }

    private fun createAudioList() {
        val singleAudio = intent!!.extras!!.getBoolean(SINGLE_AUDIO, false)
        audioList = if (singleAudio) {
            listOf(
                AudioModel(
                    "Katowickie odgłosy natury",
                    Environment.getExternalStorageDirectory().absolutePath
                            + "/mz_metropolia/audio/katowice_odglosy_natury.wav",
                    "03:51",
                    ""
                )
            )
        } else {
            listOf(
                AudioModel(
                    "Concerto for Harpsichord and String Quartet",
                    Environment.getExternalStorageDirectory().absolutePath
                            + "/mz_metropolia/audio/concerto_for_harpsichord_and_string.mp3",
                    "04:36",
                    "Henryk Górecki"
                )
                , AudioModel(
                    "Jestem Bogiem",
                    Environment.getExternalStorageDirectory().absolutePath
                            + "/mz_metropolia/audio/jestem_bogiem.mp3",
                    "03:22",
                    "Paktofonika"
                )
                , AudioModel(
                    "Nie mamy skrzydeł",
                    Environment.getExternalStorageDirectory().absolutePath
                            + "/mz_metropolia/audio/nie_mamy_skrzydel.mp3",
                    "03:48",
                    "Miuosh"
                )
                , AudioModel(
                    "Orawa",
                    Environment.getExternalStorageDirectory().absolutePath
                            + "/mz_metropolia/audio/orawa.mp3",
                    "08:25",
                    "Wojciech Kilar"
                )
                , AudioModel(
                    "Tango Schaeffera",
                    Environment.getExternalStorageDirectory().absolutePath
                            + "/mz_metropolia/audio/tango_schaeffera.mp3",
                    "03:59",
                    "Marcin Wyrostek"
                )
                , AudioModel(
                    "Wehikuł czasu",
                    Environment.getExternalStorageDirectory().absolutePath
                            + "/mz_metropolia/audio/wehikul_czasu_to_bylby_cud.mp3",
                    "06:12",
                    "Dżem"
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onSelectAudio(audioModel: AudioModel) {
        audioTitleTextView.text = String.format("%s", audioModel.artist + "\n" + audioModel.title)
        playNewAudio(audioModel.path)
    }

    private fun playNewAudio(path: String) {
        releasePlayer()

        try {
            mediaPlayer.setDataSource(applicationContext, Uri.parse(path))
            mediaPlayer.isLooping = true
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener { startAudio() }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "plik nie zostal odnaleziony", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun releasePlayer() {
        mediaPlayer.reset()
    }

    private fun stopAudio() {
        stop.visibility = View.GONE
        play.visibility = View.VISIBLE
        mediaPlayer.pause()
    }

    private fun startAudio() {
        stop.visibility = View.VISIBLE
        play.visibility = View.GONE
        mediaPlayer.start()
    }
}

interface SelectAudioCallback {

    fun onSelectAudio(audioModel: AudioModel)

}

class AudioListAdapter(
    context: Context,
    private val list: List<AudioModel>,
    private val callbacks: SelectAudioCallback
) : BaseAdapter() {

    private var selected: Int = -1

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.audio_list, parent, false)
        view.artistTextView.text = list[position].artist
        view.titleTextView.text = list[position].title
        view.timeTextView.text = list[position].time

        if (TextUtils.isEmpty(list[position].artist)) {
            view.artistTextView.visibility = View.GONE
        } else {
            view.artistTextView.visibility = View.VISIBLE
        }

        if (selected == position) {
            view.image.setBackgroundColor(Color.parseColor("#FFD7006D"))
            view.image.setImageResource(R.drawable.ic_002_music_note)
            view.arrowIcon.setImageResource(R.drawable.arrow_selected)
        } else {
            view.image.setBackgroundColor(Color.parseColor("#FFF7F7F7"))
            view.image.setImageResource(R.drawable.ic_002_music_note_normal)
            view.arrowIcon.setImageResource(R.drawable.ic_dropdown_icon)
        }

        view.rootView.setOnClickListener {
            selected = position
            callbacks.onSelectAudio(list[position])
            notifyDataSetChanged()
        }
        return view
    }

    override fun getCount(): Int {
        return list.size
    }
}

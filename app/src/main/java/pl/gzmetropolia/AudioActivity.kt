package pl.gzmetropolia

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.audio_layout.*
import kotlinx.android.synthetic.main.audio_list.view.*


class AudioActivity : BaseActivity(), SelectAudioCallback {

    private val audioList by lazy {
        listOf(
            AudioModel(
                "Get Best Advertiser In Your Side Pocket",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            ),
            AudioModel(
                "Metropolia przemysłowych miast Górnego Śląska i Zagłębia Dąbrowskiego",
                "android.resource://" + packageName + "/" + R.raw.audio, "03:21"
            )
        )
    }

    private val mediaPlayer: MediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_layout)
        audioListView.adapter = AudioListAdapter(this, audioList, this)
        stop.setOnClickListener { stopAudio() }
        play.setOnClickListener { startAudio() }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onSelectAudio(audioModel: AudioModel) {
        audioTitleTextView.text = audioModel.title
        playNewAudio(audioModel.path)
    }

    private fun playNewAudio(path: String) {
        releasePlayer()

        mediaPlayer.setDataSource(applicationContext, Uri.parse(path))
        mediaPlayer.isLooping = true
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener { startAudio() }
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
        view.timeTextView.text = list[position].time
        view.titleTextView.text = list[position].title

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

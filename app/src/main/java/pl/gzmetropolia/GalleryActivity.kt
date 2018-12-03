package pl.gzmetropolia

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_item_fragment.*
import kotlinx.android.synthetic.main.gallery_layout.*
import timber.log.Timber
import java.io.File

class GalleryActivity : BaseActivity() {

    val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_layout)
        prepareGalleyList()

        viewPager.adapter = GalleryAdapter(supportFragmentManager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(position: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                imageTitle(position)
            }
        })

        next.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem == galleryList.size - 1) {
                viewPager.currentItem = 0
            } else {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }

        back.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem == 0) {
                viewPager.currentItem = galleryList.size - 1
            } else {
                viewPager.currentItem = viewPager.currentItem - 1
            }
        }

        imageTitle(0)
    }

    private fun imageTitle(position: Int) {
        try {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({ nameTextView.animate().alpha(0f) }, 6000)
            nameTextView.text = galleryFileNameList[position]
            nameTextView.animate().alpha(1f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    private fun prepareGalleyList() {
        try {
            val folder = Environment.getExternalStorageDirectory().toString() + "/mz_metropolia/galeria/"
            val directory = File(folder)
            val files = directory.listFiles()
            for (file in files) {
                val fileName = file.name
                    .replace(".jpg", "")
                    .replace(".JPG", "")
                    .replace("jpg", "")
                    .replace("_", "")

                val path = folder + "/" + file.name
                galleryFileNameList.add(fileName)
                galleryList.add(path)
                Timber.d("item path: %s", path)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Brak plikow", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}

private val galleryList: ArrayList<String> = ArrayList()
private val galleryFileNameList: ArrayList<String> = ArrayList()

const val GALLERY_ITEM = "gallery_item"

class GalleryAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return GalleryFragment().apply {
            arguments = Bundle().apply {
                putString(GALLERY_ITEM, galleryList[position])
            }
        }
    }

    override fun getCount(): Int {
        return galleryList.size
    }
}

class GalleryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gallery_item_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            arguments?.getString(GALLERY_ITEM)
                .let { url -> activity?.let { activity -> Glide.with(activity).load(File(url)).into(imageView) } }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

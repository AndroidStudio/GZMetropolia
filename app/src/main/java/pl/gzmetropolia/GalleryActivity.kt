package pl.gzmetropolia

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_item_fragment.*
import kotlinx.android.synthetic.main.gallery_layout.*

class GalleryActivity : BaseActivity() {

    private val typeface by lazy { Typeface.createFromAsset(assets, "ArcaMajora3-Bold.otf") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_layout)
        viewPager.adapter = GalleryAdapter(supportFragmentManager)

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
    }
}

private val galleryList = listOf(R.drawable.p_1, R.drawable.p_2, R.drawable.p_3)

const val GALLERY_ITEM = "gallery_item"

class GalleryAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return GalleryFragment().apply { arguments = Bundle().apply { putInt(GALLERY_ITEM, galleryList[position]) } }
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
        arguments?.getInt(GALLERY_ITEM)
            .let { url -> activity?.let { activity -> Glide.with(activity).load(url).into(imageView) } }
    }
}

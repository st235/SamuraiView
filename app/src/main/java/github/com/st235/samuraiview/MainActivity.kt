package github.com.st235.samuraiview

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import github.com.st235.lib_samurai.Harakiri
import github.com.st235.lib_samurai.SamuraiView
import github.com.st235.samuraiview.components.CircularImageView
import github.com.st235.samuraiview.utils.BitmapHelper
import github.com.st235.samuraiview.utils.dpToPx
import st235.com.github.flow_layout.FlowLayout

class MainActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var samuraiView: SamuraiView
    private lateinit var avatar: CircularImageView
    private lateinit var feedImage: AppCompatImageView

    private lateinit var nextButtonViewController: NextButtonViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        avatar = findViewById(R.id.avatar)
        userName = findViewById(R.id.user)
        feedImage = findViewById(R.id.insta_image)
        feedImage.setImageBitmap(
            BitmapHelper.decodeSampledBitmapFromResource(
                resources,
                R.drawable.photo, PROFILE_PICTURE_SIZE, PROFILE_PICTURE_SIZE
            )
        )

        val tagsChipLayout = findViewById<FlowLayout>(R.id.tag_layout)
        val tags = resources.getStringArray(R.array.cats_tags)

        for (tag in tags) {
            addChildTag(tagsChipLayout, tag)
        }

        samuraiView = findViewById(R.id.samurai_view)
        samuraiView.visibility = View.GONE

        nextButtonViewController = NextButtonViewController(findViewById(R.id.samurai_next_view)) {
            when(it) {
                NextButtonViewController.INTRODUCTION ->
                    Harakiri(into = samuraiView)
                            .overlayColorRes(R.color.colorWhite95)
                            .withTooltip(R.layout.tooltip_image, width = ViewGroup.LayoutParams.MATCH_PARENT)
                            .capture(feedImage)
                NextButtonViewController.AUTHOR ->
                    Harakiri(into = samuraiView)
                            .rect(16F)
                            .overlayColorRes(R.color.colorWhite95)
                            .frameColorRes(R.color.colorPrimaryDark)
                            .frameThickness(1F)
                            .margins(0, 0, 6, 0)
                            .withTooltip(R.layout.tooltip_author, width = ViewGroup.LayoutParams.MATCH_PARENT)
                            .capture(avatar, userName)
                NextButtonViewController.SHARE ->
                    Harakiri(into = samuraiView)
                            .circle()
                            .overlayColorRes(R.color.colorWhite95)
                            .frameColorRes(R.color.colorPrimaryDark)
                            .frameThickness(2F)
                            .margins(20, 20, 20, 20)
                            .withTooltip(R.layout.tooltip_share, width = ViewGroup.LayoutParams.MATCH_PARENT)
                            .capture(toolbar.findViewById<View>(R.id.action_share))
                NextButtonViewController.LAST ->
                    samuraiView.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                shareLibrary()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addChildTag(
        tagLayout: ViewGroup,
        tag: String
    ) {
        val tagView = TextView(ContextThemeWrapper(this, R.style.ChipViewTextAppearance))
        tagView.text = tag

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(dpToPx(2), dpToPx(2), dpToPx(2), dpToPx(2))

        tagLayout.addView(tagView, params)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        nextButtonViewController.back()
    }

    private fun shareLibrary() {
        val shareBody = getString(R.string.share_text)
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(
                Intent.createChooser(
                        sharingIntent,
                        getString(R.string.share_chooser)
                )
        )
    }

    companion object {
        @Px
        private val PROFILE_PICTURE_SIZE = 512
    }
}

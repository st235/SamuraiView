package github.com.st235.showcaseview

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import github.com.st235.lib_showcase.Harakiri
import github.com.st235.lib_showcase.SamuraiView
import github.com.st235.lib_showcase.ShowCaseTooltip

class MainActivity : AppCompatActivity() {

    private lateinit var samuraiView: SamuraiView
    private lateinit var avatar: CircularImageView
    private lateinit var userName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        avatar = findViewById(R.id.avatar)
        userName = findViewById(R.id.user)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val feedImage = findViewById<AppCompatImageView>(R.id.insta_image)
        feedImage.setImageBitmap(
            BitmapHelper.decodeSampledBitmapFromResource(
                resources,
                R.drawable.cat2, PROFILE_PICTURE_SIZE, PROFILE_PICTURE_SIZE
            )
        )

        samuraiView = findViewById(R.id.samurai_view)
        samuraiView.frameThickness = 2F
        samuraiView.frameColor = Color.RED
        samuraiView.overlayColor = 0xEFFFFFFF.toInt()
        samuraiView.tooltip = ShowCaseTooltip(R.layout.tooltip_layout)
//        samuraiView.setShowcaseMargins(0, 0, 6, 0)

        val tagsChipLayout = findViewById<ChipLayout>(R.id.tag_layout)
        val tags = resources.getStringArray(R.array.cats_tags)

        for (tag in tags) {
            addChildTag(tagsChipLayout, tag)
        }

        Harakiri(into = samuraiView).highlightDuration(2_000).capture(avatar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                shareArticle()
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

        params.setMargins(Dimens.dpToPx(2), Dimens.dpToPx(2), Dimens.dpToPx(2), Dimens.dpToPx(2))

        tagLayout.addView(tagView, params)
    }

    private fun shareArticle() {
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

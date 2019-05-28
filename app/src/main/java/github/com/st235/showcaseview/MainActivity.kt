package github.com.st235.showcaseview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import github.com.st235.lib_showcase.Harakiri
import github.com.st235.lib_showcase.ShowCaseTooltip
import github.com.st235.lib_showcase.SamuraiView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val samuraiView = findViewById<SamuraiView>(R.id.showcase)
        val text = findViewById<TextView>(R.id.text)
        val text2 = findViewById<TextView>(R.id.text2)

        samuraiView.overlayColor = 0x8FFFFFFF.toInt()
        samuraiView.frameColor = Color.BLACK
        samuraiView.frameThickness = 1F
        samuraiView.tooltip = ShowCaseTooltip(R.layout.tooltip_layout)

        Harakiri(samuraiView)
            .highlightDuration(2_500L)
            .capture(text, text2)
    }
}

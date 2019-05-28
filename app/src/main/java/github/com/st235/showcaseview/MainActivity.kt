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

        val showCaseView = findViewById<SamuraiView>(R.id.showcase)
        val text = findViewById<TextView>(R.id.text)
        val text2 = findViewById<TextView>(R.id.text2)

        showCaseView.overlayColor = 0x8FFFFFFF.toInt()
        showCaseView.frameColor = Color.BLACK
        showCaseView.frameThickness = 1F
        showCaseView.tooltip = ShowCaseTooltip(R.layout.tooltip_layout)

        Harakiri(showCaseView)
            .highlightDuration(2_500L)
            .capture(text, text2)
    }
}

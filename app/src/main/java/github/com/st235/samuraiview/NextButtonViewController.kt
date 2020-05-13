package github.com.st235.samuraiview

import android.view.View

class NextButtonViewController(nextButton: View, private val onStep: (Int) -> Unit) {

    private var currentStep = 0

    init {
        nextButton.setOnClickListener {
            currentStep += 1
            if (currentStep >= LAST) currentStep = LAST
            onStep(currentStep)
        }

        onStep(currentStep)
    }

    fun back(): Boolean {
        if (currentStep <= INTRODUCTION) {
            return false
        }
        currentStep -= 1
        onStep(currentStep)
        return true
    }

    companion object {
        const val INTRODUCTION = 0
        const val AUTHOR = 1
        const val SHARE = 2
        const val LAST = 3
    }
}

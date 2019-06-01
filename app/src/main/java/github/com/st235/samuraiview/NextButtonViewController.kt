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

    fun back() {
        currentStep -= 1
        if (currentStep <= INTRODUCTION) currentStep = INTRODUCTION
        onStep(currentStep)
    }

    companion object {
        const val INTRODUCTION = 0
        const val AUTHOR = 1
        const val SHARE = 2
        const val LAST = 3
    }
}

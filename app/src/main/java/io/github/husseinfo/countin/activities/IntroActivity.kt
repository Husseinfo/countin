package io.github.husseinfo.countin.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import io.github.husseinfo.countin.R
import io.github.husseinfo.countin.saveFirstRun

class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        super.setColorDoneText(resources.getColor(R.color.accent, theme))
        super.setColorSkipButton(resources.getColor(R.color.primaryVariant, theme))

        addSlide(
            AppIntroFragment.createInstance(
                title = getString(R.string.intro_title_1),
                description = getString(R.string.intro_desc_1),
                imageDrawable = R.drawable.ic_baseline_timeline_primary,
                titleColorRes = R.color.primaryVariant,
                descriptionColorRes = R.color.accentDark
            )
        )

        addSlide(
            AppIntroFragment.createInstance(
                title = getString(R.string.intro_title_2),
                description = getString(R.string.intro_desc_2),
                imageDrawable = R.drawable.ic_baseline_timeline_primary,
                titleColorRes = R.color.primaryVariant,
                descriptionColorRes = R.color.accentDark
            )
        )

        addSlide(
            AppIntroFragment.createInstance(
                title = getString(R.string.intro_title_3),
                description = getString(R.string.intro_desc_3),
                imageDrawable = R.drawable.ic_baseline_timeline_primary,
                titleColorRes = R.color.primaryVariant,
                descriptionColorRes = R.color.accentDark
            )
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        saveFirstRun(this)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        saveFirstRun(this)
        finish()
    }
}

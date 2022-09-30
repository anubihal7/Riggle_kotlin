package  com.rigle.servicehub.ui.introscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rigle.servicehub.R
import kotlinx.android.synthetic.main.intro_layout_screen.*


class IntroFragment: Fragment(R.layout.intro_layout_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bundle = arguments
        val list = bundle?.getParcelable<ScreenItem>("data")
        list?.let {
            description.setText(it.description)
            img.setImageResource(it.screenImg)
        }
    }
}
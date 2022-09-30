package  com.rigle.servicehub.ui.introscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroViewPagerAdapter(
    var mContext: Context,
    var mListScreen: ArrayList<ScreenItem>,
    activity: FragmentActivity
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return mListScreen.size
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putParcelable("data", mListScreen[position])
        val fragment = IntroFragment()
        fragment.arguments = bundle
        return fragment
    }
}
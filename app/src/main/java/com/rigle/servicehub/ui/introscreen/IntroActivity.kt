package  com.rigle.servicehub.ui.introscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.rigle.servicehub.R
import com.rigle.servicehub.databinding.ActivityIntroBinding
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.login.LoginActivity
import com.rigle.servicehub.ui.login.LoginActivityVM
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    var introViewPagerAdapter: IntroViewPagerAdapter? = null
    val viewModel: LoginActivityVM by viewModels()

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResource(): Int {

        return R.layout.activity_intro
    }

    override fun onCreateView() {
        val mList: ArrayList<ScreenItem> = ArrayList()
        mList.add(
            ScreenItem(
                R.string.onboarding_title1,
                R.string.onboarding_text1,
                R.drawable.intro_1
            )
        )
        mList.add(
            ScreenItem(
                R.string.onboarding_title2,
                R.string.onboarding_text2,
                R.drawable.intro_2
            )
        )
        mList.add(
            ScreenItem(
                R.string.onboarding_title3,
                R.string.onboarding_text3,
                R.drawable.intro_1
            )
        )

        introViewPagerAdapter = IntroViewPagerAdapter(this, mList, this)
        binding.screenPager.adapter = introViewPagerAdapter
        TabLayoutMediator(binding.tabIndicator, binding.screenPager) { tab, position ->
        }.attach()
        viewModel.onClick.observe(this, Observer {
            when (it.id) {
                R.id.btn_next -> {
                    var position = binding.screenPager.currentItem
                    if (position < mList.size) {
                        position++
                        binding.screenPager.currentItem = position
                    } else {
                        loadLastScreen()
                    }
                }

                R.id.btn_login -> {
                    startActivity(LoginActivity.newIntent(this))
                    finish()
                }
                R.id.btn_register -> {
                    val intent = LoginActivity.newIntent(this)
                    intent.putExtra("fromregister", true)
                    startActivity(intent)
                    finish()
                }
                R.id.tv_skip -> {
                    binding.screenPager.currentItem = mList.size
                }
            }
        })

        binding.tabIndicator.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == mList.size - 1) {
                    loadLastScreen()
                } else unLoadLastScreen()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        unLoadLastScreen()
    }

    private fun loadLastScreen() {
        binding.vNext.root.visibility = View.GONE
        binding.btnLogin.visibility = View.VISIBLE
        binding.btnRegister.visibility = View.VISIBLE
    }

    private fun unLoadLastScreen() {
        binding.vNext.root.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.GONE
        binding.btnRegister.visibility = View.GONE

    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }
}
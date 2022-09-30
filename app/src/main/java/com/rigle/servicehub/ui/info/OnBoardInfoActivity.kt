package com.rigle.servicehub.ui.info

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.rigle.servicehub.R
import com.rigle.servicehub.databinding.*
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.business.BusinessFragment
import com.rigle.servicehub.ui.runner.RunnerFragment
import com.rigle.servicehub.ui.main.MainActivity
import com.rigle.servicehub.ui.owner.OwnerFragment
import com.rigle.servicehub.utils.extension.startNewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardInfoActivity : BaseActivity<ActivityOnboardBinding>() {

    private lateinit var fragOwner: OwnerFragment
    private lateinit var fragBusiness: BusinessFragment
    private lateinit var fragRunner: RunnerFragment
    val viewModel: OnBoardInfoActivityVM by viewModels()
    private var currentPage = 1

    companion object {
        fun newIntent(activity: Context): Intent {
            val intent = Intent(activity, OnBoardInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_onboard
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv_back -> {
                    finish()
                }
                R.id.btn_next -> {
                    if (currentPage == 1) {
                        fragOwner.updateProfile()
                    } else if (currentPage == 2) {
                        fragBusiness.updateProfile()
                    }
                }
                R.id.tv_skip -> {
                    if (currentPage == 1) {
                        currentPage = 2
                        setCurrentPage()
                    } else if (currentPage == 3) {
                        startNewActivity(MainActivity::class.java, true)
                    }
                }
            }
        }
        initFragments()
        setCurrentPage()
    }

    private fun initFragments() {
        fragOwner = OwnerFragment()
        fragBusiness = BusinessFragment()
        fragRunner = RunnerFragment()

        binding.t1.index = "1"
        binding.t1.title = "Owner Info"
        binding.t2.index = "2"
        binding.t2.title = "Business Info"
        binding.t3.index = "3"
        binding.t3.title = "Add Runner"

        binding.t1.v1.setOnClickListener {
            //  cuurentPage = 1
            //  setCurrentPage()
        }
        binding.t2.v1.setOnClickListener {
            //   cuurentPage = 2
            //  setCurrentPage()
        }
        binding.t3.v1.setOnClickListener {
            currentPage = 3
            setCurrentPage()
        }

    }

    fun setCurrentPage(page: Int) {
        currentPage = page
        setCurrentPage()
    }

    private fun setCurrentPage() {
        when (currentPage) {
            1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, fragOwner)
                    .commit()
                binding.vBottom.tvSkip.visibility = View.VISIBLE
            }
            2 -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, fragBusiness)
                    .commit()
                binding.vBottom.tvSkip.visibility = View.GONE
            }
            3 -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, fragRunner)
                    .commit()
                binding.vBottom.tvSkip.visibility = View.VISIBLE
            }
        }
        updatePage()
    }

    private fun updatePage() {
        binding.t1.current = currentPage == 1
        binding.t1.add = currentPage >= 1
        binding.t2.current = currentPage == 2
        binding.t2.add = currentPage >= 2
        binding.t3.current = currentPage == 3
        binding.t3.add = currentPage >= 3

    }

    override fun onBackPressed() {
        if (currentPage > 1) {
            currentPage--
            setCurrentPage()
        } else
            super.onBackPressed()
    }


}
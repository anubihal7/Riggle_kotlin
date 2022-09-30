package com.rigle.servicehub.ui.main

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.rigle.servicehub.R
import com.rigle.servicehub.databinding.ActivityMainBinding
import com.rigle.servicehub.databinding.ViewBottomBinding
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.base.sheet.MessageSheetBuilder
import com.rigle.servicehub.ui.login.LoginActivity
import com.rigle.servicehub.utils.extension.showInfoToast
import com.rigle.servicehub.utils.extension.showSheet
import com.rigle.servicehub.utils.extension.showSuccessToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var listMain = arrayListOf<Option>()

    private val mainViewModel: MainViewModel by viewModels()
    private var navController: NavController? = null
    private var currentPage: Int = 0

    companion object {
        fun newIntent(activity: Context): Intent {
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

        fun newIntent(activity: Context, @IdRes screenId: Int): Intent {
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("screen", screenId)
            return intent
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): BaseViewModel {
        return mainViewModel
    }

    override fun onCreateView() {
        inItOptions()
        setupTabUI()
        addNavController()
        mainViewModel.onClick.observe(this, Observer {
            when (it.id) {

            }
        })
        mainViewModel.ping()
        processIntent()
    }

    private fun processIntent() {
        when (intent.getIntExtra("screen", 0)) {
            R.id.frg_edit_account -> {
                navController?.navigate(
                    R.id.frg_edit_account, bundleOf(
                        Pair("title", getString(R.string.add_runner)),
                        Pair("type", 3)
                    )
                )
            }
        }
    }

    private fun addNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            currentPage = destination.id
            updateMainTab()
        }
    }

    private fun inItOptions() {
        listMain.add(Option(R.id.frg_order, getString(R.string.orders), R.drawable.ic_cart))
        listMain.add(Option(R.id.frg_inventory, "Inventory", R.drawable.ic_package))
        listMain.add(
            Option(
                R.id.frg_dashboard, "Home", R.drawable.ic_home,
                enable = true
            )
        )
        listMain.add(Option(0, "Purchase", R.drawable.ic_bag))
        listMain.add(Option(R.id.frg_more, "More", R.drawable.ic_more))
    }


    override fun onStart() {
        super.onStart()
    }




    private fun setupTabUI() {
        currentPage = R.id.frg_dashboard
        updateMainTab()
    }

    private fun updateMainTab() {
        for (i in 0 until binding.vMain.childCount) {
            val b =
                if (i == 2) binding.v3 else DataBindingUtil.findBinding<ViewBottomBinding>(binding.vMain[i])
            listMain[i].let {
                it.checked = currentPage == it.id
                b?.bean = it
                b?.callback = object : RVAdapter.Callback<Option> {
                    override fun onItemClick(v: View, m: Option) {
                        if (m.id == 0) {
                            showInfoToast("In progress")
                        } else {
                            currentPage = it.id
                            navController?.navigate(m.id)
                            updateMainTab()
                        }
                    }
                }
            }
        }
    }


    private fun onClickNavMenu(m: Option) {
        when (m.id) {
            2 ->
                showLogout()
            1 ->
                showInfoToast("In progress")
            else ->
                navController?.navigate(m.id)
        }
    }

    override fun onBackPressed() {
        if (navController?.navigateUp() == true) {

        } else super.onBackPressed()
    }

    private fun showLogout() {
        val sheet = MessageSheetBuilder()
            .setTitleText("Logout")
            .setMessageText("Are you sure want to logout?")
            .setPositiveButton("Ok")
            .setNegativeButton("cancel")
            .setListener(object : MessageSheetBuilder.Listener {
                override fun onPositiveButtonClick(sheet: BottomSheetDialogFragment) {
                    sheet.dismissAllowingStateLoss()
                    sharedPrefManager.clearUser()
                    val i = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(i)
                    showSuccessToast("Logout")
                    finishAffinity()
                }


            }).build()
        showSheet(sheet)

    }


/*  private fun setupObserver() {
      mainViewModel.users.observe(this, {
          when (it.status) {
              Status.SUCCESS -> {
                  progressBar.visibility = View.GONE
                  it.data?.let { users -> renderList(users) }
                  recyclerView.visibility = View.VISIBLE
              }
              Status.LOADING -> {
                  progressBar.visibility = View.VISIBLE
                  recyclerView.visibility = View.GONE
              }
              Status.ERROR -> {
                  //Handle Error
                  progressBar.visibility = View.GONE
                  Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
              }
          }
      })
  }*/


    data class Option(
        val id: Int,
        var title: String,
        var icon: Int,
        var checked: Boolean = false,
        val enable: Boolean = true
    )
}

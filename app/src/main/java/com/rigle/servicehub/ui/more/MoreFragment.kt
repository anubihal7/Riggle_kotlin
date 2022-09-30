package com.rigle.servicehub.ui.more

import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Company
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentMoreBinding
import com.rigle.servicehub.databinding.HolderMenuBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.base.sheet.MessageSheetBuilder
import com.rigle.servicehub.ui.main.MainActivity
import com.rigle.servicehub.ui.settle.SettlementActivity
import com.rigle.servicehub.ui.welcome.WelcomeActivity
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showSheet
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>() {
    private val viewmodel: MoreViewModel by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.fragment_more
    }

    override fun getViewModel(): BaseViewModel = viewmodel
    override fun getHorizontalBar(): LinearProgressIndicator? {
        return binding.toolbar.pbOne
    }

    override fun onStart() {
        super.onStart()
        viewmodel.getCompany()
    }

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_settle) {
                val intent = SettlementActivity.newIntent(requireContext())
                startActivity(intent)
            } else {
                if (v.id == R.id.ll_renew) {
                    Navigation.findNavController(v).navigate(R.id.frag_subscription)
                }
            }
        })
        viewmodel.obrCompany.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showPlan(resource.data)
                    }
                    Status.ERROR -> {
                        hideLoading()
                    }
                    Status.CACHED -> {
                        showPlan(resource.data)
                    }
                }
            })
        viewmodel.obrPlan.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        resource.data?.let {
                            openBrowser(it.addProductFormLink)
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    private fun showPlan(data: Company?) {
        data?.extra?.let { it ->
            if (it.isPlanActive == true) {
                binding.vPlan.visibility = View.VISIBLE
                val days = getRemaingTime(it.planEndDate)
                if (days > 1) {
                    binding.tvRemaining.text = "$days Days Left"
                } else {
                    binding.tvRemaining.text = "$days Day Left"
                }
                val total = getDates(it.planStartDate.toString(), it.planEndDate.toString())
                binding.tvPlan.text = "Subscription plan for ${total.size} days"
                val percent = ((days.toFloat() / total.size) * 100).toInt()
                if (percent > 15) {
                    binding.llRenew.visibility = View.GONE
                    binding.tvRemaining.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                } else {
                    binding.llRenew.visibility = View.VISIBLE
                    binding.tvRemaining.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.orange
                        )
                    )
                }
                val animator = ValueAnimator.ofInt(0, percent)
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.duration = 1000
                animator.startDelay = 500
                animator.addUpdateListener {
                    val t = it.animatedValue as Int
                    binding.cbOne.progress = t.toFloat()
                }
                animator.start()
                binding.lottie.setAnimation("congrats.json")
                binding.lottie.playAnimation()
            } else {
                binding.vPlan.visibility = View.GONE
            }

        }
    }

    private fun getRemaingTime(planEndDate: String?): Int {
        /*2022-09-04*/
        return try {
            val c = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val list = getDates(s.format(c.time), planEndDate.toString())
            list.size
        } catch (e: Exception) {
            0
        }
    }

    private fun openBrowser(link: String?) {
        try {
            val url = Uri.parse(link)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = url
            startActivity(i)
        } catch (e: Exception) {
            showErrorToast("Url not valid")
        }
    }

    private fun initViews() {
        sharedPrefManager.getCurrentUser()?.fullName?.let {
            binding.toolbar.tvTitle.text = it.capitalize()
        }
        val listOther = arrayListOf<MainActivity.Option>()
        listOther.add(
            MainActivity.Option(
                R.id.frag_subscription,
                getString(R.string.subscription).uppercase(Locale.getDefault()),
                R.drawable.ic_award
            )
        )
        listOther.add(
            MainActivity.Option(
                2,
                "Add Products".uppercase(Locale.getDefault()),
                R.drawable.ic_bag
            )
        )
        listOther.add(
            MainActivity.Option(
                R.id.frg_myaccount,
                "Account".uppercase(Locale.getDefault()),
                R.drawable.ic_user2
            )
        )
        listOther.add(
            MainActivity.Option(
                R.id.frg_statement,
                "Statements".uppercase(Locale.getDefault()),
                R.drawable.ic_statement
            )
        )
        listOther.add(
            MainActivity.Option(
                R.id.frg_referral,
                getString(R.string.coupon_codes).uppercase(Locale.getDefault()),
                R.drawable.ic_tag
            )
        )
        listOther.add(
            MainActivity.Option(
                R.id.frg_manage_runner,
                "Manage Runners".uppercase(Locale.getDefault()),
                R.drawable.ic_van
            )
        )
        listOther.add(
            MainActivity.Option(
                R.id.frg_manage_sales,
                getString(R.string.manage_sales_person).uppercase(Locale.getDefault()),
                R.drawable.ic_brifcase
            )
        )
        listOther.add(
            MainActivity.Option(
                1,
                "Logout".uppercase(Locale.getDefault()),
                R.drawable.ic_upload
            )
        )
        binding.rvOne.layoutManager = LinearLayoutManager(context)
        val adapter = RVAdapter<MainActivity.Option, HolderMenuBinding>(
            R.layout.holder_menu,
            BR.bean,
            object : RVAdapter.Callback<MainActivity.Option> {
                override fun onItemClick(v: View, m: MainActivity.Option) {
                    when (m.id) {
                        1 -> {
                            showLogout()
                        }
                        2 -> {
                            viewmodel.getPlans()
                        }
                        0 -> {
                        }
                        else -> Navigation.findNavController(v).navigate(m.id);
                    }
                }
            })
        adapter.setDataList(listOther)
        binding.rvOne.adapter = adapter
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
                    val intent = Intent(context, WelcomeActivity::class.java)
                    startActivity(intent)
                    activity?.finishAffinity()
                }


            }).build()
        showSheet(sheet)

    }

    private fun getDates(dateString1: String, dateString2: String): List<Date> {
        try {
            val dates = ArrayList<Date>()
            val df1 = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            var date1: Date? = null
            var date2: Date? = null
            try {
                date1 = df1.parse(dateString1)
                date2 = df1.parse(dateString2)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val cal1 = Calendar.getInstance()
            cal1.time = date1
            val cal2 = Calendar.getInstance()
            cal2.time = date2
            while (!cal1.after(cal2)) {
                dates.add(cal1.time)
                cal1.add(Calendar.DATE, 1)
            }
            return dates
        } catch (e: Exception) {
            showErrorToast("Error in plan date start=>$dateString1  end date=> $dateString2")
            return emptyList()
        }
    }
}
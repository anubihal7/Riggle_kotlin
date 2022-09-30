package com.rigle.servicehub.ui.account.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.MyAccountResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.*
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.business.BusinessFragment
import com.rigle.servicehub.ui.owner.OwnerFragment
import com.rigle.servicehub.ui.runner.RunnerFragment
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.showErrorToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAccountFragment : BaseFragment<FragmentEditAccountBinding>() {
    private val viewmodel: EditAccountViewModel by viewModels()
    private lateinit var fragmentOwner: OwnerFragment
    private lateinit var fragmentBusiness: BusinessFragment
    private lateinit var fragmentRunner: RunnerFragment
    override fun getLayoutResource(): Int {
        return R.layout.fragment_edit_account
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                backNavigate()

            } else if (v.id == R.id.ll_save) {
                when (arguments?.getInt("type", 0)) {
                    1 -> {
                        fragmentOwner.updateProfile()
                    }
                    2 -> {
                        fragmentBusiness.updateProfile()
                    }

                }
            }
        })
        viewmodel.obrMyAccount.observe(
            this,
            SingleRequestEvent.RequestObserver<MyAccountResponse> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        updateUi(resource.data)
                    }
                    Status.ERROR -> {
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {
                        updateUi(resource.data)
                    }
                }
            })
    }

    fun backNavigate() {
        view?.let {
            Navigation.findNavController(it).navigateUp()
        }

    }

    private fun updateUi(data: MyAccountResponse?) {

    }


    override fun onStart() {
        super.onStart()
        viewmodel.getMyAccount()
    }


    private fun initViews() {
        fragmentOwner = OwnerFragment()
        fragmentBusiness = BusinessFragment()
        fragmentRunner = RunnerFragment()
        binding.toolbar.tvTitle.text = getString(R.string.my_account)
        arguments?.getString("title")?.let {
            binding.tvOne.text = it
        }
        when (arguments?.getInt("type", 0)) {
            1 -> {
                childFragmentManager.beginTransaction().replace(R.id.ll_one, fragmentOwner).commit()
            }
            2 -> {
                val bundle = Bundle()
                bundle.putBoolean("state", true)
                fragmentBusiness.arguments = bundle
                childFragmentManager.beginTransaction().replace(R.id.ll_one, fragmentBusiness)
                    .commit()
            }
            3 -> {
                arguments?.getString("title")?.let {
                    binding.toolbar.tvTitle.text = it
                }
                binding.rlOne.visibility = View.GONE
                childFragmentManager.beginTransaction().replace(R.id.ll_one, fragmentRunner)
                    .commit()
            }
        }
    }

}
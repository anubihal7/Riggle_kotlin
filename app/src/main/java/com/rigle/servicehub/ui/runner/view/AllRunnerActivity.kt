package com.rigle.servicehub.ui.runner.view

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Runner
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.ActivityRunnerListBinding
import com.rigle.servicehub.databinding.HolderViewRunnerBinding
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.main.MainActivity
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllRunnerActivity : BaseActivity<ActivityRunnerListBinding>() {
    private lateinit var adapter: RVAdapter<Runner, HolderViewRunnerBinding>
    private val viewmodel: AllRunnerViewModel by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.activity_runner_list
    }

    override fun getViewModel(): BaseViewModel = viewmodel


    override fun onCreateView() {
        initViews()
        initAdapter()
        viewmodel.onClick.observe(this, Observer<View> { v ->
            when (v.id) {
                R.id.iv_back -> {
                    finish()
                }
                R.id.btn_go -> {
                    startActivity(MainActivity.newIntent(this, R.id.frg_edit_account))
                    finishAffinity()
                }
            }
        })
        viewmodel.obrRunner.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        adapter.setDataList(resource.data?.results)
                        updateEmptyView(adapter.itemCount, "No runner found")
                    }
                    Status.ERROR -> {
                        binding.loader.hideLoading()
                        updateEmptyView(adapter.itemCount, resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })

    }

    override fun onStart() {
        super.onStart()
        viewmodel.getRunner()
    }

    private fun updateEmptyView(itemCount: Int, message: String?) {
        if (itemCount > 0) {
            binding.empty.hideEmptyView()
        } else {
            binding.empty.showEmptyView(message)
        }
    }

    private fun initAdapter() {
        adapter =
            RVAdapter(R.layout.holder_view_runner, BR.bean, object : RVAdapter.Callback<Runner> {
                override fun onItemClick(v: View, m: Runner) {
                    val intent = Intent()
                    intent.putExtra("data", m)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            })
        binding.rvOne.layoutManager = LinearLayoutManager(this)
        binding.rvOne.adapter = adapter

    }


    private fun initViews() {
        binding.empty.btnGo.text = getString(R.string.add_runner)
        binding.toolbar.tvTitle.text = getString(R.string.all_runners)
    }

}

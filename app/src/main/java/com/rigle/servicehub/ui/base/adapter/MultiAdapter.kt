package com.rigle.servicehub.ui.base.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.rigle.servicehub.BR

abstract class MultiAdapter<M>(
    private val callback: Callback<M>
) : RecyclerView.Adapter<MultiAdapter.Holder>() {
    private val dataList = ObservableArrayList<M>()

    interface Callback<M> {
        fun onItemClick(v: View, m: M)
        fun onItemClick(v: View, m: M, position: Int) {
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataList(newList: List<M>?) {
        dataList.clear()
        newList?.let { dataList.addAll(it) }
        notifyDataSetChanged()
    }

    fun addDataList(newDataList: List<M>?) {
        val positionStart = dataList.size
        newDataList?.let {
            val itemCount = newDataList.size
            dataList.addAll(newDataList)
            notifyItemRangeInserted(positionStart, itemCount)
        }
    }

    fun getDataList(): ObservableArrayList<M> {
        return dataList
    }

    fun getData(index: Int): M? {
        if (index < dataList.size)
            return dataList[index]
        return null
    }

    fun clearList() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun addData(data: M) {
        val positionStart = dataList.size
        dataList.add(data)
        notifyItemInserted(positionStart)
    }


    class Holder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.setVariable(BR.holder, holder)
        holder.binding.setVariable(BR.bean, dataList[position])
        holder.binding.executePendingBindings()
    }

    abstract fun getLayoutResource(
        viewType: Int
    ): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val holder = Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), getLayoutResource(viewType),
                parent,
                false
            )
        )
        holder.binding.setVariable(BR.callback, callback)
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return getViewType(dataList[position])
    }

    abstract fun getViewType(bean: M): Int
}
package com.shopping.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.shopping.R
import com.shopping.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentOrdersBinding
    private val orderAdapter by lazy {
        OrderAdapter(cancelClick = {
            val order = it
            order.isCancelled = true
            viewModel.getDao().editOrder(order)
        })
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            toolbarTitleTextView.isVisible = true
            toolbarTitleTextView.text = getString(R.string.orders)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
    }

    private fun initAdapter() {
        binding.rvOrders.adapter = orderAdapter
        val orderList = viewModel.getDao().getOrderList()
        orderAdapter.setData(orderList)
    }
}
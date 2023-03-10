package com.shopping.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shopping.R
import com.shopping.data.Order
import com.shopping.databinding.ItemOrdersBinding

class OrderAdapter(
    private val cancelClick: (Order) -> Unit,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var orderList = arrayListOf<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemOrdersBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    fun setData(list: List<Order>) {
        orderList.clear()
        orderList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvOrderId.text = "OrderId = " + order.orderId.toString()
            binding.tvMaskedCardNo.text = "Card No = " + order.maskedCardNo
            binding.tvAmount.text = "${order.amount} $"
            binding.tvDate.text = "Date = " + order.createdDate.toString()

            if (order.isCancelled) {
                binding.tvCancel.text = binding.root.context.getString(R.string.cancelled)
                binding.clBackground.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.cancel_red
                    )
                )
            }

            binding.tvCancel.setOnClickListener {
                if (order.isCancelled)
                    return@setOnClickListener

                cancelClick.invoke(order)
                binding.tvCancel.text = binding.root.context.getString(R.string.cancelled)
                binding.clBackground.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.cancel_red
                    )
                )

            }

        }
    }

}
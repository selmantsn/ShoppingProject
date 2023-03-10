package com.shopping.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shopping.data.Product
import com.shopping.databinding.ItemProductBinding

class ProductAdapter(
    private val onClickListener: (Product, ClickType) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var productList = arrayListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(productList[position], position)
    }

    override fun getItemCount(): Int = productList.size

    fun setData(list: List<Product>) {
        productList.clear()
        productList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, position: Int) {
            binding.tvProductName.text = "${product.name} (${product.price} $)"
            binding.tvProductCount.text = product.currentAmount.toString()

            binding.tvPlus.setOnClickListener {
                if (product.currentAmount < 5) {
                    product.currentAmount++
                    onClickListener.invoke(product, ClickType.ADD)
                    notifyItemChanged(position)
                }
            }
            binding.tvMinus.setOnClickListener {
                if (product.currentAmount > 0) {
                    product.currentAmount--
                    onClickListener.invoke(product, ClickType.REMOVE)
                    notifyItemChanged(position)
                }
            }
        }
    }


    enum class ClickType { ADD, REMOVE }
}
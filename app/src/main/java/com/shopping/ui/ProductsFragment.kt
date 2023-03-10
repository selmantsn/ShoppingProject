package com.shopping.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.shopping.R
import com.shopping.data.Product
import com.shopping.databinding.FragmentProductsBinding


@SuppressLint("SetTextI18n")
class ProductsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private var totalPrice: Double = 0.0

    private val productAdapter by lazy {
        ProductAdapter(
            onClickListener = { product, type ->
                val item =
                    viewModel.getDao().getProductList().find { it.name == product.name }

                when (type) {
                    ProductAdapter.ClickType.ADD -> {
                        item?.let {
                            it.currentAmount++
                            viewModel.getDao().editProduct(it)
                        } ?: run {
                            viewModel.getDao().addProduct(product)
                        }
                        totalPrice += product.price

                    }
                    ProductAdapter.ClickType.REMOVE -> {
                        item?.let {
                            if (it.currentAmount > 1) {
                                it.currentAmount--
                                viewModel.getDao().editProduct(it)
                            } else {
                                viewModel.getDao().removeProduct(it)
                            }
                            totalPrice -= product.price
                        }
                    }
                }

                binding.tvTotalPrice.text = "${String.format("%.2f", totalPrice)} $"
            }
        )
    }

    private val productList: ArrayList<Product> = arrayListOf(
        Product(name = "Pencil", price = 9.58),
        Product(name = "Sheet", price = 0.61),
        Product(name = "Eraser", price = 30.00),
        Product(name = "Notebook", price = 40.00),
        Product(name = "Book", price = 50.82)
    )

    private lateinit var binding: FragmentProductsBinding

    private fun initToolbar() {
        binding.toolbar.apply {
            toolbarTitleTextView.isVisible = true
            toolbarTitleTextView.text = getString(R.string.products)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("Success")
            ?.observe(viewLifecycleOwner) {
                productList.forEach {
                    it.currentAmount = 0
                }
                productAdapter.setData(productList)
            }


        initToolbar()
        initAdapter()
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        totalPrice = viewModel.getDao().getTotalPrice()
        binding.tvTotalPrice.text = "${String.format("%.2f", totalPrice)} $"

        binding.btnGoPayment.setOnClickListener {
            if (totalPrice == 0.0) {
                return@setOnClickListener
            }

            findNavController().navigate(R.id.to_payment_fragment)
        }
    }

    private fun initAdapter() {
        binding.rvProducts.adapter = productAdapter
        val dbList = viewModel.getDao().getProductList()
        dbList.forEach { parent ->
            productList.forEach { child ->
                if (parent.name == child.name) {
                    child.currentAmount = parent.currentAmount
                }
            }
        }
        productAdapter.setData(productList)
    }
}
package com.shopping.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.shopping.R
import com.shopping.data.Order
import com.shopping.databinding.FragmentPaymentBinding
import com.shopping.util.checkLuhnAlgorithm
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private val viewModel: MainViewModel by activityViewModels()

    private fun initToolbar() {
        binding.toolbar.apply {
            toolbarTitleTextView.isVisible = true
            toolbarTitleTextView.text = getString(R.string.payment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        cardTextChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun cardTextChanged() {

        binding.etCardHolderName.doAfterTextChanged {
            binding.ilCardHolderName.isErrorEnabled = false
        }

        var previousLength = 0
        var isBackSpace: Boolean
        binding.etExpireDate.doBeforeTextChanged { text, _, _, _ ->
            previousLength = text.toString().length
        }
        binding.etExpireDate.doAfterTextChanged {
            isBackSpace = previousLength > it.toString().length
            binding.ilExpireDate.isErrorEnabled = false

            if (isBackSpace)
                return@doAfterTextChanged
            val input = it.toString()
            val formatter = SimpleDateFormat("MM/yy", Locale.getDefault())
            val expiryDate = Calendar.getInstance()

            try {
                expiryDate.time = formatter.parse(input) as Date
            } catch (e: ParseException) {
                if (input.length == 2 && !input.endsWith("/")) {
                    val month = input.toInt()
                    if (month <= 12) {
                        binding.etExpireDate.setText(binding.etExpireDate.text.toString() + "/")
                        binding.etExpireDate.setSelection(binding.etExpireDate.text.toString().length)
                    }
                } else if (input.length == 2 && input.endsWith("/")) {
                    val month = input.toInt()
                    if (month <= 12) {
                        binding.etExpireDate.setText(
                            binding.etExpireDate.text.toString().substring(0, 1)
                        )
                        binding.etExpireDate.setSelection(binding.etExpireDate.text.toString().length)
                    } else {
                        binding.etExpireDate.setText("")
                        binding.etExpireDate.setSelection(binding.etExpireDate.text.toString().length)
                        Toast.makeText(requireContext(), "Enter a valid month", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else if (input.length == 1) {
                    val month = input.toInt()
                    if (month > 1) {
                        binding.etExpireDate.setText("0" + binding.etExpireDate.text.toString() + "/")
                        binding.etExpireDate.setSelection(binding.etExpireDate.text.toString().length)
                    }
                } else {
                }
            } catch (e: Exception) {

            }

        }

        binding.etCardNo.doAfterTextChanged {
            if (!checkLuhnAlgorithm(it.toString())) {
                binding.ilCardNo.isErrorEnabled = true
                binding.ilCardNo.error = "Kart numarası uyumlu değil"
            } else {
                binding.ilCardNo.isErrorEnabled = false
            }

            if (it?.length == 2) {
                binding.ivCardImage.setImageResource(
                    when {
                        it.startsWith(CardType.VISA.startNumber) -> {
                            CardType.VISA.image
                        }
                        it.startsWith(CardType.MASTER_CARD.startNumber) -> {
                            CardType.MASTER_CARD.image
                        }
                        it.startsWith(CardType.AMERICAN_EXPRESS.startNumber) -> {
                            CardType.AMERICAN_EXPRESS.image
                        }
                        else -> {
                            R.drawable.unknown_card
                        }
                    }
                )
            } else if ((it?.length ?: 0) < 2) {
                binding.ivCardImage.setImageResource(R.drawable.unknown_card)
            }
        }
    }

    private fun initViews() {

        val totalPrice = viewModel.getDao().getTotalPrice()
        binding.tvTotalPrice.text = "${String.format("%.2f", totalPrice)} $"

        binding.btnFinishPayment.setOnClickListener {
            var canFinish = true
            if (binding.etCardHolderName.text.isNullOrEmpty()) {
                binding.ilCardHolderName.isErrorEnabled = true
                binding.ilCardHolderName.error = getString(R.string.cannot_empty)
                canFinish = false
            }
            if (binding.etCardNo.text.isNullOrEmpty() || !checkLuhnAlgorithm(binding.etCardNo.text.toString())) {
                binding.ilCardNo.isErrorEnabled = true
                binding.ilCardNo.error = "Kart numarası uyumlu değil"
                canFinish = false
            }
            if (binding.etExpireDate.text?.length != 5) {
                binding.ilExpireDate.error = getString(R.string.invalid_date)
                canFinish = false
            }

            if (canFinish) {

                val maskedCardNo = binding.etCardNo.text.toString().let {
                    if (it.length > 8) {
                        it.replaceRange(4, it.length - 4, "*".repeat(it.length - 8))
                    } else {
                        it
                    }
                }
                viewModel.getDao().addOrder(
                    Order(
                        maskedCardNo = maskedCardNo,
                        amount = totalPrice,
                        createdDate = Calendar.getInstance().time
                    )
                )
                viewModel.getDao().removeProductList()
                val navController = findNavController()
                navController.previousBackStackEntry?.savedStateHandle?.set("Success", true)
                navController.popBackStack()
                Toast.makeText(requireContext(), getString(R.string.order_received), Toast.LENGTH_SHORT).show()

            }
        }
    }

    enum class CardType(val startNumber: String, val image: Int) {
        VISA("4", R.drawable.visa),
        MASTER_CARD("5", R.drawable.mastercard),
        AMERICAN_EXPRESS("37", R.drawable.american_express)
    }


}
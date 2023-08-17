package com.tenant.mytenant.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tenant.mytenant.R
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.databinding.FragmentPaymentBinding
import com.tenant.mytenant.ui.rentpayment.Payment
import com.tenant.mytenant.ui.register.UserRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserPaymentFragment : Fragment() {
  private var _binding: FragmentPaymentBinding? = null
  private  val binding get() = _binding!!

    lateinit var  userRegistration: UserRegistration
    lateinit var  payment: Payment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // year =  Calendar.getInstance().get(Calendar.YEAR).toString()
       // val indexofmonth = Calendar.getInstance().get(Calendar.MONTH)
      //  binding.spinnerMonth.setSelection(indexofmonth)

        userRegistration = arguments?.getSerializable("OK") as UserRegistration

        payment = arguments?.getSerializable("PAYMENT") as Payment
        binding.rentPaymentAmount.setText(payment.rentAmount.toString().trim())
        binding.paidAmount.setText(payment.paidAmount.toString().trim())
        binding.dueAmount.setText(payment.dueAmount.toString().trim())
        binding.dueAmount.setTextColor(resources.getColor(R.color.red))
        // set the spinner selection
        binding.spinnerMonth.isEnabled = false
        val array = resources.getStringArray(R.array.month)
        for (i in array.indices){
            if (payment.month == array[i]){
                binding.spinnerMonth.setSelection(i)
                break
            }
        }
        var rentAmount = 0.0
        binding.paidAmount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var paidAmont = 0.0
                if (!s.toString().isEmpty()){
                    rentAmount =binding.rentPaymentAmount.text.toString().toDouble()
                    paidAmont = s!!.toString().toDouble()
                    val due = rentAmount.minus(paidAmont)
                    binding.dueAmount.setText(due.toString())
                    binding.dueAmount.setTextColor(resources.getColor(R.color.red))
                }else{
                    val due = rentAmount.minus(paidAmont)
                    binding.dueAmount.setText(due.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        /*binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected( parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                month = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }*/

        binding.savePayment.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                UserDataBase.getInstance(requireContext()).userDao().paymentUpdate(
                    Payment(payment.id,payment.month,payment.year, userRegistration.mobileNumber,
                        binding.rentPaymentAmount.text.toString().toDouble(),
                        binding.paidAmount.text.toString().toDouble(),
                        binding.dueAmount.text.toString().toDouble())
                )
                if (binding.dueAmount.text.toString() == "0.0"){
                    onUpDateStatus(false)
                }else{
                    onUpDateStatus(true)
                }
            }

           Toast.makeText(requireContext(), "Payment updated successfully", Toast.LENGTH_LONG).show()
        }
    }

    private fun onUpDateStatus(b: Boolean) {
        UserDataBase.getInstance(requireContext()).userDao().getUpdate(
            UserRegistration(
            userRegistration.userName,
            userRegistration.mobileNumber,
            userRegistration.aadharNumber,
            userRegistration.roomNumber,
            userRegistration.rentAmount,
            userRegistration.joinDate,b)
        )
    }
}
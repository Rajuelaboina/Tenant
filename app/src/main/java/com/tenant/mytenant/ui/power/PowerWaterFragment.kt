package com.tenant.mytenant.ui.power

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tenant.mytenant.R
import com.tenant.mytenant.databinding.FragmentPowerWaterBinding
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.powerBillSelectedListener
import java.util.*

class PowerWaterFragment : Fragment(), powerBillSelectedListener {
    // TODO: Rename and change types of parameters

    private  var _binding: FragmentPowerWaterBinding? = null
    private val binding get() = _binding!!
    var billAmount: Double? = null
    var due:Double? = null
    lateinit var year:String
    lateinit var month:String
    private  lateinit var  userRegistration: UserRegistration

    lateinit var viewModel: PowerWaterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPowerWaterBinding.inflate(inflater, container, false)
        userRegistration = arguments?.getSerializable("OK") as UserRegistration

        viewModel = ViewModelProvider(
            this,
            PowerWaterViewModelFactory(this, requireContext(), userRegistration.mobileNumber)
        )[PowerWaterViewModel::class.java]
        binding.viewModel =viewModel
        binding.executePendingBindings()
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         year =  Calendar.getInstance().get(Calendar.YEAR).toString()



       /* binding.paidAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var paidAmount = 0.0

                if (!s.toString().isEmpty()){
                      billAmount = binding.powerWaterPaymentAmount.text.toString().trim().toDouble()
                      paidAmount = s.toString().trim().toDouble()
                      due = billAmount!!.minus(paidAmount)
                      binding.dueAmount.setText(due.toString())
                      binding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
                }else{
                    due = billAmount!!.minus(paidAmount)
                    binding.dueAmount.setText(due.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {
               // TODO("Not yet implemented")
            }
        })*/

        //spinner
        /*val indexofmonth = Calendar.getInstance().get(Calendar.MONTH)
        binding.spinnerMonth.setSelection(indexofmonth)
        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                month = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }*/

        /*binding.savePayment.setOnClickListener {
            CoroutineScope(IO).launch {
                UserDataBase.getInstance(requireContext()).userDao().insertPowerWaterPayment(
                    PowerWaterPayment(0, month,year,userRegistration.mobileNumber,binding.powerWaterPaymentAmount.text.toString(),
                                      binding.paidAmount.text.toString(),binding.dueAmount.text.toString())
                )
            }
            Toast.makeText(requireContext(), "Payment insert successfully", Toast.LENGTH_LONG).show()
        }*/

    }



    override fun onPaidListener(due: Double) {
        var paidAmount = 0.0
        binding.dueAmount.setText(due.toString())
        binding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
      /*  if (!s.isEmpty()){
            billAmount = binding.powerWaterPaymentAmount.text.toString().trim().toDouble()
            paidAmount = s.trim().toDouble()
            due = billAmount!!.minus(paidAmount)
            binding.dueAmount.setText(due.toString())
            binding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
        }else{
            due = billAmount!!.minus(paidAmount)
            binding.dueAmount.setText(due.toString())
        }*/
    }

    override fun successListener() {
      //  showToast("Payment insert successfully")
       // Toast.makeText(requireContext(), "Payment insert successfully", Toast.LENGTH_LONG).show()
        var bundle = Bundle()
        bundle.putSerializable("OK",userRegistration)

        val fragment = PowerWaterFragment()
        fragment.arguments = bundle

        requireActivity().runOnUiThread {
            findNavController().navigate(R.id.action_powerWaterFragment_to_powerWaterListFragment,bundle)
        }

    }

    override fun errorListener() {
       // showToast("Fields are empty!")
       // Toast.makeText(requireContext(), "Fields are empty!", Toast.LENGTH_LONG).show()
    }

   fun showToast(msg:String){
       Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
   }
}
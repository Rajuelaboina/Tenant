package com.tenant.mytenant.ui.billpayment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tenant.mytenant.R
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.databinding.DialogPaymentBinding
import com.tenant.mytenant.databinding.FragmentPoweWaterListBinding
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.OnItemClicked
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@Suppress("DEPRECATION")
class PowerWaterListFragment : Fragment(), OnItemClicked {
    private var _binding: FragmentPoweWaterListBinding? =null
    private val binding get() = _binding!!
    private  lateinit var  userRegistration: UserRegistration
    private lateinit var viewModel : PowerWaterListViewModel
    private lateinit var dialogPaymentBinding: DialogPaymentBinding
    private  var month = ""
    var mobileNumber = ""
    var year=""
    var list = ArrayList<PowerWaterPayment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentPoweWaterListBinding.inflate(inflater, container, false)
        userRegistration = arguments?.getSerializable("OK") as UserRegistration
        mobileNumber = userRegistration.mobileNumber
        viewModel = ViewModelProvider(this,
            PowerWaterListViewModelFactory(requireContext(),userRegistration.mobileNumber)
        )[PowerWaterListViewModel::class.java]
        binding.viewModel =viewModel
        binding.executePendingBindings()
        PowerWaterAdapter.setOnItemSelected(this)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           binding.listpaymentLinear.visibility = View.INVISIBLE
        showAllPaymets()
        year =  Calendar.getInstance().get(Calendar.YEAR).toString()
        //Log.e("year","year: $year")
        binding.textViewMonth.text = "Month & Year"
        binding.fabPower.setOnClickListener {
            /*bundle.putSerializable("OK",userRegistration)
            val fragment = PowerWaterFragment()
            fragment.arguments = bundle
            //childFragmentManager.beginTransaction().replace(R.id.content,fragment).addToBackStack(null).commit()
            findNavController().navigate(R.id.action_powerWaterListFragment_to_powerWaterFragment,bundle)*/
            //userShowDialog(-1,"Add New")
            showBottomSheetDialog(-1,"Add New")
        }
    }

    private fun showAllPaymets() {
        viewModel.getAllPayments()
        viewModel.list.observe(this) {
            if (it.isNotEmpty()){
               binding.textViewNotFound.visibility = View.INVISIBLE
                binding.listpaymentLinear.visibility = View.VISIBLE
                list = it as ArrayList<PowerWaterPayment>
                viewModel.setAdapter(it)
            }else{
               binding.textViewNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemClicked(position: Int) {
       // userShowDialog(position,"Update")
        showBottomSheetDialog(position,"Update")
    }

    @SuppressLint("SetTextI18n")
    private fun userShowDialog(position: Int, msg: String) {
        dialogPaymentBinding = DialogPaymentBinding.inflate(layoutInflater)
        // dialog = Dialog(requireContext())

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val alert : AlertDialog = builder.create()
        alert.setView(dialogPaymentBinding.root)
        alert.setTitle("$msg Payment")
        dialogPaymentBinding.textView10.text = "Bill amount:"
        dialogPaymentBinding.textView13.text = "Paid amount:"
        dialogPaymentBinding.textView14.text = "Due amount:"
        dialogPaymentBinding.rentPaymentAmount.hint = "power & water bill"
        dialogPaymentBinding.paidAmount.hint = "power & water bill paid"
        dialogPaymentBinding.dueAmount.hint = "power & water bill due"

        var rentAmount: Double

        if (position == -1){
            rentAmount = 0.0
            dialogPaymentBinding.rentPaymentAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.dueAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.paidAmount.setText(rentAmount.toString().trim())

            val indexofmonth = Calendar.getInstance().get(Calendar.MONTH)
            dialogPaymentBinding.spinnerMonth.setSelection(indexofmonth)
        }else{
            // list edit
            rentAmount = list[position].powerWaterBill.toDouble()
            dialogPaymentBinding.rentPaymentAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.paidAmount.setText(list[position].powerWaterPaid.trim())
            dialogPaymentBinding.dueAmount.setText(list[position].powerWaterDue.trim())
            dialogPaymentBinding.rentPaymentAmount.isClickable = false
            dialogPaymentBinding.rentPaymentAmount.isEnabled = false
            val array = resources.getStringArray(R.array.month)
            for (i in array.indices){
               // Log.e("array","array: $i")
                if (list[position].month == array[i]){
                    dialogPaymentBinding.spinnerMonth.setSelection(i)
                    dialogPaymentBinding.spinnerMonth.isEnabled = false
                    break
                }
            }
        }
        //due amount text color change
        dialogPaymentBinding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.red
        ))

        // select the month
        dialogPaymentBinding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                month = parent?.getItemAtPosition(position).toString()
                // Log.e("ppppppp","spinner: $month")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    // editText pay bill amount
        dialogPaymentBinding.paidAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var paidAmont = 0.0
                if (s.toString().isNotEmpty()){
                    rentAmount =dialogPaymentBinding.rentPaymentAmount.text.toString().toDouble()
                    paidAmont = s!!.toString().toDouble()
                    val due = rentAmount.minus(paidAmont)
                    dialogPaymentBinding.dueAmount.setText(due.toString())
                    dialogPaymentBinding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.red
                    ))
                }else{
                    val due = rentAmount.minus(paidAmont)
                    dialogPaymentBinding.dueAmount.setText(due.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
      // save the bill
        dialogPaymentBinding.savePayment.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val bb= UserDataBase.getInstance(requireContext()).userDao().isRecordExistsPowerBill(month,year,mobileNumber)
                //Log.e("room","bb: $bb")
                if (position==-1){
                    if (!bb) {
                        UserDataBase.getInstance(requireContext()).userDao().insertPowerWaterPayment(
                            PowerWaterPayment(0, month,year, mobileNumber,
                                dialogPaymentBinding.rentPaymentAmount.text.toString(),
                                dialogPaymentBinding.paidAmount.text.toString(),
                                dialogPaymentBinding.dueAmount.text.toString())
                        )
                        refreshData("Payment save successfully")
                    }else{
                        refreshData("Month already Exists")
                    }

                }else{
                    UserDataBase.getInstance(requireContext()).userDao().powerBillUpdate(
                        PowerWaterPayment(list[position].id, month,year, mobileNumber,
                            dialogPaymentBinding.rentPaymentAmount.text.toString(),
                            dialogPaymentBinding.paidAmount.text.toString(),
                            dialogPaymentBinding.dueAmount.text.toString())
                    )
                    refreshData("Payment updated successfully")
                }

            }
            alert.dismiss()
        }

        alert.show()

    }

    private fun showBottomSheetDialog(position: Int, msg: String){
        dialogPaymentBinding = DialogPaymentBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireActivity(),R.style.BottomSheetDialogStyle)
        dialog.setContentView(dialogPaymentBinding.root)

        dialogPaymentBinding.textView10.text = "Bill amount:"
        dialogPaymentBinding.textView13.text = "Paid amount:"
        dialogPaymentBinding.textView14.text = "Due amount:"
        dialogPaymentBinding.rentPaymentAmount.hint = "power & water bill"
        dialogPaymentBinding.paidAmount.hint = "power & water bill paid"
        dialogPaymentBinding.dueAmount.hint = "power & water bill due"

        var rentAmount: Double

        if (position == -1){
            rentAmount = 0.0
            dialogPaymentBinding.rentPaymentAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.dueAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.paidAmount.setText(rentAmount.toString().trim())

            val indexofmonth = Calendar.getInstance().get(Calendar.MONTH)
            dialogPaymentBinding.spinnerMonth.setSelection(indexofmonth)
        }else{
            // list edit
            rentAmount = list[position].powerWaterBill.toDouble()
            dialogPaymentBinding.rentPaymentAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.paidAmount.setText(list[position].powerWaterPaid.trim())
            dialogPaymentBinding.dueAmount.setText(list[position].powerWaterDue.trim())
          //  dialogPaymentBinding.rentPaymentAmount.isClickable = false
           // dialogPaymentBinding.rentPaymentAmount.isEnabled = false
            val array = resources.getStringArray(R.array.month)
            for (i in array.indices){
                // Log.e("array","array: $i")
                if (list[position].month == array[i]){
                    dialogPaymentBinding.spinnerMonth.setSelection(i)
                 //   dialogPaymentBinding.spinnerMonth.isEnabled = false
                    break
                }
            }
        }
        //due amount text color change
        dialogPaymentBinding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.red
        ))

        // select the month
        dialogPaymentBinding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                month = parent?.getItemAtPosition(position).toString()
                // Log.e("ppppppp","spinner: $month")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        // editText pay bill amount
        dialogPaymentBinding.paidAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var paidAmont = 0.0
                if (s.toString().isNotEmpty()){
                    rentAmount =dialogPaymentBinding.rentPaymentAmount.text.toString().toDouble()
                    paidAmont = s!!.toString().toDouble()
                    val due = rentAmount.minus(paidAmont)
                    dialogPaymentBinding.dueAmount.setText(due.toString())
                    dialogPaymentBinding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.red
                    ))
                }else{
                    val due = rentAmount.minus(paidAmont)
                    dialogPaymentBinding.dueAmount.setText(due.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        // save the bill
        dialogPaymentBinding.savePayment.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val bb= UserDataBase.getInstance(requireContext()).userDao().isRecordExistsPowerBill(month,year,mobileNumber)
                //Log.e("room","bb: $bb")
                if (position==-1){
                    if (!bb) {
                        UserDataBase.getInstance(requireContext()).userDao().insertPowerWaterPayment(
                            PowerWaterPayment(0, month,year, mobileNumber,
                                dialogPaymentBinding.rentPaymentAmount.text.toString(),
                                dialogPaymentBinding.paidAmount.text.toString(),
                                dialogPaymentBinding.dueAmount.text.toString())
                        )
                        refreshData("Payment save successfully")
                    }else{
                        refreshData("Month already Exists")
                    }

                }else{
                    UserDataBase.getInstance(requireContext()).userDao().powerBillUpdate(
                        PowerWaterPayment(list[position].id, month,year, mobileNumber,
                            dialogPaymentBinding.rentPaymentAmount.text.toString(),
                            dialogPaymentBinding.paidAmount.text.toString(),
                            dialogPaymentBinding.dueAmount.text.toString())
                    )
                    refreshData("Payment updated successfully")
                }

            }
            dialog.dismiss()
        }

        dialog.show()

    }
    private fun refreshData(message:String) {
        requireActivity().runOnUiThread {
            showAllPaymets()
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

        }
    }

}
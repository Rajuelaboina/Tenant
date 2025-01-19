package com.tenant.mytenant.ui.rentpayment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.tenant.mytenant.R
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.databinding.DialogPaymentBinding
import com.tenant.mytenant.databinding.FragmentPaymentListBinding
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.OnItemClicked
import com.tenant.mytenant.utils.SwipeLeftDeleteCallback
import com.tenant.mytenant.utils.SwipeToDeleteCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*


@Suppress("DEPRECATION")
class PaymentListFragment : Fragment(), OnItemClicked, PowerWaterListener {
  //  lateinit var firebaseFireStore : FirebaseFirestore
    private var _binding: FragmentPaymentListBinding? = null
    private val  binding get() = _binding!!
    private lateinit var viewModel: PaymentListViewModel
    private lateinit var  userRegistration: UserRegistration
    private lateinit var dialogPaymentBinding: DialogPaymentBinding
    private  var month = ""
    var mobileNumber = ""
    var year=""
    var list = ArrayList<Payment>()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        // Inflate the layout for this fragment
         _binding = FragmentPaymentListBinding.inflate(inflater,container,false)
        viewModel= ViewModelProvider(this)[PaymentListViewModel::class.java]
       // FirebaseApp.initializeApp(requireContext())
       // firebaseFireStore = FirebaseFirestore.getInstance()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // binding.listpaymentLinear.visibility = View.INVISIBLE
        userRegistration = arguments?.getSerializable("OK") as UserRegistration
        mobileNumber = userRegistration.mobileNumber.toString()

        binding.payListViewModel = viewModel
        binding.executePendingBindings()
        showAllPaymets()
        //showBottomSheetDialog(-1,"Add New")
        year =  Calendar.getInstance().get(Calendar.YEAR).toString()
        //Log.e("year","year: $year")
        //binding.textViewMonth.text = "Month & Year"
        binding.fab.setOnClickListener {
           // userShowDialog(-1,"Add New")
            showBottomSheetDialog(-1,"Add New")
            /* val bundle = Bundle()
            bundle.putSerializable("OK",userRegistration)
            findNavController().navigate(R.id.action_paymentListFragment_to_userPaymentFragment2,bundle)*/

        }
        //item clicked
        PaymentListAdapter.setOnItemSelectedListener(this)
        PaymentListAdapter.setOnPowerItemListener(this)
        val swipeHandler = object : SwipeLeftDeleteCallback(requireActivity()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position :Int = viewHolder.adapterPosition
                MaterialAlertDialogBuilder(requireActivity(), R.style.RoundShapeTheme)
                    .setTitle("Do you want Delete!")
                    .setPositiveButton("Yes") { dialog, which ->
                        //SharedPrefManager.getInstance(applicationContext).isLogedout()
                          binding.progressBar2.visibility = View.VISIBLE
                        CoroutineScope(IO).launch {
                            UserDataBase.getInstance(requireActivity()).userDao().deletepayMonth(list[position].month)
                            UserDataBase.getInstance(requireActivity()).userDao().deletePowerMonth(list[position].month)
                        }

                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        //binding.progressBar2.visibility = View.VISIBLE
                        dialog.dismiss()
                        // DisplayAllUsers()
                    }
                    .show()
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    binding.progressBar2.visibility = View.GONE
                    showAllPaymets()

                    // binding.progressBar.visibility = View.GONE
                },2000)

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewPaymentList)
    }



    private fun showAllPaymets() {
        viewModel.getAllPayments(requireContext(),mobileNumber)
        viewModel.list.observe(this) {
            if (it!=null) {
                binding.progressBar2.visibility = View.INVISIBLE
                binding.textViewNotFound.visibility = View.INVISIBLE
              //  binding.listpaymentLinear.visibility = View.VISIBLE
                viewModel.setAdapter(it)
                list = it as ArrayList<Payment>
            }else{
                binding.progressBar2.visibility = View.INVISIBLE
                binding.textViewNotFound.visibility = View.VISIBLE
            }
            //  --  fire store display data ---------------//
           /* val collectionRef = firebaseFireStore.collection("payment").document(mobileNumber).collection(year).whereEqualTo("mobileNumber",mobileNumber)
                .get().addOnSuccessListener {
                      it.forEach {
                         list.add( Payment(0,it.get("month").toString(),it.get("year").toString(),it.get("mobileNumber").toString(),it.get("rentAmount").toString().toDouble(),
                                  it.get("paidAmount").toString().toDouble(),it.get("dueAmount").toString().toDouble()))
                      }
                    viewModel.setAdapter(list)
            }*/
        }

    }

    override fun onItemClicked(position: Int) {
       // Log.e("Payment","position: $position}")
        //userShowDialog(position,"Update")
        showBottomSheetDialog(position,"Update")
       /* val bundle = Bundle()
        bundle.putSerializable("OK",userRegistration)
        bundle.putSerializable("PAYMENT",list[position])
        findNavController().navigate(R.id.action_paymentListFragment_to_segmentFragment,bundle)*/



    }

    /* private fun userShowDialog(position: Int,msg:String) {
        dialogPaymentBinding = DialogPaymentBinding.inflate(layoutInflater)
       // dialog = Dialog(requireContext())

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val alert :AlertDialog = builder.create()
        alert.setView(dialogPaymentBinding.root)
        alert.setTitle("$msg Payment")



        // set background transparent
        // dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // set view customDialog 2 nd way
       // dialog.setContentView(dialogPaymentBinding.root)
        var rentAmount: Double

        if (position == -1){
            rentAmount = userRegistration.rentAmount
            dialogPaymentBinding.rentPaymentAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.dueAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.paidAmount.setText("0.0")

            val indexofmonth = Calendar.getInstance().get(Calendar.MONTH)
            dialogPaymentBinding.spinnerMonth.setSelection(indexofmonth)
        }else{
            rentAmount = list[position].rentAmount
            dialogPaymentBinding.rentPaymentAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.paidAmount.setText(list[position].paidAmount.toString().trim())
            dialogPaymentBinding.dueAmount.setText(list[position].dueAmount.toString().trim())
            dialogPaymentBinding.rentPaymentAmount.isClickable = false
            dialogPaymentBinding.rentPaymentAmount.isEnabled = false
            val array = resources.getStringArray(R.array.month)
            for (i in array.indices){
                Log.e("array","array: $i")
                if (list[position].month == array[i]){
                    dialogPaymentBinding.spinnerMonth.setSelection(i)
                    dialogPaymentBinding.spinnerMonth.isEnabled = false
                    break
                }
            }
        }
        dialogPaymentBinding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.red
        ))

        dialogPaymentBinding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                month = parent?.getItemAtPosition(position).toString()
                // Log.e("ppppppp","spinner: $month")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //edittext
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
        //save btn
        dialogPaymentBinding.savePayment.setOnClickListener {

            CoroutineScope(IO).launch {
                val bb= UserDataBase.getInstance(requireContext()).userDao().isRecordExistsUserId(month,year,mobileNumber)
                //Log.e("room","bb: $bb")
                if (position==-1){
                    if (!bb) {
                        UserDataBase.getInstance(requireContext()).userDao().insertPayment(
                            Payment(0, month,year, mobileNumber,
                                dialogPaymentBinding.rentPaymentAmount.text.toString().toDouble(),
                                dialogPaymentBinding.paidAmount.text.toString().toDouble(),
                                dialogPaymentBinding.dueAmount.text.toString().toDouble())
                        )
                        refreshData("Payment save successfully")
                    }else{
                        refreshData("Month already Exists")
                    }

                }else{
                    UserDataBase.getInstance(requireContext()).userDao().paymentUpdate(
                        Payment(list[position].id, month,year, mobileNumber,
                            dialogPaymentBinding.rentPaymentAmount.text.toString().toDouble(),
                            dialogPaymentBinding.paidAmount.text.toString().toDouble(),
                            dialogPaymentBinding.dueAmount.text.toString().toDouble())
                    )
                    refreshData("Payment updated successfully")
                }
                if (dialogPaymentBinding.dueAmount.text.toString() == "0.0"){
                    userStatusUpdate(false)
                }else{
                   userStatusUpdate(true)
                }
            }
            alert.dismiss()
        }
        alert.show()
    }*/

    private fun showBottomSheetDialog(position: Int,msg:String){
        val alertDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogStyle)
        dialogPaymentBinding = DialogPaymentBinding.inflate(layoutInflater)
        alertDialog.setContentView(dialogPaymentBinding.root)

        var rentAmount: Double

        if (position == -1){
            rentAmount = userRegistration.rentAmount
            dialogPaymentBinding.rentPaymentAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.dueAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.paidAmount.setText("0.0")

            val indexofmonth = Calendar.getInstance().get(Calendar.MONTH)
            dialogPaymentBinding.spinnerMonth.setSelection(indexofmonth)
        }else{
            rentAmount = list[position].rentAmount
            dialogPaymentBinding.rentPaymentAmount.setText(rentAmount.toString().trim())
            dialogPaymentBinding.paidAmount.setText(list[position].paidAmount.toString().trim())
            dialogPaymentBinding.dueAmount.setText(list[position].dueAmount.toString().trim())
            dialogPaymentBinding.rentPaymentAmount.isClickable = false
            dialogPaymentBinding.rentPaymentAmount.isEnabled = false
            val array = resources.getStringArray(R.array.month)
            for (i in array.indices){
                Log.e("array","array: $i")
                if (list[position].month == array[i]){
                    dialogPaymentBinding.spinnerMonth.setSelection(i)
                    dialogPaymentBinding.spinnerMonth.isEnabled = false
                    break
                }
            }
        }
        dialogPaymentBinding.dueAmount.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.red
        ))

        dialogPaymentBinding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                month = parent?.getItemAtPosition(position).toString()
                // Log.e("ppppppp","spinner: $month")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //edittext
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
        //save btn
        dialogPaymentBinding.savePayment.setOnClickListener {
          //  val collectionRef = firebaseFireStore.collection("payment")

            CoroutineScope(IO).launch {
                val bb= UserDataBase.getInstance(requireContext()).userDao().isRecordExistsUserId(month,year,mobileNumber)
                //Log.e("room","bb: $bb")
                if (position==-1){
                    if (!bb) {
                        UserDataBase.getInstance(requireContext()).userDao().insertPayment(
                            Payment(0, month,year, mobileNumber,
                                dialogPaymentBinding.rentPaymentAmount.text.toString().toDouble(),
                                dialogPaymentBinding.paidAmount.text.toString().toDouble(),
                                dialogPaymentBinding.dueAmount.text.toString().toDouble())
                        )
                        refreshData("Payment save successfully")
                    }else{
                        refreshData("Month already Exists")
                    }
                   /* collectionRef.document("users").collection(mobileNumber).document(year).collection(month).add(
                   Payment(0, month,year, mobileNumber,
                        dialogPaymentBinding.rentPaymentAmount.text.toString().toDouble(),
                        dialogPaymentBinding.paidAmount.text.toString().toDouble(),
                        dialogPaymentBinding.dueAmount.text.toString().toDouble())
                    ).addOnSuccessListener {
                        Log.e("fireBase","FireBasedata add success")
                    }*/

                   /* collectionRef.document(mobileNumber).collection(year).add(Payment(0, month,year, mobileNumber,
                        dialogPaymentBinding.rentPaymentAmount.text.toString().toDouble(),
                        dialogPaymentBinding.paidAmount.text.toString().toDouble(),
                        dialogPaymentBinding.dueAmount.text.toString().toDouble())
                    ).addOnSuccessListener {
                        Log.e("fireBase","FireBasedata add success")
                    }*/


                }else{
                    UserDataBase.getInstance(requireContext()).userDao().paymentUpdate(
                        Payment(list[position].id, month,year, mobileNumber,
                            dialogPaymentBinding.rentPaymentAmount.text.toString().toDouble(),
                            dialogPaymentBinding.paidAmount.text.toString().toDouble(),
                            dialogPaymentBinding.dueAmount.text.toString().toDouble())
                    )
                    refreshData("Payment updated successfully")
                }
                if (dialogPaymentBinding.dueAmount.text.toString() == "0.0"){
                    userStatusUpdate(false)
                }else{
                    userStatusUpdate(true)
                }
            }
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun refreshData(message:String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            showAllPaymets()
        }
    }
   // update user status
   private fun userStatusUpdate(boolean: Boolean){
       CoroutineScope(IO).launch {
           UserDataBase.getInstance(requireContext()).userDao().getUpdate(
               UserRegistration(
               userRegistration.userName,
               userRegistration.mobileNumber,
               userRegistration.aadharNumber,
               userRegistration.roomNumber,
               userRegistration.rentAmount,
               userRegistration.joinDate,boolean
           )
           )
       }

    }

    override fun powerItemClicked(payment: Payment) {
        val bundle = Bundle()
        bundle.putSerializable("OK", userRegistration)
        bundle.putString("MONTH", payment.month)
        findNavController().navigate(
            R.id.action_HomeFragment_to_poweWaterListFragment,
            bundle
        )
    }
}
package com.tenant.mytenant.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tenant.mytenant.R
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.databinding.DialogRegistrationBinding
import com.tenant.mytenant.databinding.FragmentHomeBinding
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.onItemClickListener
import com.tenant.mytenant.utils.SwipeToDeleteCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment(), onItemClickListener, /*MenuProvider,*/ FabListener {
   // lateinit var firebaseFireStore : FirebaseFirestore
    //lateinit var date:Date
    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var userList: List<UserRegistration>
    //var list  = ArrayList<UserRegistration>()
    lateinit var registrationBinding :DialogRegistrationBinding
    private var dialog_Date = ""
    private var year = 0
    private var month = 0
    private var day = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View{
         //setHasOptionsMenu(true)
       /* val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)*/
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,HomeViewModelFactory(this))[HomeViewModel::class.java]
      //  FirebaseApp.initializeApp(requireActivity())
       // firebaseFireStore = FirebaseFirestore.getInstance()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


      /*  binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
        binding.viewmodel =viewModel
        binding.executePendingBindings()
        DisplayAllUsers()

        HomeAdapter.setOnItemSelectedListener(this)

        val swipeHandler = object : SwipeToDeleteCallback(requireActivity()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position :Int = viewHolder.adapterPosition
                when(direction){
                    ItemTouchHelper.LEFT->{
                        MaterialAlertDialogBuilder(requireActivity(), R.style.RoundShapeTheme)
                            .setTitle("Do you want Delete!")
                            .setPositiveButton("Yes") { dialog, which ->
                                //SharedPrefManager.getInstance(applicationContext).isLogedout()
                                binding.progressBar.visibility = View.VISIBLE
                                CoroutineScope(IO).launch {
                                    UserDataBase.getInstance(requireActivity()).userDao().deleteUser(userList[position].mobileNumber.toString())
                                    UserDataBase.getInstance(requireActivity()).userDao().deletepayUser(userList[position].mobileNumber.toString())
                                    UserDataBase.getInstance(requireActivity()).userDao().deletePowerBillUser(userList[position].mobileNumber.toString())
                                }

                            }
                            .setNegativeButton("Cancel") { dialog, which ->
                                dialog.dismiss()
                                // DisplayAllUsers()
                            }
                            .show()

                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                            DisplayAllUsers()
                            binding.progressBar.visibility = View.GONE
                        },3000)
                    }
                    ItemTouchHelper.RIGHT->{
                       // edit too data ----------//
                        showDialog(position)
                        DisplayAllUsers()

                    }
                }




            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)





    /* val dateArray: Array<String> = date.split("/")

        val day = dateArray[0].toInt()
        val month = dateArray[1].toInt()
        val year = dateArray[2].toInt()

        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = day
        calendar[Calendar.MONTH] = month
        calendar[Calendar.YEAR] = year


        val time = calendar.timeInMillis

        return Date(time)*/

    }
    // edit data from registration
    private fun showDialog(position: Int) {
        val alertDialog = BottomSheetDialog(requireActivity(),R.style.BottomSheetDialogStyle)
        registrationBinding = DialogRegistrationBinding.inflate(layoutInflater )
        alertDialog.setContentView(registrationBinding.root)

        if (position==-1){

            val c: Calendar = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
            registrationBinding.editTextJoinDate.setText(day.toString() + "-" + (month + 1) + "-" + year)

             setSpanale("Tenant Registration !",0,6,21)

        }else{
            setSpanale("Tenant Details Update !",0,6,23)
            registrationBinding.editTextTextPersonName.isEnabled = false
            registrationBinding.editTextMobileNumber.isEnabled = false
            registrationBinding.editTextAadharNumber.isEnabled = false
            registrationBinding.editTextJoinDate.isEnabled = false

            registrationBinding.editTextTextPersonName.setText( userList[position].userName)
            registrationBinding.editTextMobileNumber.setText( userList[position].mobileNumber.toString())
            registrationBinding.editTextAadharNumber.setText( userList[position].aadharNumber)
            registrationBinding.editTextRentRoomNumber.setText( userList[position].roomNumber)
            registrationBinding.editTextRentAmount.setText( userList[position].rentAmount.toString())
            registrationBinding.editTextJoinDate.setText( userList[position].joinDate)
            dialog_Date = userList[position].joinDate
        }


        registrationBinding.editTextJoinDate.setOnClickListener {

            showdateDialog(position)
        }
        registrationBinding.buttonSave.setOnClickListener {
            if (position==-1){
                dataInsert()
            }else{
               dataUpdate()

            }

            alertDialog.dismiss()
            DisplayAllUsers()
        }

        alertDialog.show()

    }

    private fun setSpanale(string: String, sp1: Int, sp2: Int, sp3: Int) {
        val spannable =SpannableString(string)
        spannable.setSpan(UnderlineSpan(),0,spannable.length,0)
        val foregroundSpan = ForegroundColorSpan(ContextCompat.getColor(requireActivity(),R.color.purple_700))
        spannable.setSpan(foregroundSpan,sp1, sp2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val foregroundSpan2 = ForegroundColorSpan(ContextCompat.getColor(requireActivity(),R.color.red))
        spannable.setSpan(foregroundSpan2, sp2, sp3,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        registrationBinding.textView.text = spannable
    }

    // register data insert in database
   private fun dataInsert(){
        CoroutineScope(IO).launch {
            UserDataBase.getInstance(requireActivity()).userDao().insertUser(
                UserRegistration(
                    registrationBinding.editTextTextPersonName.text.toString().trim(),
                    registrationBinding.editTextMobileNumber.text.toString().trim().toLong(),
                    registrationBinding.editTextAadharNumber.text.toString().trim(),
                    registrationBinding.editTextRentRoomNumber.text.toString().trim(),
                    registrationBinding.editTextRentAmount.text.toString().trim().toDouble(),
                    registrationBinding.editTextJoinDate.text.toString(), false
                )
            )
        }
   }
    // register data update in database
    private fun dataUpdate(){
        CoroutineScope(IO).launch {
            UserDataBase.getInstance(requireActivity()).userDao().getUpdate(
                UserRegistration(
                    registrationBinding.editTextTextPersonName.text.toString().trim(),
                    registrationBinding.editTextMobileNumber.text.toString().trim().toLong(),
                    registrationBinding.editTextAadharNumber.text.toString().trim(),
                    registrationBinding.editTextRentRoomNumber.text.toString().trim(),
                    registrationBinding.editTextRentAmount.text.toString().trim().toDouble(),
                    dialog_Date, false
                )
            )
        }
    }
    private fun showdateDialog(position: Int) {

        /*val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)*/
        if (position != -1) {
            val items1: List<String> = userList[position].joinDate.split("-")
            day = items1[0].toInt()
            month = items1[1].toInt()
            year = items1[2].toInt()
        }/*else{
            val c: Calendar = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)

            registrationBinding.editTextJoinDate.setText(day.toString() + "-" + (month + 1) + "-" + year)
        }*/

        val datePickerDialog = DatePickerDialog( // on below line we are passing context.
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth -> // on below line we are setting date to our edit text.
                // binding.editTextJoinDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                var cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat("dd-MM-yyyy")
                dialog_Date = sdf.format(cal.time)
                registrationBinding.editTextJoinDate.setText(dialog_Date)
                // Log.e("picker","$date")
            },  // on below line we are passing year,
            // month and day for selected date in our date picker.
            year, month, day
        )
        // at last we are calling show to
        // display our date picker dialog.
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show()
    }

    private fun DisplayAllUsers() {

        viewModel.getAllUsers(requireActivity())
        viewModel.list.observe(this) { it ->
            // binding.recyclerView
            binding.progressBar.visibility = View.GONE
            if (it.isNotEmpty()) {
                userList = it
                viewModel.setAdapter(it)
            }
      //  --------- fire base store display the data ----------------//
            /*val collectionRef = firebaseFireStore.collection("registration")
            collectionRef.get().addOnSuccessListener {
                it.documents.forEach {
                   list.add(UserRegistration(it.get("userName").toString(),it.get("mobileNumber").toString(),it.get("aadharNumber").toString(),it.get("roomNumber").toString(),
                        it.get("rentAmount").toString().toDouble(),it.get("joinDate").toString(),it.get("status").toString().toBoolean())
                    )
                }
                viewModel.setAdapter(list)
            }*/

          /* it.forEach {
               val items1: Array<String> = it.split("-")
               val year = items1[0].toInt()
               val month = items1[1].toInt()
               val day = items1[2].toInt()
           }*/
           // -----------------//


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
       // _binding = null
    }

    override fun onItemClicked(userRegistration: UserRegistration) {
        val bundle = Bundle()
        bundle.putSerializable("OK",userRegistration)
        // bundle.putDouble("AMOUNT",userRegistration.rentAmount)
        // bundle.putString("MOBILE",userRegistration.mobileNumber)
        findNavController().navigate(R.id.action_HomeFragment_to_userPaymentFragment2,bundle)

        //dialog of payment and power payment
        /*MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
            .setTitle("Do you want add Payment")
            .setNegativeButton("Rent payment", DialogInterface.OnClickListener { dialog, which ->
                val bundle = Bundle()
                bundle.putSerializable("OK",userRegistration)
                // bundle.putDouble("AMOUNT",userRegistration.rentAmount)
                // bundle.putString("MOBILE",userRegistration.mobileNumber)
                findNavController().navigate(R.id.action_HomeFragment_to_userPaymentFragment2,bundle)
            })
            .setPositiveButton("Bill payment") { dialog, which ->
                val bundle = Bundle()
                bundle.putSerializable("OK", userRegistration)
                findNavController().navigate(
                    R.id.action_HomeFragment_to_poweWaterListFragment,
                    bundle
                )
            }
            .create()
            .show()*/

    }

    override fun onItemBillClicked(userRegistration: UserRegistration) {
       /* val bundle = Bundle()
        bundle.putSerializable("OK", userRegistration)
        findNavController().navigate(
            R.id.action_HomeFragment_to_poweWaterListFragment,
            bundle
        )*/
    }


   /* override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_settings -> {}
            R.id.action_addUser -> {
                //findNavController().navigate(R.id.action_HomeFragment_to_RegistrationFragment)
                showDialog(-1)
            }

        }
        return true
    }*/

    override fun fabClicked() {
        showDialog(-1)
    }


}
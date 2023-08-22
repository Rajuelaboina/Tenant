package com.tenant.mytenant.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tenant.mytenant.MyBroadcastReceiver
import com.tenant.mytenant.R
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.databinding.FragmentHomeBinding
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.onItemClickListener
import com.tenant.mytenant.utils.SwipeToDeleteCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment(), onItemClickListener, MenuProvider {
    lateinit var date:Date
    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var userList: List<UserRegistration>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View{
         //setHasOptionsMenu(true)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
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

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position :Int = viewHolder.adapterPosition
                MaterialAlertDialogBuilder(requireContext(), R.style.RoundShapeTheme)
                    .setTitle("Do you want Delete!")
                    .setPositiveButton("Yes") { dialog, which ->
                        //SharedPrefManager.getInstance(applicationContext).isLogedout()
                      CoroutineScope(IO).launch {
                          UserDataBase.getInstance(requireContext()).userDao().deleteUser(userList[position].mobileNumber)
                          UserDataBase.getInstance(requireContext()).userDao().deletepayUser(userList[position].mobileNumber)
                      }
                        DisplayAllUsers()
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                        DisplayAllUsers()
                    }
                    .show()

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

    private fun DisplayAllUsers() {
        viewModel.getAllUsers(requireContext())
        viewModel.list.observe(this) {
            // binding.recyclerView
            binding.progressBar.visibility = View.GONE
            if (it != null) {
                userList = it
                viewModel.setAdapter(it)
            }
          /* it.forEach {
               val items1: Array<String> = it.split("-")
               val year = items1[0].toInt()
               val month = items1[1].toInt()
               val day = items1[2].toInt()
           }*/
            val cal = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            val smpl = SimpleDateFormat("dd-M-yyyy")
            val smpl2 = SimpleDateFormat("dd-M-yyyy")
            var inActiveDate: Date? = null
            var date1 :Date
            var date2 :Date
            for (i in it.indices){
                val items1: List<String> = it[i].joinDate.split("-")
                val day = items1[0].toInt()
                val month = items1[1].toInt()
                val year = items1[2].toInt()
               /* Log.e("split date",""+year)
                Log.e("split date",""+month)
                Log.e("split date",""+day)*/
                cal.set(Calendar.YEAR,year)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.DAY_OF_MONTH,day)
                date1 = cal.time
               // Log.e("Db Date>>>>>>>","DB Date: $date1")
                date2 =cal2.time
               // Log.e("Db Date>>>>>>>","DB Date: $date2")
               // Log.e("Current>>>>>>>","current Date: ${smpl.format(date2)}")


               /* if (date1.before(date2)){
                    Log.e("before>>>>>>>","before")
                }else if (date1.after(date2)){
                    Log.e("after>>>>>>>","after")
                }else{
                    Log.e("same>>>>>>>","same")
                }*/

            }

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
        /*MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
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
        val bundle = Bundle()
        bundle.putSerializable("OK", userRegistration)
        findNavController().navigate(
            R.id.action_HomeFragment_to_poweWaterListFragment,
            bundle
        )
    }

    /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         super.onCreateOptionsMenu(menu, inflater)
         inflater.inflate(R.menu.menu_main, menu)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
             R.id.action_settings -> {}
             R.id.action_addUser -> {
                 findNavController().navigate(R.id.action_HomeFragment_to_RegistrationFragment)
             }

         }
         return super.onOptionsItemSelected(item)
     }*/

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_settings -> {}
            R.id.action_addUser -> {
                findNavController().navigate(R.id.action_HomeFragment_to_RegistrationFragment)
            }

        }
        return true
    }
}
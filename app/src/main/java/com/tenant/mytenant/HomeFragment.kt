package com.tenant.mytenant

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tenant.mytenant.adapter.HomeAdapter
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.databinding.FragmentHomeBinding
import com.tenant.mytenant.model.UserRegistration
import com.tenant.mytenant.userlistener.onItemClickListener
import com.tenant.mytenant.viewModel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment(), onItemClickListener, MenuProvider {

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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
       // _binding = null
    }

    override fun onItemClicked(userRegistration: UserRegistration) {
       // Log.e("item clicked","onItemclicked: ${userRegistration.userName}")
       /* val bundle = Bundle()
        bundle.putSerializable("OK",userRegistration)
       // bundle.putDouble("AMOUNT",userRegistration.rentAmount)
       // bundle.putString("MOBILE",userRegistration.mobileNumber)
        findNavController().navigate(R.id.action_HomeFragment_to_userPaymentFragment2,bundle)*/

        //dialog of payment and power payment
        MaterialAlertDialogBuilder(requireContext(),R.style.AlertDialogTheme)
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
            .show()
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
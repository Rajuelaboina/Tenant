package com.tenant.mytenant.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.tenant.mytenant.R
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.databinding.FragmentRegistrationBinding
import com.tenant.mytenant.userlistener.RegistrationListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegistrationFragment : Fragment(), RegistrationListener {

    private var _binding: FragmentRegistrationBinding? = null
    private lateinit var viewModel: RegistrationViewModel
   // lateinit var firebaseFireStore : FirebaseFirestore


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var date = ""
    var year = 0
    var month = 0
    var day = 0
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.viewModel = ViewModelProvider(this, RegistrationViewModelFactory(this))[RegistrationViewModel::class.java]
       // FirebaseApp.initializeApp(requireContext())
       // firebaseFireStore = FirebaseFirestore.getInstance()
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c: Calendar = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
        binding.editTextJoinDate.setText(day.toString() + "-" + (month + 1) + "-" + year)

        /*  binding.buttonSecond.setOnClickListener {
              findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
          }*/
       /* binding.editTextAadharNumber.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("booblean","BBBBB: "+  formatCard(s.toString()))
            }
            override fun afterTextChanged(s: Editable?) {
               // Log.e("booblean","BBBBB: "+ isValidVisaCardNo(s.toString()))
            }
        }) */
    }

    private fun formatCard(cardNumber: String):String {
        if (cardNumber == null)
            return null.toString()
        var delimiter = ' '
        return cardNumber.replaceFirst("\\d{4}", "$0 ").replaceFirst("\\d{6}", "$0 ")
    }


    override fun onDestroyView() {
        super.onDestroyView()
       // _binding = null
    }

    override fun onSuccess(code: Int) {
           //val collectionRef = firebaseFireStore.collection("registration")
            CoroutineScope(IO).launch {
              UserDataBase.getInstance(requireContext()).userDao().insertUser(
                  UserRegistration(binding.editTextTextPersonName.text.toString().trim(),
                  binding.editTextMobileNumber.text.toString().trim().toLong(),
                  binding.editTextAadharNumber.text.toString().trim(),
                  binding.editTextRentRoomNumber.text.toString().trim(),
                  binding.editTextRentAmount.text.toString().trim().toDouble(),
                  date,false)
              )
       //------- fire base store add the data ------------- //
             /* collectionRef.add(UserRegistration(binding.editTextTextPersonName.text.toString().trim(),
                  binding.editTextMobileNumber.text.toString().trim(),
                  binding.editTextAadharNumber.text.toString().trim(),
                  binding.editTextRentRoomNumber.text.toString().trim(),
                  binding.editTextRentAmount.text.toString().trim().toDouble(),
                  date,false)).addOnSuccessListener {
                     Log.e("fireBase","FireBasedata add success")
              }*/
                /*collectionRef.document("users").collection(binding.editTextMobileNumber.text.toString().trim()).add(
                UserRegistration(binding.editTextTextPersonName.text.toString().trim(),
                    binding.editTextMobileNumber.text.toString().trim(),
                    binding.editTextAadharNumber.text.toString().trim(),
                    binding.editTextRentRoomNumber.text.toString().trim(),
                    binding.editTextRentAmount.text.toString().trim().toDouble(),
                    date,false)).addOnSuccessListener {
                    Log.e("fireBase","FireBasedata add success")

                }*/
                /*collectionRef.document(binding.editTextMobileNumber.text.toString().trim()).set(
                    UserRegistration(binding.editTextTextPersonName.text.toString().trim(),
                        binding.editTextMobileNumber.text.toString().trim(),
                        binding.editTextAadharNumber.text.toString().trim(),
                        binding.editTextRentRoomNumber.text.toString().trim(),
                        binding.editTextRentAmount.text.toString().trim().toDouble(),
                        date,false)
                )*/

                // val user= UserDataBase.getInstance(requireContext()).userDao().getAllUsers()
               // Log.e("data",""+user.size)
            }
        Toast.makeText(requireContext(),"Registration successfully: ",Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_SecondFragment_to_HomeFragment)
    }

    override fun onError(code: Int) {
        Toast.makeText(requireContext(),"Fields are not empty",Toast.LENGTH_LONG).show()
    }

    override fun onDate() {
        //val c: Calendar = Calendar.getInstance()

        // on below line we are getting
        // our day, month and year.

        // on below line we are getting
        // our day, month and year.
      /*  val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)*/
        // on below line we are creating a variable for date picker dialog.

        // on below line we are creating a variable for date picker dialog.
        val datePickerDialog = DatePickerDialog( // on below line we are passing context.
            requireContext(),
            { view, year, monthOfYear, dayOfMonth -> // on below line we are setting date to our edit text.
               // binding.editTextJoinDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                var cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat("dd-MM-yyyy")
                date = sdf.format(cal.time)
                binding.editTextJoinDate.setText(date)
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
}
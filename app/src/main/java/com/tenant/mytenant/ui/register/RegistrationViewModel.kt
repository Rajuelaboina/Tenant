package com.tenant.mytenant.ui.register

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModel
import com.tenant.mytenant.userlistener.RegistrationListener

class RegistrationViewModel(var listener: RegistrationListener): ViewModel() {
   val userRegistration = UserRegistration("",0,"","",0.0,"",false)
  val userNameTextWatcher: TextWatcher get() = object :TextWatcher{
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          userRegistration.userName = s.toString().trim()
      }

      override fun afterTextChanged(s: Editable?) {

      }

  }
  val mobileNumberTextWatcher: TextWatcher get() = object : TextWatcher{
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          userRegistration.mobileNumber = s.toString().trim().toLong()
      }

      override fun afterTextChanged(s: Editable?) {

      }

  }
    val aadharNumberTextWatcher: TextWatcher get() = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            userRegistration.aadharNumber = s.toString().trim()

        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
    val roomNumberTextWatcher: TextWatcher get() = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            userRegistration.roomNumber = s.toString().trim()
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
    val rentAmountTextWatcher: TextWatcher get() = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            userRegistration.rentAmount = s.toString().trim().toDouble()
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
    val joiningDateTextWatcher: TextWatcher get() = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            userRegistration.joinDate = s.toString().trim()
            //listener.onDate()
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
    fun editOnClick(view: View){

        listener.onDate()
    }

   @SuppressLint("SuspiciousIndentation")
   fun onClicked(view:View){
     var code =userRegistration.isValidUser()
       if (code == 1){
           listener.onError(code)
       }else{
           listener.onSuccess(code)
       }
   }
}
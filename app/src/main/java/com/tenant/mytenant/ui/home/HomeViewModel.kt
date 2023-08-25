package com.tenant.mytenant.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.RegistrationListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
     val list = MutableLiveData<List<UserRegistration>>()
     private var adapter = HomeAdapter()
    fun getAllUsers(context: Context){
        CoroutineScope(IO).launch {
           list.postValue( UserDataBase.getInstance(context).userDao().getAllUsers())
        }

    }
    fun setAdapter(userList: List<UserRegistration>) {
           adapter.updateList(userList)
    }
    fun getHomeAdapter(): HomeAdapter {
      return adapter
    }

    fun setOnClick(view: View){
       Log.e("Fab","Fab><<<<<")
    }

}
package com.tenant.mytenant.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tenant.mytenant.adapter.PaymentListAdapter
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.model.Payment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PaymentListViewModel:ViewModel() {
     val list = MutableLiveData<List<Payment>>()
     private var adapter = PaymentListAdapter()

    fun getAllPayments(context: Context, mobileNumber: String){
        CoroutineScope(IO).launch {
           list.postValue( UserDataBase.getInstance(context).userDao().getUserAllPayments(mobileNumber))
        }
    }
    fun setAdapter(userList: List<Payment>) {
           adapter.updateList(userList)
    }
    fun getAdapter():PaymentListAdapter{
      return adapter
    }
}
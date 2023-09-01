package com.tenant.mytenant.ui.rentpayment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.ui.billpayment.PowerWaterPayment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PaymentListViewModel:ViewModel() {
     val list = MutableLiveData<List<Payment>>()
    val powerList = MutableLiveData<List<PowerWaterPayment>>()
     private var adapter = PaymentListAdapter()

    fun getAllPayments(context: Context, mobileNumber: String){
        CoroutineScope(IO).launch {
           list.postValue( UserDataBase.getInstance(context).userDao().getUserAllPayments(mobileNumber))
        }
    }

    fun getAllPowerPayments(context: Context, mobileNumber: String,month: String, year: String) {
        CoroutineScope(IO).launch {
            powerList.postValue(UserDataBase.getInstance(context).userDao().getUserAllPowerPayments(month,year,mobileNumber))
        }
    }

    fun setAdapter(userList: List<Payment>) {
           adapter.updateList(userList)
    }
    fun getAdapter(): PaymentListAdapter {
      return adapter
    }
}
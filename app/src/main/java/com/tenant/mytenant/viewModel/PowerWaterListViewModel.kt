package com.tenant.mytenant.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tenant.mytenant.adapter.PowerWaterAdapter
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.model.PowerWaterPayment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PowerWaterListViewModel(var context: Context, var mobileNumber: String) : ViewModel() {
    val list = MutableLiveData<List<PowerWaterPayment>>()
   var adapter = PowerWaterAdapter()
    fun getAllPayments(){
      CoroutineScope(IO).launch {
        list.postValue(UserDataBase.getInstance(context).userDao().getUserAllPowerPayments(mobileNumber))
      }
    }

    fun setAdapter(it: List<PowerWaterPayment>) {
         adapter.upDateList(it)
    }
    fun PowerAdapter():PowerWaterAdapter{
        return adapter
    }

}
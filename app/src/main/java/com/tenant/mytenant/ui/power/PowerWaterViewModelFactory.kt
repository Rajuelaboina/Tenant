package com.tenant.mytenant.ui.power

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tenant.mytenant.userlistener.powerBillSelectedListener

class PowerWaterViewModelFactory(
    var listener: powerBillSelectedListener,
    var context: Context,
    var mobileNumber: String
):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PowerWaterViewModel(listener,context,mobileNumber) as T
    }
}
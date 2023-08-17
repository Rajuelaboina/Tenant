package com.tenant.mytenant.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PowerWaterListViewModelFactory(
    var context: Context,
    var mobileNumber: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PowerWaterListViewModel(context,mobileNumber) as T
    }
}
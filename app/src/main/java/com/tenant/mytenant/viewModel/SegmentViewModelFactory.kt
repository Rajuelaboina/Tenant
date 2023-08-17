package com.tenant.mytenant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.tenant.mytenant.userlistener.powerBillSelectedListener

class SegmentViewModelFactory(var listener:powerBillSelectedListener):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return SegmentViewModel(listener) as T
    }
}
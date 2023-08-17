package com.tenant.mytenant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.tenant.mytenant.model.UserRegistration
import com.tenant.mytenant.userlistener.RegistrationListener

class RegistrationViewModelFactory(var listener:RegistrationListener):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return RegistrationViewModel(listener) as T
    }
}
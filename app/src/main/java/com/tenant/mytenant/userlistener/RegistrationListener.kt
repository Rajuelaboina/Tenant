package com.tenant.mytenant.userlistener

interface RegistrationListener {
        fun onSuccess(code:Int)
        fun onError(code: Int)
        fun onDate()
}
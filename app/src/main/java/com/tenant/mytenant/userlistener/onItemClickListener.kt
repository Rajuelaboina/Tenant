package com.tenant.mytenant.userlistener

import com.tenant.mytenant.ui.register.UserRegistration

interface onItemClickListener {
    fun onItemClicked(userRegistration: UserRegistration)
}
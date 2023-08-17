package com.tenant.mytenant.userlistener

import com.tenant.mytenant.model.UserRegistration

interface onItemClickListener {
    fun onItemClicked(userRegistration: UserRegistration)
}
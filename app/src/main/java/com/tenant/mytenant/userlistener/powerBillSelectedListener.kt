package com.tenant.mytenant.userlistener

interface powerBillSelectedListener {
    
    fun onPaidListener(string: Double)
    fun successListener()
    fun errorListener()
}
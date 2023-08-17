package com.tenant.mytenant.viewModel

import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.lifecycle.ViewModel
import com.tenant.mytenant.userlistener.powerBillSelectedListener

class SegmentViewModel(var listener: powerBillSelectedListener) : ViewModel() {

    val segmentSelection: OnCheckedChangeListener get() = object : OnCheckedChangeListener{
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
           Log.e("sdafdsfsdf","dsfsdfsdfsdfsdf")
        }

    }
    fun segmentSelection(view:View){
        Log.e("sdafdsfsdf","dsfsdfsdfsdfsdf")
    }
}
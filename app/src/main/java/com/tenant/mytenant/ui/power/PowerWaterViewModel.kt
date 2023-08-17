package com.tenant.mytenant.ui.power

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import com.tenant.mytenant.R
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.ui.billpayment.PowerWaterPayment
import com.tenant.mytenant.userlistener.powerBillSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

class PowerWaterViewModel(
    var listener: powerBillSelectedListener,
    var context: Context,
    var mobileNumber: String
): ViewModel() {

     var totalAmount = 0.0
     var paidAmount = 0.0
     var  dueAmount = 0.0
     var month = ""
     var dd =""
     var year =  Calendar.getInstance().get(Calendar.YEAR).toString()
     var str1 =""
     var str2 =""

    /*val spinnerMonth : OnItemSelected get() = object : AdapterView.OnItemSelectedListener,
        OnItemSelected {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
           // month = parent?.getItemAtPosition(position).toString()

            listener.onButtonSelected( parent?.getItemAtPosition(position).toString())
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //TODO("Not yet implemented")
        }

    }
    fun setIndex():Int{
        return  Calendar.getInstance().get(Calendar.MONTH)
    }*/
    fun getadapter(): ArrayAdapter<String> {
        val array =context.resources.getStringArray(R.array.month)
        return ArrayAdapter(context,R.layout.dropdownitem,array)
    }
   /* val spinnerMonth2 : AdapterView.OnItemClickListener
        get() = AdapterView.OnItemClickListener { parent, view, position, id ->
            listener.onButtonSelected(parent.getItemAtPosition(position).toString())
    }*/

    val monthNameTextWatcher: TextWatcher
        get() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //listener.onButtonSelected(s.toString())
            month = s.toString()
        }
        override fun afterTextChanged(s: Editable?) {
        }
    }
    //paid amount
    val totalAmountTextWatcher: TextWatcher get() = object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            str1 =s.toString()
            if (s.toString().isNotEmpty()) {
                totalAmount = s.toString().toDouble()
            }else{
                listener.onPaidListener(0.0)
            }
        }
        override fun afterTextChanged(s: Editable?) {
        }
    }
    val paidTextWatcher: TextWatcher get() = object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            str2 =s.toString()
            if (s.toString().isNotEmpty()) {
                paidAmount = s.toString().toDouble()
                dueAmount = totalAmount!!.minus(paidAmount)
                listener.onPaidListener(dueAmount)
            }else{
                listener.onPaidListener(totalAmount)

            }
        }
        override fun afterTextChanged(s: Editable?) {
        }
   }
    fun setMonthName():String{
       val array =context.resources.getStringArray(R.array.month)
       val indexofmonth = Calendar.getInstance().get(Calendar.MONTH)
       var str=""
       for (i in array.indices){
           if (i==indexofmonth)
           {
             str =array[i]
           }
       }
       return str
   }
    fun saveOnclick(view:View){
        if (str1.isNotEmpty() && str2.isNotEmpty() && month.isNotEmpty() ){
                 CoroutineScope(IO).launch {
                     val bb= UserDataBase.getInstance(context).userDao().isRecordExistsPowerBill(month,year,mobileNumber)
                     if (!bb) {
                         UserDataBase.getInstance(context).userDao().insertPowerWaterPayment(
                             PowerWaterPayment(0, month,year,mobileNumber,totalAmount.toString(),
                                 paidAmount.toString(),dueAmount.toString())
                         )
                         listener.successListener()
                     }
                 }

        }else{
            listener.errorListener()
       }
    }
}
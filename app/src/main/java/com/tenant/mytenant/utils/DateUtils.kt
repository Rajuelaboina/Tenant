package com.tenant.mytenant.utils

import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tenant.mytenant.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    var output: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
   companion object{

       fun  getDate(str: String ): String{
           var parser: SimpleDateFormat
           var date: String? = null
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
               //  Log.i("DATE><<<<<<<<", "" + list.get(position).getCreated_at());
               try {
                   val parsed: Date = parser.parse(str)
                   val input = SimpleDateFormat("dd-MM-YYYY")
                   date = input.format(parsed)
               } catch (e: ParseException) {
                   e.printStackTrace()
               }
           }
           return date!!

       }
       // Display from Time //
       fun  getCurrentTime(str: String): String{
           val output = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
           var parser: SimpleDateFormat
           var time: String? = null
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
               try {
                   val parsed = parser.parse(str)
                   val input = SimpleDateFormat("HH:mm:ss")
                   time = input.format(parsed)
                   //String formatted = output.format(parsed);
                   // Log.i("DATE", "" + formatted);
                   // Log.i("DATE", "" + time);
               } catch (e: ParseException) {
                   e.printStackTrace()
               }
           }
           return time!!

       }
       fun  getDateTime(str: String): String{

           var parser: SimpleDateFormat
           var date: String? = null
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
               //  Log.i("DATE><<<<<<<<", "" + list.get(position).getCreated_at());
               try {
                   val parsed = parser.parse(str)
                   val input = SimpleDateFormat("MMM-dd-YYYY HH:mm:ss")
                   date = input.format(parsed)
               } catch (e: ParseException) {
                   e.printStackTrace()
               }
           }
           return date!!

       }
       fun  getDateTime2(str: String): String{

           var parser: SimpleDateFormat
           var date: String? = null
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
               //  Log.i("DATE><<<<<<<<", "" + list.get(position).getCreated_at());
               try {
                   val parsed = parser.parse(str)
                   val input = SimpleDateFormat("dd-MM-YYYY HH:mm:ss")
                   date = input.format(parsed)
               } catch (e: ParseException) {
                   e.printStackTrace()
               }
           }
           return date!!

       }

       fun getMonthName(str:String): String{
           var date: String? = null
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               try {
                   val items1: List<String>  = str.split("-")
                   val day = items1[0].toInt()
                   val month = items1[1].toInt()
                   val year = items1[2].toInt()

                   val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR,year)
                      calendar.set(Calendar.MONTH,month)
                     calendar.set(Calendar.DAY_OF_MONTH,day)
                   val date1 = calendar.time
                   val input = SimpleDateFormat("dd-MMM-YYYY")
                   date = input.format(date1)
               } catch (e: ParseException) {
                   e.printStackTrace()
               }
           }
           return date!!
       }
       fun setSpannable(activity: FragmentActivity, string: String, sp1: Int, sp2: Int, sp3: Int): SpannableString {
           val spannable = SpannableString(string)
           spannable.setSpan(UnderlineSpan(),0,spannable.length,0)
           val foregroundSpan = ForegroundColorSpan(ContextCompat.getColor(activity, R.color.purple_700))
           spannable.setSpan(foregroundSpan,sp1, sp2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
           val foregroundSpan2 = ForegroundColorSpan(ContextCompat.getColor(activity, R.color.red))
           spannable.setSpan(foregroundSpan2, sp2, sp3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
           return spannable
       }
   }


}
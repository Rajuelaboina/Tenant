package com.tenant.mytenant.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tenant.mytenant.MyBroadcastReceiver
import com.tenant.mytenant.R
import com.tenant.mytenant.databinding.HomeItemBinding
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.onItemClickListener
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
      var list = ArrayList<UserRegistration>()
      lateinit var context:Context
    fun updateList(userList: List<UserRegistration>) {
       this.list = userList as ArrayList<UserRegistration>
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        context =parent.context
      return HomeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.home_item,parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
           holder.bind(list[position])
        //Log.e("bind>>>>>>>","bind: "+list[position].userName+": "+ position)
        holder.binding.rentButton.setOnClickListener {
            myListener.onItemClicked(list[position])
        }
        holder.binding.billButton.setOnClickListener {
            myListener.onItemBillClicked(list[position])
        }

        val items1: List<String>  = list[position].joinDate.split("-")
        val day = items1[0].toInt()
        //Log.e("db_day>>>>>>>","db_day: $day")
       // val month = items1[1].toInt()
       // val year = items1[2].toInt()
        /*var day2 = 0
        if (5 < day){
             day2 = day-5
        }else{
            day2 = 1
        }*/

        val smpl = SimpleDateFormat("dd-M-yyyy")
        val cal2 = Calendar.getInstance()
        val date2 =cal2.time
        val current_date =smpl.format(date2)
        //Log.e("current_date>>>>>>>","current_date: $current_day")
        val items2: List<String>  = current_date.split("-")
        val day_current = items2[0].toInt()
        val day_month = items2[1].toInt()
        //Log.e("current_day>>>>>>>","current_day: $day_current")
       /* if (day_current == day) {
           // Log.e("current_day>>>>>>>","<>Equal><<<< $position")
            // status update
           *//* CoroutineScope(Dispatchers.IO).launch {
                UserDataBase.getInstance(context).userDao().getUpdate(
                    UserRegistration(
                        list[position].userName,
                        list[position].mobileNumber,
                        list[position].aadharNumber,
                        list[position].roomNumber,
                        list[position].rentAmount,
                        list[position].joinDate,true
                    )
                )
            }*//*
        }*/
        if (smpl.parse(list[position].joinDate).before(smpl.parse(current_date))){
            Log.e("before","before")
        }else if (smpl.parse(list[position].joinDate).after(smpl.parse(current_date))){
            Log.e("else if ","else if ")
            setAlarm(list[position].joinDate)
        }else{
            Log.e("celse","else")

        }

        if (list[position].status){
            holder.binding.cardView1.setBackgroundColor(ContextCompat.getColor(context,R.color.background))

        }

    }

    private fun setAlarm(joinDate: String) {

        val items1: List<String>  = joinDate.split("-")
        val day = items1[0].toInt()
        val month = items1[1].toInt()
         val year = items1[2].toInt()

        val calendar = Calendar.getInstance()
       //  calendar.set(Calendar.YEAR,year)
      //   calendar.set(Calendar.MONTH,month)
      //  calendar.set(Calendar.DAY_OF_MONTH,day)
        calendar.set(Calendar.HOUR_OF_DAY,7)
        calendar.set(Calendar.MINUTE,30)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.AM_PM, Calendar.PM)

        val intent = Intent(context, MyBroadcastReceiver::class.java)
        val id = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var alarmMgr: AlarmManager? = null
        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
        if (alarmMgr != null) {
            // alarmMgr.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + (i * 1000), pendingIntent)
           // alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,AlarmManager.INTERVAL_DAY,calendar.getTimeInMillis(), pendingIntent)
           // alarmMgr.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent)
            //alarmMgr.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent)
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)

        }


       // Toast.makeText(applicationContext, "Alarm set in $i seconds", Toast.LENGTH_LONG).show()
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class HomeViewHolder(var binding: HomeItemBinding) : ViewHolder(binding.root){

         fun bind(userRegistration: UserRegistration) {
           binding.model = userRegistration
         }
    }
   companion object{
       lateinit var myListener: onItemClickListener
       fun setOnItemSelectedListener(listener : onItemClickListener){
          myListener =listener
       }
   }
}
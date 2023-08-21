package com.tenant.mytenant.ui.home

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tenant.mytenant.R
import com.tenant.mytenant.database.UserDataBase
import com.tenant.mytenant.databinding.HomeItemBinding
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.onItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
        Log.e("bind>>>>>>>","bind: "+list[position].userName+": "+ position)
        holder.itemView.setOnClickListener {
            myListener.onItemClicked(list[position])
        }

        val items1: List<String>  = list[position].joinDate.split("-")
        val day = items1[0].toInt()
        //Log.e("db_day>>>>>>>","db_day: $day")
        val month = items1[1].toInt()
       // val year = items1[2].toInt()
        var day2 = 0
        if (5 < day){
             day2 = day-5
        }else{
            day2 = 1
        }

        val smpl = SimpleDateFormat("dd-M-yyyy")
        val cal2 = Calendar.getInstance()
        val date2 =cal2.time
        val current_date =smpl.format(date2)
        //Log.e("current_date>>>>>>>","current_date: $current_day")
        val items2: List<String>  = current_date.split("-")
        val day_current = items2[0].toInt()
        val day_month = items2[1].toInt()
        //Log.e("current_day>>>>>>>","current_day: $day_current")
        if (day_current == day2 ) {
            Log.e("current_day>>>>>>>","<>Equal><<<< $position")
            // status update
           /* CoroutineScope(Dispatchers.IO).launch {
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
            }*/
        }

        if (list[position].status){
            holder.binding.cardView1.setBackgroundColor(ContextCompat.getColor(context,R.color.background))

        }
       /* var date = Date()
        //Log.e("CurrentDate","date: $date")
        val smpl = SimpleDateFormat("dd-M-yyyy")
        var dd= smpl.format(date)

        Log.e("CurrentDate","t: $dd")
        var dbDate = list[position].joinDate
        Log.e("dbDate","dbDate: $dbDate")

        val formatter = DateTimeFormatter.ofPattern("dd-M-yyyy")

        val firstDate: LocalDate = LocalDate.parse(dd, formatter)
        val secondDate: LocalDate = LocalDate.parse(dbDate, formatter)

        when {
            firstDate.isAfter(secondDate) -> {
                Log.e("%s is after %s", "$dd, $dbDate")

            }
            firstDate.isBefore(secondDate) -> {
                Log.e("%s is before %s"," $dd, $dbDate")
            }
            firstDate.isEqual(secondDate) -> {
                Log.e("Both dates are equal","Both dates are equal")
            }
        }*/
        /*  var dd = smpl.parse(date.toString())
          Log.e("CurrentDate","date: $dd")

         var db_date = smpl.parse(list[position].joinDate)
          Log.e("CurrentDate2 ","db_date :"+db_date)
          if (dd.after(db_date)) {
              Log.e("CurrentDate","date: after")
          }else if (dd.before(db_date)){
              Log.e("CurrentDate","date: before")
          }else{
              Log.e("CurrentDate","date: ")
          }*/

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
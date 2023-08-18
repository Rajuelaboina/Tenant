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
import com.tenant.mytenant.databinding.HomeItemBinding
import com.tenant.mytenant.ui.register.UserRegistration
import com.tenant.mytenant.userlistener.onItemClickListener
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
        holder.itemView.setOnClickListener {
            myListener.onItemClicked(list[position])
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
package com.tenant.mytenant.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tenant.mytenant.R
import com.tenant.mytenant.databinding.HomeItemBinding
import com.tenant.mytenant.model.UserRegistration
import com.tenant.mytenant.userlistener.onItemClickListener

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

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
           holder.bind(list[position])
        holder.itemView.setOnClickListener {
            myListener.onItemClicked(list[position])
        }
        if (list[position].status){
           holder.binding.cardView1.setBackgroundColor(context.resources.getColor(R.color.background))
        }
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
          this.myListener=listener
       }
   }
}
package com.tenant.mytenant.ui.billpayment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tenant.mytenant.R
import com.tenant.mytenant.databinding.PowerItemBinding
import com.tenant.mytenant.userlistener.OnItemClicked

class PowerWaterAdapter: RecyclerView.Adapter<PowerWaterAdapter.MyViewHolder>() {
    var list = ArrayList<PowerWaterPayment>()
    fun upDateList(it: List<PowerWaterPayment>) {
        this.list = it as ArrayList<PowerWaterPayment>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.power_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
        if (list[position].powerWaterDue.toDouble() != 0.0){
            holder.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }
        }
    }

    class MyViewHolder(var binding: PowerItemBinding) : RecyclerView.ViewHolder(binding.root){
         fun bind(payment: PowerWaterPayment) {
            binding.model = payment
         }
    }
    companion object{
        lateinit var listener: OnItemClicked
        fun setOnItemSelected(listener:OnItemClicked){
            Companion.listener = listener
        }
    }
}
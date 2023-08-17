package com.tenant.mytenant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tenant.mytenant.R
import com.tenant.mytenant.databinding.PaymentlistItemBinding
import com.tenant.mytenant.model.Payment
import com.tenant.mytenant.userlistener.OnItemClicked

class PaymentListAdapter : RecyclerView.Adapter<PaymentListAdapter.MyViewHolder>() {
    var list = ArrayList<Payment>()
    lateinit var context: Context
    fun updateList(userList: List<Payment>) {
        this.list = userList as ArrayList<Payment>
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context =parent.context
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.paymentlist_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
        if (list[position].dueAmount != 0.0){
             holder.itemView.setOnClickListener {
                    myListener.onItemClicked(position)
             }
        }
       /* holder.itemView.setOnClickListener {
            myListener.onItemClicked(list[position])
        }*/

    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(var binding: PaymentlistItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(payment: Payment) {
            binding.model = payment
        }
    }
    companion object{
        lateinit var myListener: OnItemClicked
        fun setOnItemSelectedListener(listener : OnItemClicked){
            this.myListener=listener
        }
    }
}
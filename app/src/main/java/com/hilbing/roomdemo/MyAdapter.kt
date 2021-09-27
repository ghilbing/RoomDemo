package com.hilbing.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hilbing.roomdemo.databinding.ListItemBinding
import com.hilbing.roomdemo.db.Subscriber

class MyAdapter (
    private val subscribers: List<Subscriber>,
    private val clickListener:(Subscriber)->Unit)
    : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscribers[position], clickListener)
    }

    override fun getItemCount(): Int {
        return subscribers.size
    }

}

class MyViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(subscriber: Subscriber, clickListener:(Subscriber)->Unit){
        binding.nameTV.text = subscriber.name
        binding.emailTV.text = subscriber.email
        binding.listItemLL.setOnClickListener {
            clickListener(subscriber)
        }
    }
}
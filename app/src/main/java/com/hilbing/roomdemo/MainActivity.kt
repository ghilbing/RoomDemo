package com.hilbing.roomdemo

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hilbing.roomdemo.databinding.ActivityMainBinding
import com.hilbing.roomdemo.db.Subscriber
import com.hilbing.roomdemo.db.SubscriberDatabase
import com.hilbing.roomdemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this,factory).get(SubscriberViewModel::class.java)
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        initRecyclerView()
        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        })
    }
    private fun displaySubscribersList(){
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("MyTag", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })

    }

    private fun initRecyclerView(){
        binding.subscriberRV.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter({selectedItem:Subscriber->listItemClicked(selectedItem)})
        binding.subscriberRV.adapter = adapter
        displaySubscribersList()
    }

    private fun listItemClicked(subscriber: Subscriber){
       // Toast.makeText(this, "selected is ${subscriber.name}", Toast.LENGTH_SHORT).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}
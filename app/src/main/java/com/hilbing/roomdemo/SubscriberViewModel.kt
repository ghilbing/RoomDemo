package com.hilbing.roomdemo

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilbing.roomdemo.db.Subscriber
import com.hilbing.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel(), Observable{

    val subscribers = repository.subsribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber

    @Bindable
    val inputName = MutableLiveData<String?>()
    @Bindable
    val inputEmail = MutableLiveData<String?>()
    @Bindable
    val saveOrUpdateBT = MutableLiveData<String>()
    @Bindable
    val clearAllOrDeleteBT = MutableLiveData<String>()

    init {
        saveOrUpdateBT.value = "Save"
        clearAllOrDeleteBT.value = "Clear all"
    }

    fun saveOrUpdate(){
        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(Subscriber(0,name, email))
        inputName.value = null
        inputEmail.value = null
    }

    fun clearOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }else {
            clearAll()
        }

    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateBT.value = "Update"
        clearAllOrDeleteBT.value = "Delete"


    }

    fun insert(subscriber: Subscriber): Job = viewModelScope.launch{
        repository.insert(subscriber)
    }
    fun update(subscriber: Subscriber): Job = viewModelScope.launch {
        repository.update(subscriber)
    }
    fun delete(subscriber: Subscriber): Job = viewModelScope.launch {
        repository.delete(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateBT.value = "Save"
        clearAllOrDeleteBT.value = "Clear all"
    }
    fun clearAll(): Job = viewModelScope.launch {
        repository.deleteAll()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}
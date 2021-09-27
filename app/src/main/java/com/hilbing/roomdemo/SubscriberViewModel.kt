package com.hilbing.roomdemo

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
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

    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
    get() = statusMessage

    init {
        saveOrUpdateBT.value = "Save"
        clearAllOrDeleteBT.value = "Clear all"
    }

    fun saveOrUpdate(){
        if(isUpdateOrDelete){
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            update(subscriberToUpdateOrDelete)
        } else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscriber(0,name, email))
            inputName.value = null
            inputEmail.value = null
        }

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
        val newRowId: Long = repository.insert(subscriber)
        if(newRowId > -1) {
            statusMessage.value = Event("Subscriber added successfully $newRowId")
        } else {
            statusMessage.value = Event("Error occurred")
        }
    }
    fun update(subscriber: Subscriber): Job = viewModelScope.launch {
        val noOfRows: Int = repository.update(subscriber)
        if(noOfRows > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateBT.value = "Save"
            clearAllOrDeleteBT.value = "Clear All"
            statusMessage.value = Event("$noOfRows updated successfully")
        }
         else {
            statusMessage.value = Event("Error occurred")
        }
    }
    fun delete(subscriber: Subscriber): Job = viewModelScope.launch {
        val noOfRowsDeleted: Int = repository.delete(subscriber)
        if(noOfRowsDeleted > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateBT.value = "Save"
            clearAllOrDeleteBT.value = "Clear all"
            statusMessage.value = Event("Subscriber deleted successfully")
        } else {
            statusMessage.value = Event("Error occurred")
        }
    }
    fun clearAll(): Job = viewModelScope.launch {
        val rowsDeleted: Int = repository.deleteAll()
        if(rowsDeleted > 0) {
            statusMessage.value = Event("All subscribers deleted successfully")
        } else {
            statusMessage.value = Event("Error occurred")
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}
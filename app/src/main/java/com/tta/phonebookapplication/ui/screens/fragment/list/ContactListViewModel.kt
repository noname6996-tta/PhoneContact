package com.tta.phonebookapplication.ui.screens.fragment.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tta.phonebookapplication.data.model.Contact
import com.tta.phonebookapplication.data.repository.ContactRepository
import com.tta.phonebookapplication.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {
    private val listContacts = MutableLiveData<State<List<Contact>>>()
    private val error = MutableLiveData<State<String>>()

    val getListContactsResult: LiveData<State<List<Contact>>> = listContacts
    val getErrorResult: LiveData<State<String>> = error

    fun getListContacts(): Job = viewModelScope.launch {
        listContacts.postValue(State.Loading)
        runCatching {
            withContext(Dispatchers.IO) {
                repository.getAllList()
            }
        }
            .onSuccess {
                listContacts.postValue(
                    State.Success(it)
                )
            }
            .onFailure {
                listContacts.postValue(
                    State.Failure(it)
                )
            }
    }

    fun importContactsFromJson(context: Context, fileName: Int): Job = viewModelScope.launch {
        listContacts.postValue(State.Loading)
        var count = 0
        runCatching {
            withContext(Dispatchers.IO) {
                val userList: JSONArray =
                    context.resources.openRawResource(fileName).bufferedReader().use {
                        JSONArray(it.readText())
                    }

                userList.takeIf { it.length() > 0 }?.let { list ->
                    for (index in 0 until list.length()) {
                        val userObj = list.getJSONObject(index)
                        val contact = Contact(
                            null,
                            name = userObj.getString("name"),
                            email = userObj.getString("email"),
                            phone = userObj.getString("phone")
                        )
//                      Remove duplicate contacts from new data when adding to db
                        if (repository.doesContactExist(contact.email, contact.phone) == 0) {
                            repository.insertContact(
                                contact
                            )
                        } else {
                            count ++
                        }
                    }
                }
            }
        }.onSuccess {
            getListContacts()
            if (count >0){
                error.postValue(State.Success("Not insert $count duplicate item"))
            }
            Timber.tag("error").e("delete $count duplicate item")
        }.onFailure { throwable ->
            Timber.tag("error").e(throwable.message.toString())
        }
    }

    fun deleteAllData(): Job = viewModelScope.launch {
        listContacts.postValue(State.Loading)
        runCatching {
            withContext(Dispatchers.IO) {
                repository.deleteAll()
                repository.resetAutoIncrement()
            }
        }
            .onSuccess {
                getListContacts()
            }
            .onFailure { throwable ->
                Timber.tag("error_delete").e(throwable.message.toString())
            }
    }

    fun insertListData(list: List<Contact>): Job = viewModelScope.launch {
        listContacts.postValue(State.Loading)
        var count = 0
        runCatching {
            withContext(Dispatchers.IO) {
//              Remove duplicate contacts from new data when adding to db
                for (contact in list) {
                    if (repository.doesContactExist(contact.email, contact.phone) == 0) {
                        repository.insertContact(
                            contact
                        )
                    } else {
                        count ++
                    }
                }
//              just add
//              repository.insertListContact(list)
            }
        }
            .onSuccess {
                getListContacts()
                if (count > 0){
                    error.postValue(State.Success("Not insert $count duplicate item"))
                }
                Timber.tag("error").e("delete $count duplicate item")
            }
            .onFailure { throwable ->
                Timber.tag("error_insert").e(throwable.message.toString())
            }
    }

    fun deleteContact(id: Int): Job = viewModelScope.launch {
        listContacts.postValue(State.Loading)
        runCatching {
            withContext(Dispatchers.IO) {
                repository.deleteContactById(id)
            }
        }
            .onSuccess {
                getListContacts()
            }
            .onFailure { throwable ->
                Timber.tag("error_delete").e(throwable.message.toString())
            }
    }
}
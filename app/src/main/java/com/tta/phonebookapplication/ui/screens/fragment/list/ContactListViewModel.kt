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

    val getListContactsResult: LiveData<State<List<Contact>>> = listContacts

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
        runCatching {
            withContext(Dispatchers.IO) {
                val userList: JSONArray =
                    context.resources.openRawResource(fileName).bufferedReader().use {
                        JSONArray(it.readText())
                    }

                userList.takeIf { it.length() > 0 }?.let { list ->
                    for (index in 0 until list.length()) {
                        val userObj = list.getJSONObject(index)
                        repository.insertContact(
                            Contact(
                                null,
                                name = userObj.getString("name"),
                                email = userObj.getString("email"),
                                phone = userObj.getString("phone")
                            )
                        )

                    }
                }
            }
        }.onSuccess {
            getListContacts()
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
        runCatching {
            withContext(Dispatchers.IO) {
                repository.insertListContact(list)
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
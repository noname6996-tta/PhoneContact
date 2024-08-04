package com.tta.phonebookapplication.ui.screens.fragment.add

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
import javax.inject.Inject

@HiltViewModel
class AddListViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {
    private val addContact = MutableLiveData<State<Unit>>()

    val getAddContactResult: LiveData<State<Unit>> = addContact

    fun addContact(contact: Contact): Job = viewModelScope.launch {
        addContact.postValue(State.Loading)
        val result = runCatching {
            withContext(Dispatchers.IO) {
                if (repository.doesContactExist(contact.email, contact.phone) > 0) {
                    throw IllegalStateException("Contact already exists.")
                }
                repository.insertContact(contact)
            }
        }

        addContact.postValue(
            result.fold(
                onSuccess = { State.Success(it) },
                onFailure = { State.Failure(it) }
            )
        )
    }
}
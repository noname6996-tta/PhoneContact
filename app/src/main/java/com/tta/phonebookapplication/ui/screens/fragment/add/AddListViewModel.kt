package com.tta.phonebookapplication.ui.screens.fragment.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tta.phonebookapplication.domain.entity.ContactEntity
import com.tta.phonebookapplication.domain.repository.ContactRepository
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

    fun addContact(contactEntity: ContactEntity): Job = viewModelScope.launch {
        addContact.postValue(State.Loading)
        val result = runCatching {
            withContext(Dispatchers.IO) {
                if (repository.doesContactExist(contactEntity.email, contactEntity.phone) > 0) {
                    throw IllegalStateException("Contact already exists.")
                }
                repository.insertContact(contactEntity)
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
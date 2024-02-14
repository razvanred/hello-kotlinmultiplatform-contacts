package red.razvan.hello.kotlinmultiplatform.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import red.razvan.hello.kotlinmultiplatform.contacts.repository.Contact
import red.razvan.hello.kotlinmultiplatform.contacts.repository.ContactId
import red.razvan.hello.kotlinmultiplatform.contacts.repository.ContactsRepository

class ContactActivityViewModel(
    private val id: ContactId,
    private val repository: ContactsRepository
): ViewModel() {

    private val mutableContact = MutableStateFlow<Contact?>(null)
    val contact = mutableContact.asStateFlow()

    init {
        viewModelScope.launch {
            refreshData()
        }
    }

    fun editContact(contactName: String) {
        viewModelScope.launch {
            val contact = contact.first()?.copy(name = contactName) ?: return@launch
            repository.update(contact)
            refreshData()
        }
    }

    private suspend fun refreshData() {
        mutableContact.emit(repository.getById(id))
    }

    fun deleteContact() {
        viewModelScope.launch {
            repository.removeById(id = id)
        }
    }
}
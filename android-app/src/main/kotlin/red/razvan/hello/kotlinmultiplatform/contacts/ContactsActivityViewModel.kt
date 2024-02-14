package red.razvan.hello.kotlinmultiplatform.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import red.razvan.hello.kotlinmultiplatform.contacts.repository.Contact
import red.razvan.hello.kotlinmultiplatform.contacts.repository.ContactsRepository
import red.razvan.hello.kotlinmultiplatform.contacts.repository.NewContact

class ContactsActivityViewModel(
    private val repository: ContactsRepository,
) : ViewModel() {

    private val mutableContacts = MutableStateFlow(emptyMap<Char, List<Contact>>())
    val contacts = mutableContacts.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            mutableContacts.emit(repository.getAll())
        }
    }

    fun createNewContact(contactName: String) {
        val newContact = NewContact(contactName)
        repository.add(newContact)
        refreshData()
    }
}
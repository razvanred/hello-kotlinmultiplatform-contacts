package red.razvan.hello.kotlinmultiplatform.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import red.razvan.hello.kotlinmultiplatform.contacts.repository.Contact
import red.razvan.hello.kotlinmultiplatform.contacts.repository.ContactsRepository
import red.razvan.hello.kotlinmultiplatform.contacts.repository.ContactsSection
import red.razvan.hello.kotlinmultiplatform.contacts.repository.NewContact

class ContactsActivityViewModel(
    private val repository: ContactsRepository,
) : ViewModel() {

    private val mutableSections = MutableStateFlow(emptyList<ContactsSection>())
    val sections = mutableSections.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            mutableSections.emit(repository.getAll())
        }
    }

    fun createNewContact(contactName: String) {
        val newContact = NewContact(contactName)
        repository.add(newContact)
        refreshData()
    }
}
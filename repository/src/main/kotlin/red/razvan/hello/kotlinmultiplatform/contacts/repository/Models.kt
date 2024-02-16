package red.razvan.hello.kotlinmultiplatform.contacts.repository

import java.util.UUID

data class ContactId(
    val value: String = UUID.randomUUID().toString()
)

data class Contact(
    val id: ContactId = ContactId(),
    val name: String
)

data class ContactsSection(
    val initial: Char,
    val contacts: List<Contact>
)

data class NewContact(val name: String)
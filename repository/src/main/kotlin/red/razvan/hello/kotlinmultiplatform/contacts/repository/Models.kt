package red.razvan.hello.kotlinmultiplatform.contacts.repository

import java.util.UUID

@JvmInline
value class ContactId(val value: String = UUID.randomUUID().toString())

data class Contact(
    val id: ContactId = ContactId(),
    val name: String
)

data class NewContact(val name: String)
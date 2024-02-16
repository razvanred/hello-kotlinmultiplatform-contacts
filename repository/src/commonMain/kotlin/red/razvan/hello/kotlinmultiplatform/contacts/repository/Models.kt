package red.razvan.hello.kotlinmultiplatform.contacts.repository

// If you want to use the https://github.com/benasher44/uuid library
// import com.benasher44.uuid.uuid4

data class ContactId(
    val value: String = uuid()
    // If you want to use the https://github.com/benasher44/uuid library
    // val value: String = uuid4().toString()
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

// Or simply use the https://github.com/benasher44/uuid library
internal expect fun uuid(): String
package red.razvan.hello.kotlinmultiplatform.contacts.repository

import java.util.UUID

internal actual fun uuid(): String = UUID.randomUUID().toString()
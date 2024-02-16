package red.razvan.hello.kotlinmultiplatform.contacts.repository

import platform.Foundation.NSUUID

internal actual fun uuid(): String =
    NSUUID().UUIDString
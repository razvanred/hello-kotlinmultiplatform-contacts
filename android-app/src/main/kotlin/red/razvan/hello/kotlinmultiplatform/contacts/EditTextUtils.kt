package red.razvan.hello.kotlinmultiplatform.contacts

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

// Taken from https://github.com/nextcloud/android/blob/839c205eea39212827ef5d59f6008340d648df09/app/src/main/java/com/owncloud/android/utils/KeyboardUtils.kt#L32
fun EditText.showKeyboardOnDialog() {
    requestFocus()
    // needs 100ms delay to account for focus animations
    postDelayed({
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, ShowInputDelayMillis)
}

private const val ShowInputDelayMillis = 1000L
package red.razvan.hello.kotlinmultiplatform.contacts

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.parcelize.Parcelize
import red.razvan.hello.kotlinmultiplatform.contacts.databinding.EditContactDialogContentBinding

class EditContactDialogFragment : DialogFragment() {

    private var _binding: EditContactDialogContentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var mode: Mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments()
                .getParcelable(BundleKeys.MODE, Mode::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments()
                .getParcelable(BundleKeys.MODE)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(mode.dialogTitle)
            .setView(
                EditContactDialogContentBinding.inflate(layoutInflater)
                    .apply {
                        _binding = this
                        with(editText) {
                            (mode as? Mode.Edit)?.let {
                                editText.setText(it.contactName)
                            }

                            setOnEditorActionListener { _, actionId, _ ->
                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                    handleEditClick()
                                    // If we are in full screen mode, due to the fact that the ExtractEditText does not support showing errors,
                                    //  do also the default implementation, which consists in hiding the keyboard.
                                    return@setOnEditorActionListener !requireInputMethodManager()
                                        .isFullscreenMode
                                }
                                return@setOnEditorActionListener false
                            }
                            addTextChangedListener {
                                textInputLayout.error = null
                            }
                        }
                    }
                    .root
            )
            // The listener is set later
            .setPositiveButton(mode.positiveButtonText, null)
            .setNegativeButton(getString(android.R.string.cancel), null)
            .create()
            .apply {
                // Due to the fact that the alert dialog buttons dismiss the dialog when pressed, to avoid
                //  such behavior on the confirm button this approach seems to be the work-around, in
                //  order to display correctly the error message on the text field.
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        handleEditClick()
                    }
                }
            }

    override fun onResume() {
        super.onResume()
        binding.editText.showKeyboardOnDialog()
    }

    private fun handleEditClick() {
        val name = binding.editText.text?.trim()

        if (name.isNullOrBlank()) {
            binding.textInputLayout.error = getString(R.string.field_required_error_message)
        } else {
            (requireContext() as ActivityCallbacks).onContactDialogFlowCompleted(contactName = name.toString())
            requireDialog().dismiss()
        }
    }

    companion object {
        private fun Fragment.requireInputMethodManager(): InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        fun newInstance(mode: Mode): EditContactDialogFragment =
            EditContactDialogFragment().apply {
                arguments = bundleOf(BundleKeys.MODE to mode)
            }
    }

    private object BundleKeys {
        const val MODE = "mode"
    }

    sealed interface Mode : Parcelable {
        @Parcelize
        data object Create : Mode

        @Parcelize
        data class Edit(val contactName: String): Mode
    }

    interface ActivityCallbacks {
        fun onContactDialogFlowCompleted(contactName: String)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val Mode.dialogTitle: String
        get() = when(this) {
            Mode.Create -> getString(R.string.edit_contact_dialog_create_title)
            is Mode.Edit -> getString(R.string.edit_contact_dialog_edit_title)
        }

    private val Mode.positiveButtonText: String
        get() = when (this) {
            Mode.Create -> getString(R.string.edit_contact_dialog_create_button)
            is Mode.Edit -> getString(R.string.edit_button)
        }
}
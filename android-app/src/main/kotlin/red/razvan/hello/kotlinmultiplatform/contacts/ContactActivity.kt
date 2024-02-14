package red.razvan.hello.kotlinmultiplatform.contacts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import red.razvan.hello.kotlinmultiplatform.contacts.databinding.ContactActivityBinding
import red.razvan.hello.kotlinmultiplatform.contacts.repository.ContactId

class ContactActivity : AppCompatActivity(), EditContactDialogFragment.ActivityCallbacks {

    private val id: ContactId by lazy {
        ContactId(value = intent.getStringExtra(IntentKeys.ID)!!)
    }

    private val viewModel: ContactActivityViewModel by viewModel {
        parametersOf(id)
    }

    private lateinit var binding: ContactActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ContactActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            deleteButton.setOnClickListener {
                showDeleteContactDialog()
            }
            editButton.setOnClickListener {
                showEditContactDialog()
            }
            toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .contact
                    .collect { contact ->
                        binding.toolbar.title = contact?.name
                    }
            }
        }
    }

    private fun showDeleteContactDialog() {
        val name = viewModel.contact.value?.name ?: return

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_contact_dialog_title, name))
            .setMessage(getString(R.string.delete_contact_dialog_message))
            .setIcon(R.drawable.ic_delete_24)
            .setPositiveButton(getString(R.string.delete_button)) { dialog, _ ->
                viewModel.deleteContact()
                dialog.dismiss()
                finish()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditContactDialog() {
        val name = viewModel.contact.value?.name ?: return

        EditContactDialogFragment
            .newInstance(mode = EditContactDialogFragment.Mode.Edit(contactName = name))
            .show(supportFragmentManager, "edit-contact")
    }

    private object IntentKeys {
        const val ID = "id"
    }

    companion object {
        fun newIntent(context: Context, id: ContactId): Intent =
            Intent(context, ContactActivity::class.java)
                .putExtra(IntentKeys.ID, id.value)
    }

    override fun onContactDialogFlowCompleted(contactName: String) {
        viewModel.editContact(contactName = contactName)
        // The toolbar refresh is not working, so we restart the activity
        startActivity(newIntent(context = this, id = id))
        finish()
    }
}
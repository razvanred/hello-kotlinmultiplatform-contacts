package red.razvan.hello.kotlinmultiplatform.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import red.razvan.hello.kotlinmultiplatform.contacts.databinding.ContactListItemBinding
import red.razvan.hello.kotlinmultiplatform.contacts.databinding.ContactsActivityBinding
import red.razvan.hello.kotlinmultiplatform.contacts.repository.Contact

class ContactsActivity : AppCompatActivity(), EditContactDialogFragment.ActivityCallbacks {

    private val viewModel: ContactsActivityViewModel by viewModel()

    private lateinit var binding: ContactsActivityBinding

    private val fabDefaultMarginsPixelSize: Int
        get() = resources.getDimensionPixelSize(R.dimen.fab_default_margins)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ContactsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ContactListItemsAdapter {
            startActivity(
                ContactActivity.newIntent(context = this@ContactsActivity, id = it.id)
            )
        }

        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(
                MaterialDividerItemDecoration(
                    this@ContactsActivity,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )

            ViewCompat.setOnApplyWindowInsetsListener(addContactButton) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = insets.bottom + fabDefaultMarginsPixelSize
                    rightMargin = insets.right + fabDefaultMarginsPixelSize
                }
                windowInsets
            }

            addContactButton.setOnClickListener {
                EditContactDialogFragment
                    .newInstance(EditContactDialogFragment.Mode.Create)
                    .show(supportFragmentManager, "create-contact")
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .sections
                    .collect { sections ->
                        adapter.submitList(
                            sections
                                .map { (letter, contacts) ->
                                    listOf(ContactListItem.Header(letter = letter))
                                        .plus(
                                            contacts.map { contact ->
                                                ContactListItem.Element(contact)
                                            }
                                        )
                                }
                                .flatten()
                        )
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }

    private sealed interface ContactListItem {
        data class Header(val letter: Char) : ContactListItem

        data class Element(val contact: Contact) : ContactListItem
    }

    private class ContactListItemsAdapter(
        private val onContactClickListener: (Contact) -> Unit
    ) :
        ListAdapter<ContactListItem, ContactListItemsAdapter.ViewHolder>(
            ContactListItemDiffCallback
        ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                binding = ContactListItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(getItem(position), onContactClickListener)
        }

        class ViewHolder(
            private val binding: ContactListItemBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: ContactListItem, onContactClickListener: (Contact) -> Unit) {
                with(binding) {
                    when (item) {
                        is ContactListItem.Element -> {
                            contactNameTextView.visibility = View.VISIBLE
                            headerLetterTextView.visibility = View.GONE
                            contactNameTextView.text = item.contact.name
                            contactNameTextView.setOnClickListener {
                                onContactClickListener(item.contact)
                            }
                        }

                        is ContactListItem.Header -> {
                            contactNameTextView.visibility = View.GONE
                            headerLetterTextView.visibility = View.VISIBLE
                            headerLetterTextView.text = item.letter.toString()
                        }
                    }
                }
            }
        }
    }

    private object ContactListItemDiffCallback : DiffUtil.ItemCallback<ContactListItem>() {
        override fun areContentsTheSame(
            oldItem: ContactListItem,
            newItem: ContactListItem
        ): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: ContactListItem, newItem: ContactListItem): Boolean =
            when {
                oldItem is ContactListItem.Header && newItem is ContactListItem.Header -> {
                    oldItem.letter == newItem.letter
                }

                oldItem is ContactListItem.Element && newItem is ContactListItem.Element -> {
                    oldItem.contact.id == newItem.contact.id
                }
                // the elements are not of the same type, so why bother
                else -> false
            }
    }

    override fun onContactDialogFlowCompleted(contactName: String) {
        viewModel.createNewContact(contactName)
    }
}
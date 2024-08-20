package com.tta.phonebookapplication.ui.screens.fragment.list

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tta.phonebookapplication.R
import com.tta.phonebookapplication.domain.entity.ContactEntity
import com.tta.phonebookapplication.databinding.FragmentContactListBinding
import com.tta.phonebookapplication.ui.component.adapter.ContactAdapter
import com.tta.phonebookapplication.ui.screens.base.BaseFragment
import com.tta.phonebookapplication.ui.screens.fragment.add.AddListFragment
import com.tta.phonebookapplication.utils.State
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStreamReader

@AndroidEntryPoint
class ContactListFragment : BaseFragment<FragmentContactListBinding>() {
    override var isBackKeyActive: Boolean = false
    private val viewModel: ContactListViewModel by viewModels()
    private var contactAdapter = ContactAdapter()
    private var list = ArrayList<ContactEntity>()
    private lateinit var pickJsonFileLauncher: ActivityResultLauncher<Intent>

    override fun getDataBinding(): FragmentContactListBinding {
        return FragmentContactListBinding.inflate(layoutInflater)
    }

    override fun addEvent() = with(binding) {
        super.addEvent()
        icMoreOptions.setOnClickListener {
            showPopupMenu(it)
        }
        icAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddListFragment())
                .addToBackStack(null)
                .commit()
        }
        contactAdapter.deleteItem {
            showError("Delete", "Do you want delete this item") {
                viewModel.deleteContact(list[it].id!!)
            }
        }
    }

    override fun initView() {
        super.initView()
        pickJsonFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    readJsonFile(uri)
                }
            }
        }
        binding.rcvContact.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ContactAdapter().also {
                contactAdapter = it
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.getListContacts()
    }

    override fun addObservers() {
        super.addObservers()
        viewModel.getListContactsResult.distinctUntilChanged()
            .observe(viewLifecycleOwner) { result ->
                getListContactResult(result)
            }

        viewModel.getErrorResult.distinctUntilChanged().observe(viewLifecycleOwner) {
            getErrorResult(it)
        }
    }

    private fun getErrorResult(result: State<String>) {
        when (result) {
            is State.Success -> {
                if (result.hasBeenHandled) {
                    return
                }
                result.hasBeenHandled = true
                showError("Insert", result.data) {}
            }

            is State.Failure -> {}
            State.Loading -> {}
        }
    }

    private fun getListContactResult(result: State<List<ContactEntity>>) {
        when (result) {
            is State.Loading -> {
                startLoading()
            }

            is State.Success -> {
                finishLoading()
                list.clear()
                list.addAll(result.data)
                contactAdapter.setListContact(list, requireContext())
                binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                binding.tvNumberContacts.text =
                    "${getString(R.string.number_of_phone_numbers)} : ${contactAdapter.itemCount}"
            }

            is State.Failure -> {
                finishLoading()
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_options, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.import_small_data -> {
                    viewModel.importContactsFromJson(requireContext(), R.raw.contact_data_small)
                    true
                }

                R.id.import_medium_data -> {
                    viewModel.importContactsFromJson(requireContext(), R.raw.contact_data_medium)
                    true
                }

                R.id.import_big_data -> {
                    viewModel.importContactsFromJson(requireContext(), R.raw.contact_data_big)
                    true
                }

                R.id.delete_all_data -> {
                    viewModel.deleteAllData()
                    true
                }

                R.id.import_system -> {
                    openJsonFilePicker()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    private fun openJsonFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/json"
        }
        pickJsonFileLauncher.launch(intent)
    }

    private fun readJsonFile(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val reader = InputStreamReader(inputStream)
            val contactEntityListType = object : TypeToken<List<ContactEntity>>() {}.type
            val contactEntities: List<ContactEntity> = Gson().fromJson(reader, contactEntityListType)

            viewModel.insertListData(contactEntities)
        } catch (e: JsonSyntaxException) {
            showError("Error", "File không đúng định dạng JSON Danh bạ") {}
        } catch (e: Exception) {
            showError("Error", "Đã xảy ra lỗi khi xử lý file") {}
        }
    }

    private fun showError(tittle: String, message: String, onOkClick: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(tittle)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onOkClick()
            }
            .show()
    }
}
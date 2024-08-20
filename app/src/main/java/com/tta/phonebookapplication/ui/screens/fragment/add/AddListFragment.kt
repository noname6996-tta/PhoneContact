package com.tta.phonebookapplication.ui.screens.fragment.add

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.tta.phonebookapplication.R
import com.tta.phonebookapplication.domain.entity.ContactEntity
import com.tta.phonebookapplication.databinding.FragmentAddContactBinding
import com.tta.phonebookapplication.ui.screens.base.BaseFragment
import com.tta.phonebookapplication.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddListFragment : BaseFragment<FragmentAddContactBinding>() {
    override var isBackKeyActive: Boolean = true
    private val viewModel: AddListViewModel by viewModels()

    override fun getDataBinding(): FragmentAddContactBinding {
        return FragmentAddContactBinding.inflate(layoutInflater)
    }

    override fun addEvent() = with(binding) {
        super.addEvent()
        imgBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        tvDone.setOnClickListener {
            hideKeyboard(it)
            if (edtName.text.trim().toString().isEmpty()
                || edtPhone.text.trim().toString().isEmpty()
                || edtEmail.text.trim().toString().isEmpty()
            ) {
                tvError.text = getString(R.string.please_enter_complete_information)
            } else {
                val name = edtName.text.trim().toString()
                val email = edtEmail.text.trim().toString()
                val phone = edtPhone.text.trim().toString()

                val contactEntity = ContactEntity(id = null, name = name, email = email, phone = phone)
                viewModel.addContact(contactEntity)
            }
        }
    }

    override fun addObservers() {
        super.addObservers()
        viewModel.getAddContactResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is State.Loading -> {
                    startLoading()
                }

                is State.Success -> {
                    finishLoading()
                    AlertDialog.Builder(requireContext())
                        .setMessage(getString(R.string.add_success))
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                            parentFragmentManager.popBackStack()
                        }
                        .show()
                }

                is State.Failure -> {
                    finishLoading()
                    binding.tvError.text = result.throwable.message
                }
            }
        }
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
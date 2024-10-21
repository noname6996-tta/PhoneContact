package com.tta.phonebookapplication.ui.screens.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tta.phonebookapplication.ui.screens.fragment.LoadingDialogFragment

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var _binding: T? = null
    private val loadingDialog = LoadingDialogFragment()
    private var isLoading = false
    protected val binding: T
        get() = checkNotNull(_binding) {
            "Fragment $this binding cannot be accessed before onCreateView() or after onDestroyView()"
        }

    //Enabling or disabling the device back key
    abstract var isBackKeyActive: Boolean

    protected inline fun binding(block: T.() -> Unit): T {
        return binding.apply(block)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isBackKeyActive.not()) {
            requireActivity().onBackPressedDispatcher
                .addCallback(this) { }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getDataBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        addEvent()
        addObservers()
        initData()
    }

    abstract fun getDataBinding(): T

    open fun initViewModel() {}

    open fun initView() {}

    open fun addEvent() {}

    open fun addObservers() {}

    open fun initData() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun startLoading() {
        if (isLoading.not()) {
            isLoading = true
            val fragmentManager = parentFragmentManager
        val loadingFragment = fragmentManager.findFragmentByTag("LoadingDialogFragment") as? LoadingDialogFragment

        if (loadingFragment == null || !loadingFragment.isAdded) {
            LoadingDialogFragment().show(fragmentManager, "LoadingDialogFragment")
        }
        }
    }

    fun finishLoading() {
        if (isLoading) {
            val loadingFragment = parentFragmentManager.findFragmentByTag("LoadingDialogFragment") as? LoadingDialogFragment

        if (loadingFragment != null && loadingFragment.isAdded) {
            loadingFragment.dismiss()
        }
            isLoading = false
        }
    }
}

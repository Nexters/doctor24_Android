package com.nexters.doctor24.todoc.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.koin.android.ext.android.inject

internal abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    abstract val layoutResId: Int

    abstract val viewModel: VM

    protected val binding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<VB>(this, layoutResId)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding.lifecycleOwner = this
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
    }
}
package com.nexters.doctor24.todoc.base

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.nexters.doctor24.todoc.ext.isActivityDestroyed

abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment(), View.OnClickListener {

    abstract val layoutResId: Int
    abstract fun onDismiss()
    lateinit var binding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        val view = binding?.run {
            this?.lifecycleOwner = this@BaseDialogFragment
            this?.root
        } ?: inflater.inflate(layoutResId, container)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setCanceledOnTouchOutside(true)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismiss()
        super.onDismiss(dialog)
    }

    override fun onClick(v: View?) {
        dialogDismiss()
    }

    protected fun dialogDismiss() {
        if (!activity.isActivityDestroyed()) {
            dialog?.let {
                if (it.isShowing) it.dismiss()
            }
        }
    }
}

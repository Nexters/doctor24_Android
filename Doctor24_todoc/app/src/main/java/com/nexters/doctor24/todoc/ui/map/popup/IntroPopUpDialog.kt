package com.nexters.doctor24.todoc.ui.map.popup

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseDialogFragment
import com.nexters.doctor24.todoc.databinding.IntroPopUpDialogBinding
import com.nexters.doctor24.todoc.ext.dpToPixel
import com.nexters.doctor24.todoc.ui.map.NaverMapViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class IntroPopUpDialog : BaseDialogFragment<IntroPopUpDialogBinding>() {

    companion object {
        @JvmField
        val TAG: String = this::class.java.simpleName
    }

    override val layoutResId: Int = R.layout.intro_pop_up_dialog

    private val viewModel: NaverMapViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val metrics = DisplayMetrics()
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        val dialogWidth = (metrics.widthPixels - 24f.dpToPixel() * 2).toInt()
        val dialogHeight = (245f.dpToPixel()).toInt()
        dialog?.window?.setLayout(dialogWidth, dialogHeight)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.vm = viewModel
        initObserve()
    }

    private fun initObserve() {
        viewModel.dialogCloseEvent.observe(viewLifecycleOwner, Observer {
            dialogDismiss()
        })
    }

    override fun onDismiss() {

    }
}
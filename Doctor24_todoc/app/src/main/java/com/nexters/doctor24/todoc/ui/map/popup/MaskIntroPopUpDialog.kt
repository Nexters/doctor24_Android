package com.nexters.doctor24.todoc.ui.map.popup

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseDialogFragment
import com.nexters.doctor24.todoc.databinding.MaskIntroPopUpDialogBinding
import com.nexters.doctor24.todoc.ext.dpToPixel
import com.nexters.doctor24.todoc.ui.mask.MaskMapViewModel
import com.nexters.doctor24.todoc.util.getDayOfWeek
import com.nexters.doctor24.todoc.util.getDayOfWeekBuy
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MaskIntroPopUpDialog : BaseDialogFragment<MaskIntroPopUpDialogBinding>() {

    companion object {
        @JvmField
        val TAG: String = this::class.java.simpleName
    }

    override val layoutResId: Int = R.layout.mask_intro_pop_up_dialog

    private val viewModel: MaskMapViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val metrics = DisplayMetrics()
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        val dialogWidth = (metrics.widthPixels - 24f.dpToPixel() * 2).toInt()
        dialog?.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.vm = viewModel
        initView()
        initObserve()
    }

    private fun initView() {
        binding.tvPopUpTitle.text = HtmlCompat.fromHtml(getString(R.string.mask_popup_title, getDayOfWeek(), getDayOfWeekBuy()), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun initObserve() {
        viewModel.dialogCloseEvent.observe(viewLifecycleOwner, Observer {
            dialogDismiss()
        })
    }

    override fun onDismiss() {

    }
}
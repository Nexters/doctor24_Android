package com.nexters.doctor24.todoc.ui.map.preview

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.ui.map.NaverMapViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class PreviewFragment : BottomSheetDialogFragment() {

    private val viewModel: NaverMapViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.preview_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setCanceledOnTouchOutside(true)
        }
    }

    override fun dismiss() {
        super.dismiss()
        viewModel.onClosePreview()
    }
}
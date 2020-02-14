package com.nexters.doctor24.todoc.ui.map.preview

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.data.marker.response.OperatingDate
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.databinding.PreviewFragmentBinding
import com.nexters.doctor24.todoc.ui.findload.FindLoadDialog
import com.nexters.doctor24.todoc.ui.findload.FindLoadViewModel
import com.nexters.doctor24.todoc.util.toDistance
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class PreviewFragment : BottomSheetDialogFragment() {
    companion object {
        @JvmField
        val TAG: String = this::class.java.simpleName

        const val KEY_MEDICAL: String = "KEY_MEDICAL"
        const val KEY_MY_LOCATION: String = "KEY_MY_LOCATION"
    }
    private lateinit var binding : PreviewFragmentBinding
    private var previewData: PreviewUiData? = null
    private lateinit var currentLocation : LatLng
    private val previewViewModel : PreviewViewModel by sharedViewModel()
    private val findLoadViewModel : FindLoadViewModel by viewModel()
    private val findLoadDialog : FindLoadDialog by lazy { FindLoadDialog(findLoadViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = arguments?.let{
            it.getParcelable<ResMapMarker>(KEY_MEDICAL)
        }
        val loc = arguments?.let {
            it.getDoubleArray(KEY_MY_LOCATION)
        }

        data?.let {
            val from = loc?.let { LatLng(it[0], it[1]) }
            val to = LatLng(data.latitude, data.longitude)
            previewData = PreviewUiData(
                id = it.id,
                isEmergency = it.emergency,
                isNight = it.nightTimeServe,
                placeName = it.placeName,
                todayHour = it.day,
                phoneNumber = it.placePhone ?: "",
                phoneCall = if(it.placePhone == null || it.placePhone.isEmpty()) resources.getString(R.string.medical_call_disable) else resources.getString(R.string.medical_call),
                address = it.placeAddress ?: "",
                categories = getCategories(it.categories),
                distance = to.toDistance(from)
            )
            findLoadViewModel.centerName = it.placeName
            findLoadViewModel.determineLocation = to
            findLoadViewModel.currentLocation = from
        }

    }

    private fun getCategories(categories: List<String>?) : String {
        categories?.let {
            return if(it.count() < 10) {
                it.joinToString(separator = ", ")
            } else it.joinToString(separator = ", ", truncated = "등", limit = 10)
        }
        return ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate<PreviewFragmentBinding>(inflater, R.layout.preview_fragment, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setCanceledOnTouchOutside(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.item = previewData
        initObserve()

        binding.ivDetailedFragGotoMap.setOnClickListener {
            findLoadDialog.show(childFragmentManager, FindLoadDialog.TAG)
        }

        binding.tvDetailedFragCallBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, ("tel:${previewData?.phoneNumber ?: ""}").toUri()))
        }

    }

    private fun initObserve() {

    }

    override fun dismiss() {
        super.dismiss()
    }

    data class PreviewUiData(
        val id: String,
        val isEmergency : Boolean = false,
        val isNight : Boolean = false,
        val isNormal : Boolean = !isEmergency && !isNight,
        val placeName : String = "",
        val todayHour : OperatingDate?,
        val categories : String = "",
        val phoneNumber : String = "",
        val phoneCall : String = "",
        val address : String = "",
        val distance : String = ""
    )
}
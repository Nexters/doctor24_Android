package com.nexters.doctor24.todoc.ui.map.list

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.analytics.MEDICAL_LIST_SORT
import com.nexters.doctor24.todoc.base.BaseActivity
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.databinding.ActivityMedicalListBinding
import com.nexters.doctor24.todoc.ui.detailed.DetailedActivity
import com.nexters.doctor24.todoc.ui.findload.FindLoadDialog
import com.nexters.doctor24.todoc.ui.findload.FindLoadViewModel
import com.nexters.doctor24.todoc.ui.map.marker.group.GroupMarkerListDialog
import com.nexters.doctor24.todoc.util.toDistance
import com.nexters.doctor24.todoc.util.toDistanceDouble
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

internal class MedicalListActivity : BaseActivity<ActivityMedicalListBinding, MedicalListViewModel>(),
    MedicalListAdapter.MedicalListListener {
    companion object {
        @JvmField
        val TAG: String = this::class.java.simpleName

        const val KEY_MEDI_LIST: String = "KEY_MEDI_LIST"
        const val KEY_MEDI_MY_LOCATION: String = "KEY_MEDI_MY_LOCATION"
    }

    override val layoutResId: Int
        get() = R.layout.activity_medical_list
    override val viewModel: MedicalListViewModel by viewModel()
    private var groupData: ArrayList<ResMapMarker>? = arrayListOf()
    private var uiData = arrayListOf<GroupMarkerListDialog.GroupListHospitalUiData>()
    private var location : LatLng? = null
    private val listAdapter by lazy { MedicalListAdapter().apply { listener = this@MedicalListActivity } }
    private val findLoadViewModel : FindLoadViewModel by viewModel()
    private val findLoadDialog : FindLoadDialog by lazy { FindLoadDialog(findLoadViewModel) }

    private val analytics : FirebaseAnalytics by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        intent.extras?.apply {
            groupData = getParcelableArrayList<ResMapMarker>(KEY_MEDI_LIST)?.let {
                it
            } ?: arrayListOf()
            getDoubleArray(KEY_MEDI_MY_LOCATION)?.let{
                location = LatLng(it[0], it[1])
            }
        }
        /*groupData = intent.extras?.let{
            it.getParcelableArrayList<ResMapMarker>(KEY_MEDI_LIST) as ArrayList<ResMapMarker>
        }
        val loc = intent.extras?.let {
            it.getDoubleArray(KEY_MEDI_MY_LOCATION)
        }
        location = loc?.let { LatLng(it[0], it[1]) }

        Timber.d("location : $location")*/
        initView()
        initObserve()
    }

    private fun initView() {
        groupData?.forEach {
            uiData.add(
                GroupMarkerListDialog.GroupListHospitalUiData(
                    id = it.id,
                    type = it.medicalType,
                    isPharmacy = it.medicalType == MarkerTypeEnum.PHARMACY.type,
                    isEmergency = it.emergency,
                    isNight = it.nightTimeServe,
                    placeName = it.placeName,
                    todayHour = it.day,
                    distance = location?.toDistance(LatLng(it.latitude, it.longitude)) ?: "",
                    distanceOrder = location.toDistanceDouble(LatLng(it.latitude, it.longitude)),
                    determine = location,
                    isShowFindLoad = true
                )
            )
        }
        uiData.sortBy { it.distanceOrder }

        binding.tvTotal.text = String.format(getString(R.string.medical_list_total), uiData.count())

        binding.tvEmpty.isVisible = uiData.isEmpty()
        binding.rvList.isVisible = uiData.isNotEmpty()
        if(uiData.isNotEmpty()) {
            binding.rvList.apply{
                adapter = listAdapter.apply {
                    submitList(uiData.apply { add(GroupMarkerListDialog.GroupListHospitalUiData(isPharmacy = uiData[0].isPharmacy)) })
                }
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }
    }

    private fun initObserve() {
        viewModel.closeEvent.observe(this, Observer {
            finish()
        })

        viewModel.sortEvent.observe(this, Observer {
            analytics.logEvent(MEDICAL_LIST_SORT, null)
        })
    }

    override fun onClickListItem(index: Int) {
        uiData.let {
            startActivity(Intent(this, DetailedActivity::class.java).apply {
                putExtra(DetailedActivity.KEY_MEDICAL_TYPE, it[index].type)
                putExtra(DetailedActivity.KEY_MEDICAL_ID, it[index].id)
                putExtra(DetailedActivity.KEY_DISTANCE, it[index].distance)
            })
        }
    }

    override fun onClickFindLoad(index: Int) {
        uiData.let{
            findLoadViewModel.centerName = it[index].placeName
            findLoadViewModel.determineLocation = it[index].determine
            findLoadDialog.show(supportFragmentManager, FindLoadDialog.TAG)
        }
    }
}
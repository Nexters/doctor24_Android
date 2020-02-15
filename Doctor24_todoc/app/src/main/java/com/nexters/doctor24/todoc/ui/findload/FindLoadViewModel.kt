package com.nexters.doctor24.todoc.ui.findload

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.base.SingleLiveEvent
import timber.log.Timber


sealed class MapApps(val scheme: Uri, val packageName: String?) {
    class GoogleMap(mediName: String, endLoc: LatLng) : MapApps(
        "http://maps.google.com/maps?daddr=${endLoc.latitude},${endLoc.longitude}".toUri(),
        "com.google.android.apps.maps")
    class NaverMap(mediName: String, endLoc: LatLng) :MapApps(
        "nmap://route/walk?dlat=${endLoc.latitude}&dlng=${endLoc.longitude}&dname=${mediName}&appname=com.nexters.doctor24.todoc".toUri(),
        "com.nhn.android.nmap")
    class KakaoMap(mediName: String, endLoc: LatLng) : MapApps(
        "kakaomap://route?ep=${endLoc.latitude},${endLoc.longitude}&by=FOOT".toUri(),
        "net.daum.android.map")
}

class FindLoadViewModel : ViewModel() {

    var currentLocation : LatLng? = null
    var determineLocation : LatLng? = null
    var centerName : String = ""

    private val _checkedMapAppEvent = MutableLiveData<MapApps>()
    val checkedMapAppEvent : LiveData<MapApps> get() = _checkedMapAppEvent

    private val _closeEvent = SingleLiveEvent<Unit>()
    val closeEvent : LiveData<Unit> get() = _closeEvent



    fun onCheckedGoogleMap() {
        determineLocation?.let { medi ->
            Timber.d("Checked MapApps - google")
            _checkedMapAppEvent.value = MapApps.GoogleMap(centerName, medi)
        }
    }

    fun onCheckedNaverMap() {
        determineLocation?.let { medi ->
            Timber.d("Checked MapApps - naver")
            _checkedMapAppEvent.value = MapApps.NaverMap(centerName, medi)
        }
    }

    fun onCheckedKakaoMap() {
        determineLocation?.let { medi ->
            Timber.d("Checked MapApps - kakao")
            _checkedMapAppEvent.value = MapApps.KakaoMap(centerName, medi)
        }
    }


    fun onCloseDialog() {
        _closeEvent.call()
    }
}
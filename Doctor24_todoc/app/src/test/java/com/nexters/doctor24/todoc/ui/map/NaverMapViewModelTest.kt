package com.nexters.doctor24.todoc.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.OperatingDate
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.repository.MarkerRepository
import com.nexters.doctor24.todoc.rule.TestCoroutineRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by jiyoung on 13/01/2020
 */
@RunWith(MockitoJUnitRunner::class)
class NaverMapViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel : NaverMapViewModel
    @Mock
    private lateinit var markerRepo : MarkerRepository

    @Before
    fun init() {
//        MockitoAnnotations.initMocks(this)
//        startKoin { modules(listOf(networkModule, repositoryModule)) }
        viewModel = NaverMapViewModel(markerRepo)
    }

    @Test
    fun `(Given) Map 화면 진입 시 (When) 현재 위치 받아와서 (Then) 주변 병원 정보 마커 표시`() {
        val expectedResult = listOf(MarkerUIData(
            location = LatLng(0.0, 0.0),
            name = "강남병원"
        ))

        testCoroutineRule.runBlockingTest {
            Mockito. `when` (markerRepo.getMarkers("0.0", "0.0", MarkerTypeEnum.HOSPITAL)).thenReturn(
                listOf(
                    ResMapMarker(
                        placeName = "강남병원", latitude = 0.0, longitude = 0.0, medicalType = "hospital",
                        placeAddress = "", placePhone = "01000000000",
                        days = listOf(OperatingDate(
                            dayType = "MONDAY",
                            startTime = "00:00:00",
                            endTime = "00:00:00"))
                    ))
            )
            viewModel.reqMarker(0.0,0.0)
            viewModel.hospitalMarkerDatas.observeForever {
                println("hospitalMarderDatas : $it")
                assertEquals(expectedResult, it)
            }
        }

    }
}
/*
package com.nexters.doctor24.todoc.ui.map

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation

import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.OperatingDate
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.repository.MarkerRepository
import com.nexters.doctor24.todoc.rule.CoroutineTestRule
import com.nexters.doctor24.todoc.ui.map.category.CategoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

*/
/**
 * Created by jiyoung on 13/01/2020
 *//*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NaverMapViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var testCoroutineRule = CoroutineTestRule()

    private lateinit var viewModel: NaverMapViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    @Mock
    private lateinit var markerRepo: MarkerRepository

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var sharedPref : SharedPreferences

    @Before
    fun init() {
//        MockitoAnnotations.initMocks(this)
//        startKoin { modules(listOf(networkModule, repositoryModule)) }
        viewModel = NaverMapViewModel(testCoroutineRule.testDispatcherProvider, markerRepo)
        categoryViewModel = CategoryViewModel()
    }

    @Test
    fun `(Given) Map 화면 진입 시 (When) 현재 위치 받아와서 (Then) 주변 병원 정보 마커 표시`() {
        val expectedResult = listOf(
            MarkerUIData(
                location = LatLng(0.0, 0.0),
                count = 1,
                medicalType = "hospital"
            )
        )

        testCoroutineRule.testDispatcher.runBlockingTest {
            Mockito.`when`(markerRepo.getMarkers(LatLng(0.0, 0.0), MarkerTypeEnum.HOSPITAL, 2)).thenReturn(
                listOf(
                    ResMapLocation(
                        latitude = 0.0,
                        longitude = 0.0,
                        total = 1,
                        facilities = listOf(
                            ResMapMarker(
                                id = "id",
                                placeName = "강남병원",
                                latitude = 0.0,
                                longitude = 0.0,
                                medicalType = "hospital",
                                placeAddress = "",
                                placePhone = "01000000000",
                                days = listOf(
                                    OperatingDate(
                                        dayType = "MONDAY",
                                        startTime = "00:00:00",
                                        endTime = "00:00:00"
                                    )
                                )
                            )
                        )
                    )
                )
            )
            viewModel.reqMarker(LatLng(0.0, 0.0), 14.0)
            viewModel.hospitalMarkerDatas.observeForever {
                println("hospitalMarkerDatas : ${it.peekContent()}")
                assertEquals(expectedResult, it.peekContent())
            }
        }
    }

    @Test
    fun `(Given) 시간 스피너에서 (When) 시작식간을 변경 확인버튼을 누르면 (Then) 시작시간 텍스트 변`() {
        val expectedResult = "11:0"
        //viewModel

    }

    @Test
    fun `(Given) 필터버튼 클릭했을 때 (Then) 진료과목 선택 이력이 없으면 (When) 기본값 전체 선택`() {
        val expectedResult = 0

        Mockito.`when`(sharedPref.getInt(anyString(), anyInt())).thenReturn(0)
        viewModel.onClickFilter(sharedPref.getInt("KEY_PREF_CATEGORY", -1))
        viewModel.categoryEvent.observeForever {
            assertEquals(expectedResult, it)
        }
    }

}*/

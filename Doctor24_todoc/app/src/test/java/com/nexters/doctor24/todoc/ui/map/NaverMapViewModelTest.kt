package com.nexters.doctor24.todoc.ui.map

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.nexters.doctor24.todoc.data.marker.response.ResMapLocation

import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.data.marker.response.OperatingDate
import com.nexters.doctor24.todoc.data.marker.response.ResMapMarker
import com.nexters.doctor24.todoc.repository.MarkerRepository
import com.nexters.doctor24.todoc.rule.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NaverMapViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var testCoroutineRule = CoroutineTestRule()

    private lateinit var viewModel: NaverMapViewModel
    @Mock
    private lateinit var markerRepo: MarkerRepository

    @Mock
    lateinit var resources: Resources

    @Before
    fun init() {
//        MockitoAnnotations.initMocks(this)
//        startKoin { modules(listOf(networkModule, repositoryModule)) }
        viewModel = NaverMapViewModel(testCoroutineRule.testDispatcherProvider, markerRepo)
    }

    @Test
    fun `(Given) Map 화면 진입 시 (When) 현재 위치 받아와서 (Then) 주변 병원 정보 마커 표시`() {
        val expectedResult = listOf(
            MarkerUIData(
                location = LatLng(0.0, 0.0),
                count = 1
            )
        )

        testCoroutineRule.testDispatcher.runBlockingTest {
            Mockito.`when`(markerRepo.getBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0), MarkerTypeEnum.HOSPITAL)).thenReturn(
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
            viewModel.reqBounds(LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0)))
            viewModel.hospitalMarkerDatas.observeForever {
                println("hospitalMarkerDatas : $it")
                assertEquals(expectedResult, it)
            }
        }
    }

    /*@Test
    fun `(Given) Map 화면에서 (When) 병원 탭을 눌렀을 때 (Then) 하단에 주변 병원 리스트 텍스트가 노출`() {
        val expectedResult = "주변 병원 리스트"
        viewModel.onChangeTab(0)
        viewModel.tabChangeEvent.observeForever {
            viewModel.onChangeBottomTitle(String.format("주변 %s 리스트", viewModel.tabList[it].title))
        }
        viewModel.bottomTitle.observeForever {
            println("bottomTitle : $it")
            assertEquals(expectedResult, it)
        }
    }

    @Test
    fun `(Given) Map 화면에서 (When) 약국 탭을 눌렀을 때 (Then) 하단에 주변 약국 리스트 텍스트가 노출`() {
        val expectedResult = "주변 약국 리스트"
        viewModel.onChangeTab(1)
        viewModel.tabChangeEvent.observeForever {
            viewModel.onChangeBottomTitle(String.format("주변 %s 리스트", viewModel.tabList[it].title))
        }
        viewModel.bottomTitle.observeForever {
            println("bottomTitle : $it")
            assertEquals(expectedResult, it)
        }
    }

    @Test
    fun `(Given) Map 화면에서 (When) 동물병원 탭을 눌렀을 때 (Then) 하단에 주변 동물병원 리스트 텍스트가 노`() {
        val expectedResult = "주변 동물병원 리스트"
        viewModel.onChangeTab(2)
        viewModel.tabChangeEvent.observeForever {
            viewModel.onChangeBottomTitle(String.format("주변 %s 리스트", viewModel.tabList[it].title))
        }
        viewModel.bottomTitle.observeForever {
            println("bottomTitle : $it")
            assertEquals(expectedResult, it)
        }
    }*/
}
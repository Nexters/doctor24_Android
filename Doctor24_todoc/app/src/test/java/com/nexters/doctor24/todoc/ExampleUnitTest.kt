package com.nexters.doctor24.todoc

import com.nexters.doctor24.todoc.util.stockAt
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_stock(){
        print(stockAt("2020/04/01 09:02:03"))
    }
}

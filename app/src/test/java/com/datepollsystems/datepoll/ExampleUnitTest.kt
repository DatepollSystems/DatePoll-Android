package com.datepollsystems.datepoll

import com.datepollsystems.datepoll.utils.formatDateToEn
import org.junit.Test
import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val date = Date()

        assertEquals(formatDateToEn(date), "")
    }
}

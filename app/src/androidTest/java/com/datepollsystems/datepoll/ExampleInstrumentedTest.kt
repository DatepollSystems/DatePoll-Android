package com.datepollsystems.datepoll

import android.R.attr.country
import android.content.res.Configuration
import android.content.res.Resources
import androidx.test.InstrumentationRegistry.getContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.datepollsystems.datepoll.utils.formatDateEnToLocal
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.bke.datepoll", appContext.packageName)
    }

    @Test
    fun testExtensionMethod(){
        val locale = Locale("de", "de")
        Locale.setDefault(locale)
        val res: Resources = getContext().getResources()
        val config: Configuration = res.getConfiguration()
        config.locale = locale
        res.updateConfiguration(config, res.getDisplayMetrics())

        val r = formatDateEnToLocal("2000-10-02")
        assertEquals("2. Oktober 2000", r)
    }
}

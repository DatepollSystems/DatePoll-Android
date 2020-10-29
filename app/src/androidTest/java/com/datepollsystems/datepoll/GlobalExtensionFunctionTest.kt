package com.datepollsystems.datepoll

import android.content.res.Configuration
import android.content.res.Resources
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.datepollsystems.datepoll.utils.formatDateEnToLocal
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class GlobalExtensionFunctionTest {
    @Test
    fun testExtensionMethod(){
        val locale = Locale("de", "de")
        Locale.setDefault(locale)
        val res: Resources = InstrumentationRegistry.getInstrumentation().targetContext.resources
        val config: Configuration = res.configuration
        config.locale = locale
        res.updateConfiguration(config, res.displayMetrics)

        val r = formatDateEnToLocal("2000-10-02")
        Assert.assertEquals("2. Oktober 2000", r)
    }
}
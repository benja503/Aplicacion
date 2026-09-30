package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("RACHA", appName)
    }

    @Test
    fun `streak calculation test`() {
        val today = DateUtils.getTodayString()
        val yesterday = DateUtils.getDateStringForDaysAgo(1)
        val twoDaysAgo = DateUtils.getDateStringForDaysAgo(2)

        val set1 = setOf(today, yesterday, twoDaysAgo)
        assertEquals(3, DateUtils.calculateCurrentStreak(set1))

        // When today not studied yet, streak from yesterday should be alive
        val set2 = setOf(yesterday, twoDaysAgo)
        assertEquals(2, DateUtils.calculateCurrentStreak(set2))

        // When neither today nor yesterday studied, streak is 0
        val set3 = setOf(twoDaysAgo)
        assertEquals(0, DateUtils.calculateCurrentStreak(set3))
    }
}

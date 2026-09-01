package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.ListRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertTrue(appName.isNotEmpty())
  }

  @Test
  fun `launch MainActivity successfully`() {
    val controller = Robolectric.buildActivity(MainActivity::class.java)
    controller.setup()
    val activity = controller.get()
    assertTrue(activity != null)
  }

  @Test
  fun `update default priority tag persists and reflects changes`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = ListRepository(context)

    // Update default HIGH priority tag to "Urgent" with custom red color
    repo.updatePriority("HIGH", "Urgent", "#B91C1C")
    val prioritiesAfterUpdate = repo.priorities.value
    val highPriority = prioritiesAfterUpdate.find { it.id == "HIGH" }

    assertTrue("HIGH priority tag should exist", highPriority != null)
    assertEquals("Urgent", highPriority?.label)
    assertEquals("#B91C1C", highPriority?.colorHex)

    // Reset priorities
    repo.resetPriorities()
    val prioritiesAfterReset = repo.priorities.value
    val resetHigh = prioritiesAfterReset.find { it.id == "HIGH" }
    assertEquals("High", resetHigh?.label)
  }

  @Test
  fun `update widget theme style to plain white persists`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = ListRepository(context)

    val initialConfig = repo.widgetConfig.value
    val whiteConfig = initialConfig.copy(themeStyle = "WHITE")
    repo.updateWidgetConfig(whiteConfig)

    val updatedConfig = repo.widgetConfig.value
    assertEquals("WHITE", updatedConfig.themeStyle)
  }
}


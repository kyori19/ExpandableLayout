package com.github.aakira.expandablelayout.uitest

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.github.aakira.expandablelayout.ExpandableWeightLayout
import com.github.aakira.expandablelayout.uitest.utils.ElapsedIdLingResource
import com.github.aakira.expandablelayout.uitest.utils.equalHeight
import com.github.aakira.expandablelayout.uitest.utils.equalWeight
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as _is

@RunWith(AndroidJUnit4::class)
class ExpandableWeightLayoutActivityTest : ActivityTestRule<ExpandableWeightLayoutActivity>
(ExpandableWeightLayoutActivity::class.java) {

    companion object {
        const val DURATION = 1000L
    }

    @Before
    fun setUp() {
        launchActivity(activityIntent)
    }

    @After
    fun tearDown() {
        finishActivity()
    }

    @Test
    fun testExpandableWeightLayout() {
        val activity = activity
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val idlingRegistry = IdlingRegistry.getInstance()

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        val expandableLayout = activity.findViewById(R.id.expandableLayout) as ExpandableWeightLayout

        // default close
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))
        assertThat(expandableLayout.isExpanded, _is(false))

        // open toggle
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        var idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(3f)))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // change weight
        instrumentation.runOnMainSync { expandableLayout.move(6f) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(6f)))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick change a weight using move method
        instrumentation.runOnMainSync { expandableLayout.move(2f, 0, null) }
        idlingResource = ElapsedIdLingResource(0)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(2f)))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick collapse
        instrumentation.runOnMainSync { expandableLayout.collapse(0, null) }
        idlingResource = ElapsedIdLingResource(0)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(0f)))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(false))

        // set expanse (default expanse weight is 3)
        instrumentation.runOnMainSync { expandableLayout.isExpanded = true }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(3f)))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // check init layout
        instrumentation.runOnMainSync {
            expandableLayout.setExpandWeight(10f)
            expandableLayout.expand()
        }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalWeight(10f)))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))
    }
}

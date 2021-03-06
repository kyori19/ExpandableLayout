package com.github.aakira.expandablelayout.uitest

import android.app.Activity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.github.aakira.expandablelayout.uitest.utils.ElapsedIdLingResource
import com.github.aakira.expandablelayout.uitest.utils.equalHeight
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as _is

@RunWith(AndroidJUnit4::class)
class ExpandableRelativeLayoutActivityTest3 : ActivityTestRule<ExpandableRelativeLayoutActivity3>
(ExpandableRelativeLayoutActivity3::class.java) {

    companion object {
        const val DURATION = 350L
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
    fun testExpandableRelativeLayoutActivity3() {
        val activity = activity
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val idlingRegistry = IdlingRegistry.getInstance()

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        val expandableLayout = activity.findViewById(R.id.expandableLayout) as ExpandableRelativeLayout
        val child0 = activity.findViewById(R.id.child0) as LinearLayout
        val child1 = activity.findViewById(R.id.child1) as TextView
        val child2 = activity.findViewById(R.id.child2) as TextView
        val child3 = activity.findViewById(R.id.child3) as TextView

        // default close
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))
        assertThat(expandableLayout.isExpanded, _is(false))

        // open toggle
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        var idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child2,
                child3
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to first layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(0) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to second layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(1) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child1
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to third layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(2) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child2
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to forth layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(3) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child2,
                child3
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick move to first layout using moveChild method
        instrumentation.runOnMainSync { expandableLayout.moveChild(0, 0, null) }
        idlingResource = ElapsedIdLingResource(0)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick move to 200 using move method
        instrumentation.runOnMainSync { expandableLayout.move(200, 0, null) }
        idlingResource = ElapsedIdLingResource(0)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(200)))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // quick collapse
        instrumentation.runOnMainSync { expandableLayout.collapse(0, null) }
        idlingResource = ElapsedIdLingResource(0)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(false))

        // quick expand
        instrumentation.runOnMainSync { expandableLayout.expand(0, null) }
        idlingResource = ElapsedIdLingResource(0)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child0,
                child2,
                child3
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))
    }
}

package com.github.aakira.expandablelayout.uitest

import android.app.Activity
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
import com.github.aakira.expandablelayout.uitest.utils.orMoreHeight
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExpandableRelativeLayoutActivityTest : ActivityTestRule<ExpandableRelativeLayoutActivity>
(ExpandableRelativeLayoutActivity::class.java) {

    companion object {
        const val DURATION = 500L
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
    fun testExpandableRelativeLayoutActivity() {
        val activity = activity
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val idlingRegistry = IdlingRegistry.getInstance()

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        val expandableLayout = activity.findViewById(R.id.expandableLayout) as ExpandableRelativeLayout
        val child1 = activity.findViewById(R.id.child1) as TextView
        val child2 = activity.findViewById(R.id.child2) as TextView
        val child3 = activity.findViewById(R.id.child3) as TextView

        // default close
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))

        // open toggle
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        var idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(orMoreHeight(1)))
        idlingRegistry.unregister(idlingResource)

        // move to first layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(0) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.child1)).check(matches(equalHeight(expandableLayout)))
        idlingRegistry.unregister(idlingResource)

        // set close height
        instrumentation.runOnMainSync { expandableLayout.closePosition = expandableLayout.currentPosition; }

        // move to second layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(1) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.child2)).check(matches(orMoreHeight(1)))
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child1,
                child2
        )))
        idlingRegistry.unregister(idlingResource)

        // check toggle (close to first)
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        // move to first position
        onView(withId(R.id.child1)).check(matches(equalHeight(expandableLayout)))
        idlingRegistry.unregister(idlingResource)

        // check toggle open (full)
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        // move to first position
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child1,
                child2,
                child3
        )))
        idlingRegistry.unregister(idlingResource)
    }
}

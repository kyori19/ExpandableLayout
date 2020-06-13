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
import com.github.aakira.expandablelayout.uitest.utils.equalWidth
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as _is

@RunWith(AndroidJUnit4::class)
class ExpandableRelativeLayoutActivityTest2 : ActivityTestRule<ExpandableRelativeLayoutActivity2>
(ExpandableRelativeLayoutActivity2::class.java) {

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
    fun testExpandableRelativeLayoutActivity2() {
        val activity = activity
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val idlingRegistry = IdlingRegistry.getInstance()
        val resources = activity.resources

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        val expandableLayout = activity.findViewById(R.id.expandableLayout) as ExpandableRelativeLayout
        val child1 = activity.findViewById(R.id.child1) as TextView
        val child2 = activity.findViewById(R.id.child2) as TextView
        val child3 = activity.findViewById(R.id.child3) as TextView
        val expandableLayoutPadding = resources.getDimensionPixelSize(R.dimen.margin_large)

        // default close
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))
        assertThat(expandableLayout.isExpanded, _is(false))

        // width
        onView(withId(R.id.child1)).check(matches(
                equalWidth(resources.getDimensionPixelSize(R.dimen.relative_layout_2_child_1_width))))
        onView(withId(R.id.child2)).check(matches(
                equalWidth(resources.getDimensionPixelSize(R.dimen.relative_layout_2_child_2_width))))

        // open toggle
        instrumentation.runOnMainSync { expandableLayout.toggle() }
        var idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child1,
                child3,
                margin = expandableLayoutPadding
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to first layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(0) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child1,
                margin = expandableLayoutPadding
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))

        // move to second layout
        instrumentation.runOnMainSync { expandableLayout.moveChild(1) }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child2,
                margin = expandableLayoutPadding
        )))
        idlingRegistry.unregister(idlingResource)
        assertThat(expandableLayout.isExpanded, _is(true))
    }
}

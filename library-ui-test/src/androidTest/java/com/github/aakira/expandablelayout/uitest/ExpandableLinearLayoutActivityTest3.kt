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
import com.github.aakira.expandablelayout.ExpandableLinearLayout
import com.github.aakira.expandablelayout.uitest.utils.ElapsedIdLingResource
import com.github.aakira.expandablelayout.uitest.utils.equalHeight
import com.github.aakira.expandablelayout.uitest.utils.orMoreHeight
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * test for [com.github.aakira.expandablelayout.ExpandableLinearLayout#initlayout]
 *
 * The default value is  {@link android.view.animation.AccelerateDecelerateInterpolator}
 *
 */
@RunWith(AndroidJUnit4::class)
class ExpandableLinearLayoutActivityTest3 : ActivityTestRule<ExpandableLinearLayoutActivity3>
(ExpandableLinearLayoutActivity3::class.java) {

    companion object {
        const val DURATION = 300L
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
    fun testExpandableLinearLayoutActivity3() {
        val activity = activity
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val idlingRegistry = IdlingRegistry.getInstance()

        // check activity
        assertThat<Activity>(activity, notNullValue())
        assertThat(instrumentation, notNullValue())

        val expandableLayout = activity.findViewById(R.id.expandableLayout) as ExpandableLinearLayout
        val child1 = activity.findViewById(R.id.child1) as TextView
        val child2 = activity.findViewById(R.id.child2) as TextView
        val marginSmall = getActivity().resources.getDimensionPixelSize(R.dimen.margin_small)

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
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child1,
                margin = marginSmall
        )))
        idlingRegistry.unregister(idlingResource)

        // change child size
        instrumentation.runOnMainSync {
            child1.text = "++++++++++++++++++++check_init_layout++++++++++++++++++++++++++++" +
                    "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
            expandableLayout.initLayout()
        }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(0)))
        idlingRegistry.unregister(idlingResource)

        // check init layout
        instrumentation.runOnMainSync { expandableLayout.expand() }
        idlingResource = ElapsedIdLingResource(DURATION)
        idlingRegistry.register(idlingResource)
        onView(withId(R.id.expandableLayout)).check(matches(equalHeight(
                child1,
                child2,
                margin = marginSmall
        )))
        idlingRegistry.unregister(idlingResource)
    }
}

package com.anthony.foodmap

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.anthony.foodmap.ui.MainActivity
import com.anthony.foodmap.util.DataBindingIdlingResource
import com.anthony.foodmap.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Large End-to-End test for the venues module.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {


    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)


    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setup() {
        dataBindingIdlingResource.activity = mActivityTestRule.activity
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }
    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun testView() {
        onView(withId(R.id.venues_list)).check(matches(isDisplayed()))
    }
}

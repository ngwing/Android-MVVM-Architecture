package com.anthony.foodmap.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.anthony.foodmap.R
import com.anthony.foodmap.util.DataBindingIdlingResource
import com.anthony.foodmap.util.monitorActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class VenuesFragmentTest {
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Test
    fun display() {
//        launchActivity()
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)


        Espresso.onView(withId(R.id.venues_list)).check(matches(isDisplayed()))
//        Espresso.onView(withText("TITLE1")).check(matches(IsNot.not(isDisplayed())))
    }


    private fun launchActivity(): ActivityScenario<MainActivity>? {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity { activity ->
            // Disable animations in RecyclerView
            (activity.findViewById(R.id.venues_list) as RecyclerView).itemAnimator = null
        }
        return activityScenario
    }


}
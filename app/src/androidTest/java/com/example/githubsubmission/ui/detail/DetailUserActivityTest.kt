package com.example.githubsubmission.ui.detail

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubsubmission.database.FavoriteUser
import com.example.githubsubmission.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailUserActivityTest{

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(DetailUserActivity::class.java)


    @Test
    fun assertFavoriteButton() {
        onView(withId(R.id.floating_button)).check(matches(isDisplayed()))
        onView(withId(R.id.floating_button)).perform(click())
        onView(withId(R.id.floating_button)).check(matches(withDrawable(R.drawable.star_24)))
        onView(withId(R.id.floating_button)).perform(click())
        onView(withId(R.id.floating_button)).check(matches(withDrawable(R.drawable.outline_star_24)))
    }

    private fun withDrawable(resourceId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.appendText("with drawable from resource id: $resourceId")
            }

            override fun matchesSafely(item: View?): Boolean {
                if (item !is ImageView) {
                    return false
                }
                val expectedDrawable = ContextCompat.getDrawable(item.context, resourceId)
                val actualDrawable = item.drawable
                return expectedDrawable?.constantState == actualDrawable?.constantState
            }
        }
    }
}
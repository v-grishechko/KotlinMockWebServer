package com.kotlinmockwebserver.espresso

import android.support.annotation.IdRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId

fun enterText(@IdRes id: Int, text: String) {
    onView(withId(id)).perform(typeText(text))
}

fun pressButton(@IdRes id: Int) {
    onView(withId(id)).perform(click())
}

fun isVisible(@IdRes id: Int) {
    onView(withId(id)).check(matches(isDisplayed()))
}

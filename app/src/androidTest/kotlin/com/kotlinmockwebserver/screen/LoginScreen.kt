package com.kotlinmockwebserver.screen

import android.app.Activity
import com.kotlinmockwebserver.espresso.enterText
import com.kotlinmockwebserver.espresso.isVisible
import com.kotlinmockwebserver.espresso.pressButton
import com.kotlinmockwebserver.example.R
import org.junit.Assert
import org.junit.Assert.assertTrue

class LoginScreen {

    fun login(login: String) = enterText(R.id.email, login)

    fun password(password: String) = enterText(R.id.password, password)

    fun pressLogin() = pressButton(R.id.email_sign_in_button)

    fun assert(func: Assert.() -> Unit) {
        val assert = Assert()
        assert.func()
    }

    class Assert {

        fun showingProgress() {
            isVisible(R.id.login_progress)
        }

    }
}

fun loginScreen(func: LoginScreen.() -> Unit) {
    val loginScreen = LoginScreen()
    loginScreen.func()
}
package com.kotlinmockwebserver.test

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.kotlinmockwebserver.example.app.GithubApp
import com.kotlinmockwebserver.example.di.DaggerAppComponent
import com.kotlinmockwebserver.example.di.modules.*
import com.kotlinmockwebserver.example.ui.activities.SignInActivity
import com.kotlinmockwebserver.screen.loginScreen
import com.kotlinmockwebserver.server_rule.MockWebServerRule
import com.kotlinmockwebserver.server_rule.OnlinerMockWebServerRule
import com.kotlinmockwebserver.server_rule.interceptor.HostSelectionInterceptor
import com.kotlinmockwebserver.server_rule.request.Header
import com.kotlinmockwebserver.server_rule.rule
import com.kotlinmockwebserver.utils.ResponseUtils
import okhttp3.Interceptor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    val server = mockServer()

    val loginScreenRule = loginScreen()

    @Rule @JvmField
    val rulechain: RuleChain? = RuleChain.emptyRuleChain()
            .around(loginScreenRule)
            .around(server)

    @Test
    fun loginSuccessfull() {
        server.addRule(loginSucess())

        loginScreen {

            login("ultimate3939@gmail.com")
            password("androidjavaforever")
            pressLogin()

            assert {
                showingProgress()
            }
        }
    }

    fun loginSucess(): com.kotlinmockwebserver.server_rule.Rule {

        val rule = rule {
            get("/user") {

                response {
                    code = 200
                    body = ResponseUtils.getFileAsString("login_sucess_response.json")
                    headers(Header("OkHttp", "Ok"))
                }
            }
        }

        return rule
    }

    fun loginScreen(): ActivityTestRule<SignInActivity> {
        return ActivityTestRule(SignInActivity::class.java)
    }

    fun mockServer(): MockWebServerRule {
        return OnlinerMockWebServerRule()
    }
}
package com.kotlinmockwebserver.server_rule

import com.kotlinmockwebserver.example.app.GithubApp
import com.kotlinmockwebserver.example.di.AppComponent
import com.kotlinmockwebserver.example.di.DaggerAppComponent
import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.*

class OnlinerMockWebServerRule : MockWebServerRule {

    val server: okhttp3.mockwebserver.MockWebServer = okhttp3.mockwebserver.MockWebServer()

    var rules: MutableList<Rule> = ArrayList()

    var isServerStarted: Boolean = false

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                start()

                GithubApp.setAppComponent(object : TestComponent() {

                })

                server.setDispatcher(dispatcher())

                base?.evaluate()

                rules.clear()

                stop()
            }
        }
    }

    override fun start() {
        if (isServerStarted) {
            return
        }

        try {
            server.start()
            isServerStarted = true
        } catch (e: Exception) {
            print(e)
        }
    }

    override fun stop() {
        if (!isServerStarted) {
            return
        }

        try {
            server.shutdown()
            isServerStarted = false
        } catch (e: Exception) {
            print(e)
        }
    }

    override fun addRule(rule: Rule) {
        rules.add(rule)
    }

    override fun removeRule(rule: Rule) {
        rules.remove(rule)
    }

    override fun url(): HttpUrl {
       return server.url("")
    }

    fun dispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                val rule = rules.firstOrNull { it.request.path.equals(request?.path, true) } ?: return response404()

                val mockedResponse = MockResponse()

                for (header in rule.response.headers) {
                    mockedResponse.addHeader(header.name, header.value)
                }

                mockedResponse.setBody(rule.response.body)
                mockedResponse.setResponseCode(rule.response.code)

                rules.remove(rule)

                return mockedResponse
            }
        }
    }

    fun response404(): MockResponse {
        return MockResponse().setResponseCode(404)
    }

}
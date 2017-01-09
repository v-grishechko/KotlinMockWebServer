package com.kotlinmockwebserver.server_rule

import android.support.test.InstrumentationRegistry
import com.kotlinmockwebserver.example.app.GithubApp
import com.kotlinmockwebserver.example.di.DaggerAppComponent
import com.kotlinmockwebserver.example.di.modules.ContextModule
import com.kotlinmockwebserver.example.di.modules.RetrofitModule
import com.kotlinmockwebserver.server_rule.interceptor.HostSelectionInterceptor
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.runner.Description
import org.junit.runners.model.Statement
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subjects.SerializedSubject
import java.util.*

class OnlinerMockWebServerRule : MockWebServerRule {

    val server: okhttp3.mockwebserver.MockWebServer = okhttp3.mockwebserver.MockWebServer()

    var rules: MutableList<Rule> = ArrayList()

    var isServerStarted: Boolean = false

    var stopEvents = SerializedSubject(BehaviorSubject.create<Unit>())

    var startEvents = SerializedSubject(BehaviorSubject.create<Unit>())

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                start()

                GithubApp.getAppComponent().hostInterceptor().setHost(url())

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
            startEvents.onNext(Unit)
            isServerStarted = true
        } catch (e: Exception) {
            print(e)
        }
    }

    override fun started(): Observable<Unit> {
        return startEvents
    }

    override fun stop() {
        if (!isServerStarted) {
            return
        }

        try {
            server.shutdown()
            stopEvents.onNext(Unit)
            isServerStarted = false
        } catch (e: Exception) {
            print(e)
        }
    }

    override fun stopped(): Observable<Unit> {
        return stopEvents
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
                val rule = rules.firstOrNull { it.request?.path.equals(request?.path, true) } ?: return response404()

                val mockedResponse = MockResponse()

                if (rule.response != null) {
                    for ((name, value) in rule.response!!.headers) {
                        mockedResponse.addHeader(name, value)
                    }

                    mockedResponse.setBody(rule.response!!.body)
                    mockedResponse.setResponseCode(rule.response!!.code)
                }

                rules.remove(rule)

                return mockedResponse
            }
        }
    }

    fun response404(): MockResponse {
        return MockResponse().setResponseCode(404)
    }
}
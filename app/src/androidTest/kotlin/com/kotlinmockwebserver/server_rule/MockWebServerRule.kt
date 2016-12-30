package com.kotlinmockwebserver.server_rule

import android.net.Uri
import com.kotlinmockwebserver.server_rule.request.Request
import com.kotlinmockwebserver.server_rule.response.Response
import okhttp3.HttpUrl
import org.junit.rules.TestRule
import rx.Observable


interface MockWebServerRule : TestRule {

    fun start()

    fun started() : Observable<Unit>

    fun stop()

    fun stopped() : Observable<Unit>

    fun url() : HttpUrl

    fun addRule(rule: Rule)

    fun removeRule(rule: Rule)

}
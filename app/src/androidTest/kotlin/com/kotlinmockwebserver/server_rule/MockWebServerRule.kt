package com.kotlinmockwebserver.server_rule

import android.net.Uri
import com.kotlinmockwebserver.server_rule.request.Request
import com.kotlinmockwebserver.server_rule.response.Response
import okhttp3.HttpUrl
import org.junit.rules.TestRule


interface MockWebServerRule : TestRule {

    fun start()

    fun stop()

    fun url() : HttpUrl

    fun addRule(rule: Rule)

    fun removeRule(rule: Rule)
}
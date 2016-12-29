package com.kotlinmockwebserver.server_rule.response

import com.kotlinmockwebserver.server_rule.request.Header
import java.util.*

class Response {

    var headers : List<Header> = ArrayList()

    var body: String = ""

    var code: Int = 200

    fun headers(vararg headers: Header) {
        val result = ArrayList<Header>()
        for (header in headers) {
            result.add(header)
        }

        this.headers = result
    }

}
package com.kotlinmockwebserver.server_rule.request

import com.kotlinmockwebserver.server_rule.response.Response
import java.util.*

abstract class Request(val path: String, val type: Request.Type) {

    var headers: List<Header> = ArrayList()

    var body: String = ""

    var response: Response? = null

    fun headers(vararg headers: Header) {
        val result = ArrayList<Header>()
        for (header in headers) {
            result.add(header)
        }

        this.headers = result
    }

    fun execute(exec: () -> Response?): Response? {
        return exec()
    }

    enum class Type {
        GET, POST
    }
}

class Get(path: String) : Request(path, type = Request.Type.GET)

class Post(path: String) : Request(path, type = Request.Type.POST)

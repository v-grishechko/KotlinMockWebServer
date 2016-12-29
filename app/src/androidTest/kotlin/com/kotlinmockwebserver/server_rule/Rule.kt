package com.kotlinmockwebserver.server_rule

import com.kotlinmockwebserver.server_rule.request.Get
import com.kotlinmockwebserver.server_rule.request.Post
import com.kotlinmockwebserver.server_rule.request.Request
import com.kotlinmockwebserver.server_rule.response.Response

data class Rule(var request: Request, var response: Response) {

    fun get(url: String, init: Get.() -> Unit) {
        val request = Get(url)
        request.init()
        this.request = request
    }

    fun post(url: String, init: Post.() -> Unit) {
        val request = Post(url)
        request.init()
        this.request = request
    }

    fun response(init: Response.() -> Unit) {
        val response = Response()
        response.init()
        this.response = response
    }
}


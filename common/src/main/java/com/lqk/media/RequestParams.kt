package com.lqk.media

import java.io.FileNotFoundException
import java.util.concurrent.ConcurrentHashMap

class RequestParams {

    var urlParams = ConcurrentHashMap<String, String>()
    var fileParams = ConcurrentHashMap<String, Any>()

    /**
     * Constructs a new empty `RequestParams` instance.
     */
    constructor() : this(mutableMapOf<String, String>())

    /**
     * Constructs a new RequestParams instance containing the key/value string
     * params from the specified map.
     *
     * @param source the source key/value string map to add.
     */
    constructor(source: Map<String, String>?) {
        if (source != null) {
            for ((key, value) in source) {
                put(key, value)
            }
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string param.
     *
     * @param key   the key name for the intial param.
     * @param value the value string for the initial param.
     */
    constructor(key: String, value: String) : this(object : HashMap<String, String>() {
        init {
            put(key, value)
        }
    })

    /**
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    fun put(key: String?, value: String?) {
        if (key != null && value != null) {
            urlParams[key] = value
        }
    }

    @Throws(FileNotFoundException::class)
    fun put(key: String?, o: Class<*>) {
        if (key != null) {
            fileParams[key] = o
        }
    }

    fun hasParams(): Boolean {
        return urlParams.size > 0 || fileParams.size > 0
    }
}

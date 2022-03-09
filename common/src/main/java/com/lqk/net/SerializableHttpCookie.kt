package com.lqk.net

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.HttpCookie


/**
 * @author LQK
 * @time 2019/3/11 17:20
 * @remark
 */
class SerializableHttpCookie : Serializable {
    companion object {
        private const val serialVersionUID = 6374381323722046732L
    }

    @Transient
    private var cookie: HttpCookie? = null
    @Transient
    private var clientCookie: HttpCookie? = null

    constructor(cookie: HttpCookie) {
        this.cookie = cookie
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(cookie?.name)
        out.writeObject(cookie?.value)
        out.writeObject(cookie?.comment)
        out.writeObject(cookie?.commentURL)
        out.writeObject(cookie?.domain)
        out.writeLong(cookie?.maxAge ?: 0L)
        out.writeObject(cookie?.path)
        out.writeObject(cookie?.portlist)
        out.writeInt(cookie?.version ?: 0)
        out.writeBoolean(cookie?.secure ?: false)
        out.writeBoolean(cookie?.discard ?: false)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inputStream: ObjectInputStream) {
        val name = inputStream.readObject() as String
        val value = inputStream.readObject() as String
        clientCookie = HttpCookie(name, value)
        clientCookie?.comment = inputStream.readObject() as String
        clientCookie?.commentURL = inputStream.readObject() as String
        clientCookie?.domain = inputStream.readObject() as String
        clientCookie?.maxAge = inputStream.readLong()
        clientCookie?.path = inputStream.readObject() as String
        clientCookie?.portlist = inputStream.readObject() as String
        clientCookie?.version = inputStream.readInt()
        clientCookie?.secure = inputStream.readBoolean()
        clientCookie?.discard = inputStream.readBoolean()
    }
}
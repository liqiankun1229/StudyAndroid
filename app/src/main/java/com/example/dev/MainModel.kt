package com.example.dev

import android.os.Build
import androidx.annotation.RequiresApi

class MainModel {
    private var mainPresenter: MainPresenter? = null

    constructor(mainPresenter: MainPresenter) {
        this.mainPresenter = mainPresenter
    }

    fun getData(): String {
        return "123"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDataSync() {
        Thread {
            var s = MutableList<Int>(3) { 0 }
            s.add(4)
            var s1 = ArrayList<Int>()
            s1.add(5)
            s += 5
            s.removeIf {
                return@removeIf it == 5
            }
            s.forEach { }
            for (i in s.indices) {

            }
            s.forEachIndexed { index, i ->

            }
            var (v1) = s

            var arr = arrayOf<Int>()
            var arr1 = arrayOf<String>()

            Thread.sleep(2000)
            mainPresenter?.responseTxt("321")
        }.run()
    }
}
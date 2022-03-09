package com.example.dev

import android.content.Context

class MainPresenter {
    private var mainActivity: MVPActivity? = null
    private var mainModel: MainModel? = null

    constructor(context: Context) {
        if (context is MVPActivity) {
            this.mainActivity = context
        }
        mainModel = MainModel(this)
    }

    fun updateText() {
        var data = mainModel?.getData()
        if (data != null && data != "") {
            mainActivity?.updateTxt(data)
        }
    }

    fun updateTextSync(){
        mainModel?.getDataSync()
    }

    fun responseTxt(str:String){
        mainActivity?.updateTxt(str)
    }
}
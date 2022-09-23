package com.lqk.rxjava

import android.util.Log
import android.view.View
import com.lqk.rxjava.databinding.ActivityMainBinding
import io.reactivex.*
import io.reactivex.BackpressureStrategy.DROP
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class RxJavaActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        const val TAG = "RxJavaActivity"
    }

    var observer = object : CustomObserver<String> {
        override fun changeValue(t: String) {
            Log.d(TAG, "changeValue: 1 $t")
        }
    }

//    class MySubscribe<S, T>(s: S) : SyncOnSubscribe<S, T>() {
//        var s: S = s
//        override fun generateState(): S {
//            return s
//        }
//
//        override fun next(state: S, observer: Observer<in T>?): S {
//            return state
//        }
//    }

    override fun initVM(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var customObservable: CustomObservable<String>

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: tv-h-resume: ${vb.tv.height}")
        Log.d(TAG, "onResume: tv-mh-resume: ${vb.tv.measuredHeight}")
    }

    override fun initView() {
        super.initView()
        Log.d(TAG, "onCreate: tv-h: ${vb.tv.height}")
        Log.d(TAG, "onCreate: tv-mh: ${vb.tv.measuredHeight}")
        vb.tv.post {
            Log.d(TAG, "onCreate: tv-h-post: ${vb.tv.height}")
            Log.d(TAG, "onCreate: tv-mh-post: ${vb.tv.measuredHeight}")
        }

        customObservable = CustomObservable<String>("123")
        customObservable.addObserver(observer)
            .addObserver(object : CustomObserver<String> {
                override fun changeValue(t: String) {
                    Log.d(TAG, "changeValue: 2 $t")
                }
            }).addObserver(object : CustomObserver<String> {
                override fun changeValue(t: String) {
                    Log.d(TAG, "changeValue: 3 $t")
                }
            }).addObserver(object : CustomObserver<String> {
                override fun changeValue(t: String) {
                    Log.d(TAG, "changeValue: 4 $t")
                }
            })

        var txt = getString(R.string.txt)
        var a = 1
        var b = 2
        a = b.also {
            b = a
        }

        // RxJava
//        rx.Observable.create(MySubscribe<String, String>("123"))
//            .map { t -> t?.toInt() ?: -1 }
//            .observeOn(rx.schedulers.Schedulers.io()) // 控制上游数据 线程
//            .subscribeOn(rx.schedulers.Schedulers.io()) // 控制下游数据线程
//            .subscribe()

        // RxJava2
        var observerRxJava2 = object : io.reactivex.Observer<String> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: String) {

            }

            override fun onError(e: Throwable) {

            }
        }
        // rx2
        var observable =
            Observable
                .create(ObservableOnSubscribe<String> {
                    "String"
                })
                .map {

                }
//                .flatMap(object :Function<String,Int>(){
//                    override fun apply(t: String): Int {
//                        return t.length
//                    }
//                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flatMap {
                    return@flatMap ObservableSource<String> {

                    }
                }
                .flatMap {
                    return@flatMap ObservableSource<String> {

                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    it.length
                }
                .subscribe(
                )

        val do1 = ObservableOnSubscribe<String> {}
        var do2 = Observable.create(do1)
        var do3 = do2.map(object : io.reactivex.functions.Function<String, Int> {
            override fun apply(t: String): Int {
                return t.length
            }
        })
        var do4 = do3.observeOn(Schedulers.io())
        var do5 = do4.subscribeOn(Schedulers.io())
        var do6 = do5.flatMap(object : io.reactivex.functions.Function<Int, Observable<String>> {
            override fun apply(t: Int): Observable<String> {
                return Observable.create { t.toString() }
            }
        })
        var do7 = do6.subscribe(object : Consumer<String> {
            override fun accept(t: String?) {

            }
        }, object : Consumer<Throwable> {
            override fun accept(t: Throwable?) {

            }
        }, object : Action {
            override fun run() {

            }
        }, object : Consumer<Disposable> {
            override fun accept(t: Disposable?) {

            }
        })
        vb.btnRxjava1.setCompoundDrawables(getDrawable(R.drawable.ic_passed), null, null, null)
        vb.btnRxjava1.setCompoundDrawables(
            getDrawable(R.drawable.ic_close),
            getDrawable(R.drawable.ic_arrow_back),
            null,
            null
        )
        vb.btnRxjava1.setOnClickListener { t ->
            run {
                if (t == null) {
                    return@run
                }
                EventActivity.show(applicationContext)
//                LiveEventBus.get<String>(EVENT_LOGIN).post("RxJavaActivity")
//                EventActivity.show(this@RxJavaActivity)
//                try {
//                    var s = StringBuilder();
//                    var p = Runtime.getRuntime()
//                        .exec("dumpsys activity top | grep ACTIVITY | tail -n 1")
////                        .exec("adb shell dumpsys activity top | grep ACTIVITY | tail -n 1")
//                    var i = p.inputStream;
//                    var b = BufferedReader(InputStreamReader(i))
//                    try {
//                        p.waitFor()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                    var line: String? = b.readLine()
//                    while (line != null) {
//                        s.append(line)
//                        line = b.readLine()
//                    }
//                    i.close()
//                    b.close()
//                    Log.d(TAG, "onCreate: $s")
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                val s = ""
//                // 传递一个base64的蹄片
//                Base64Activity.show(this@RxJavaActivity)
                // 被观察者 RxJava
//                Observable
//                    .create(
//                        Observable.OnSubscribe<String> {
//                            // call 方法
//                            if (!it.isUnsubscribed) {
//                                it.onNext("data")
//                                it.onCompleted()
//                            }
//                        })
//                    .subscribeOn(rx.schedulers.Schedulers.newThread())
//                    .subscribe(
//                        // 观察者
//                        object : Observer<String> {
//                            override fun onError(e: Throwable?) {
//                                Log.d(TAG, "1-OnError${e?.printStackTrace()}")
//                            }
//
//                            override fun onNext(t: String?) {
//                                Log.d(TAG, "1-OnNext: $t")
//                            }
//
//                            override fun onCompleted() {
//                                Log.d(TAG, "1-OnCompleted")
//                            }
//
//                        })
            }
        }

        vb.btnRxjava2.setOnClickListener { t ->
            run {
                if (t == null) {
                    return@run
                }
                // 支持 背压
                Flowable
                    .create(FlowableOnSubscribe<String> {
                        // subscribe
                        if (!it.isCancelled) {
                            it.onNext("data")
                            it.onComplete()
                        }
                    }, DROP)
                    .flatMap { v ->
                        return@flatMap Flowable.create(FlowableOnSubscribe<String> {
                            var i = 0
                            while (i < 10) {
                                if (!it.isCancelled) {
                                    Log.d(TAG, "onCreate: send $it")
                                    it.onNext(v)
                                } else {
                                    Log.d(TAG, "onCreate: not send $it")
                                }
                                i++
                            }
                        }, DROP)
                    }
                    .map {
                        return@map "$it:$it"
                    }
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        //
                        object : Subscriber<String> {
                            override fun onComplete() {
                                Log.d(TAG, "2-OnComplete")
                            }

                            override fun onSubscribe(s: Subscription?) {
                                // 背压处理 响应式拉取
                                // 返回一个数值，表示观察者
                                s?.request(Long.MAX_VALUE)
                                Log.d(TAG, "2-OnSubscribe")
                            }

                            override fun onNext(t: String?) {
                                Log.d(TAG, "2-OnNext: $t")
                            }

                            override fun onError(t: Throwable?) {
                                Log.d(TAG, "2-OnError: ${t!!.printStackTrace()}")
                            }

                        })
            }
        }

        vb.btnRxjava2Flase.setOnClickListener { t ->
            run {
                if (t == null) {
                    return@run
                }
                io.reactivex.Observable
                    .create(ObservableOnSubscribe<String> {
                        // subscribe
                        if (!it.isDisposed) {
                            it.onNext("data")
                            it.onComplete()
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .lift(ObservableOperator<Int, String> {
                        object : io.reactivex.Observer<String> {
                            override fun onComplete() {

                            }

                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(t: String) {

                            }

                            override fun onError(e: Throwable) {

                            }
                        }
                    })
                    .map { t -> t.toInt() }
                    .subscribe(object : io.reactivex.Observer<Int> {
                        override fun onComplete() {
                            Log.d(TAG, "2-OnComplete")
                        }

                        override fun onSubscribe(d: Disposable) {
                            // 无背压处理不用 参数不同，不用做修改 request 值处理
                            Log.d(TAG, "2-OnSubscribe")
                        }

                        override fun onNext(t: Int) {
                            Log.d(TAG, "2-OnNext: $t")
                        }

                        override fun onError(e: Throwable) {
                            Log.d(TAG, "2-OnError: ${e.printStackTrace()}")
                        }
                    })
//                        .subscribe(object : io.reactivex.Observer<String> {
//                            override fun onComplete() {
//                                Log.d(TAG, "2-OnComplete")
//                            }
//
//                            override fun onSubscribe(d: Disposable) {
//                                // 无背压处理不用 参数不同，不用做修改 request 值处理
//                                Log.d(TAG, "2-OnSubscribe")
//                            }
//
//                            override fun onNext(t: String) {
//                                Log.d(TAG, "2-OnNext: $t")
//                            }
//
//                            override fun onError(e: Throwable) {
//                                Log.d(TAG, "2-OnError: ${e.printStackTrace()}")
//                            }
//
//                        })
            }
        }

//        btn_ext.setOnClickListener {
//            //            doAny()
////            SharePopupWindow(R.layout.layout_share, this)
////                    .showAtLocation(this.findViewById(R.id.cl_root), Gravity.BOTTOM, 0, 0)
//            ShareBottomDialog.show(this)
//        }
    }

    fun rx3() {

        Observable.create<String> {

        }.doOnSubscribe {

        }.observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<String>() {
                override fun onNext(t: String) {

                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })


        io.reactivex.rxjava3.core.Flowable.create(io.reactivex.rxjava3.core.FlowableOnSubscribe<String> {
            Log.d(TAG, "rx3: ${it.serialize()}")
            it.onNext("Rx3")
        }, io.reactivex.rxjava3.core.BackpressureStrategy.valueOf("rx3"))
            .observeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
            .subscribeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
            .doOnSubscribe {

            }
//            .subscribe(object : DisposableSubscriber<String>() {
//                override fun onNext(t: String?) {
//
//                }
//
//                override fun onError(t: Throwable?) {
//                }
//
//                override fun onComplete() {
//
//                }
//            })
            .subscribe({
                Log.d(TAG, "rx3: onNext: $it")
            }, {
                Log.d(TAG, "rx3: onError: ${it.message}")
            }, {
                Log.d(TAG, "rx3: onComplete")
            })
    }

    var popupWindow: SharePopupWindow? = null

    override fun finish() {
        super.finish()
        Log.d(TAG, "finish: ")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (popupWindow != null) {
            popupWindow == null
        }
    }

    fun doPost(view: View) {

        customObservable?.changeValue("345")
    }


}

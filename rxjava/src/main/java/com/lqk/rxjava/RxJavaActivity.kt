package com.lqk.rxjava

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import rx.Observer
import rx.observables.SyncOnSubscribe

class RxJavaActivity : BaseActivity() {

    companion object {
        const val TAG = "RxJavaActivity"
    }

    var observer = object : CustomObserver<String> {
        override fun changeValue(t: String) {
            Log.d(TAG, "changeValue: 1 $t")
        }
    }

    class MySubscribe<S, T>(s: S) : SyncOnSubscribe<S, T>() {
        var s: S = s
        override fun generateState(): S {
            return s
        }

        override fun next(state: S, observer: Observer<in T>?): S {
            return state
        }
    }

    private lateinit var customObservable: CustomObservable<String>

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


        // RxJava
        rx.Observable.create(MySubscribe<String, String>("123"))
            .map { t -> t?.toInt() ?: -1 }
            .observeOn(rx.schedulers.Schedulers.io()) // 控制上游数据 线程
            .subscribeOn(rx.schedulers.Schedulers.io()) // 控制下游数据线程
            .subscribe()


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


        btn_rxjava1
        btn_rxjava1.setOnClickListener { t ->
            run {
                if (t == null) {
                    return@run
                }
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

        btn_rxjava2.setOnClickListener { t ->
            run {
                if (t == null) {
                    return@run
                }
                //
                Flowable
                    .create(FlowableOnSubscribe<String> {
                        // subscribe
                        if (!it.isCancelled) {
                            it.onNext("data")
                            it.onComplete()
                        }
                    }, BackpressureStrategy.DROP)
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

        btn_rxjava2_flase.setOnClickListener { t ->
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
        io.reactivex.rxjava3.core.Flowable.create(io.reactivex.rxjava3.core.FlowableOnSubscribe<String> {
            Log.d(TAG, "rx3: ${it.serialize()}")
            it.onNext("Rx3")
        }, io.reactivex.rxjava3.core.BackpressureStrategy.valueOf("rx3"))
            .observeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
            .subscribeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "rx3: onNext: $it")
            }, {
                Log.d(TAG, "rx3: onError: ${it.message}")
            }, {
                Log.d(TAG, "rx3: onComplete")
            })
    }

    var popupWindow: SharePopupWindow? = null

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

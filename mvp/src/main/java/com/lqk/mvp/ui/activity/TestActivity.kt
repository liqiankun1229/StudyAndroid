package com.lqk.mvp.ui.activity

//import com.lqk.network.RetrofitUtil
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lqk.mvp.R
import com.lqk.mvp.base.activity.BaseVBActivity
import com.lqk.mvp.bean.Sex
import com.lqk.mvp.bean.User
import com.lqk.mvp.databinding.ActivityTestBinding
import com.lqk.mvp.http.Api
import com.lqk.mvp.http.HttpResponse
import com.lqk.mvp.http.RetrofitHelper
import com.lqk.network.data.HttpDataType
import com.lqk.utils.AppUtil
import com.lqk.utils.KeybordUtil
import com.lqk.utils.RecentListUtil
import com.lqk.utils.SystemUtil
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class TestActivity : BaseVBActivity<ActivityTestBinding>() {

    companion object {
        const val TAG = "Handle"
        const val json =
            "{\"status\": 200, \"message\": \"Login Succeed\", \"data\": {\"name\": \"LQK\", \"mobile\": \"18106899660\",\"sex\":{\"s\":\"男\"}}}"
    }

    @SuppressLint("HandlerLeak")
    private val mainHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.d(TAG, msg?.obj.toString())
        }
    }

    private var btn: Button? = null

    override fun layoutId(): Int {
        return R.layout.activity_test
    }

    override fun initVB(): ActivityTestBinding {
        return ActivityTestBinding.inflate(layoutInflater)
    }

    suspend fun doLogin() {
        withTimeout(1000) {
            return@withTimeout "123"
        }
    }

    @SuppressLint("CheckResult")
    override fun initView() {
        makeStatusBarTransparent(this)
//        pluginRegisterImpl = PluginRegisterImpl(this)
//        pluginRegisterImpl?.onCreate(savedInstanceState)
        KeybordUtil.showSoftInput(this, vb.et)

        btn = findViewById(R.id.btn_commit)

        object : Thread() {
            override fun run() {
                super.run()
                val msg = Message()
                msg.obj = "SendMessage"
                mainHandler.sendMessage(msg)
            }
        }.start()
        vb.btnCommit.setOnClickListener {
            GlobalScope.launch {
                doLogin()
            }


            object : Thread() {
                override fun run() {
                    super.run()
                    ARouter.getInstance().build("/mvp/register").navigation()
//                    tv_msg.text = "Message"
//                mainHandler.post {
//                    tv_msg.text = "Message"
//                }
                }
            }.start()
        }
        vb.btnGetRetrofit.setOnClickListener {
            val sex = Sex("男")
            val user = User("LQK", "18106899660", sex)
            RetrofitHelper.instance
                .initService(Api::class.java)
                .userLogin(user)
                .enqueue(object : Callback<HttpResponse<User>> {
                    override fun onFailure(call: Call<HttpResponse<User>>, t: Throwable) {
                        Toast.makeText(this@TestActivity, "网络请求失败", Toast.LENGTH_SHORT).show()
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<HttpResponse<User>>,
                        response: Response<HttpResponse<User>>
                    ) {
                        val httpResponse = response.body() as HttpResponse
                        val userResult = httpResponse.data ?: return
                        vb.tvData.text =
                            "Retrofit : ${httpResponse.status} + ${userResult.name} + ${userResult.mobile}"
                        Toast.makeText(this@TestActivity, "${vb.tvData.text}", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
        vb.btnGetOkhttp.setOnClickListener {
            val params: HashMap<String, Any> = hashMapOf()
            params["name"] = "LQK"
            params["mobile"] = "18106899660"
            val sex = Sex("男")
            val user = User("LQK", "18106899660", sex)
            RetrofitHelper.instance.initService(Api::class.java)
                .userLoginObservable(user)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .map {
                    return@map it
                }
                .subscribe(
                    {
                        runOnUiThread {
                            Toast
                                .makeText(this@TestActivity, "${it.toString()}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }, {
                        it.printStackTrace()
                    })

        }
        vb.btnGetGson.setOnClickListener {
            KeybordUtil.toggleKeybord(this)
            Log.d("IP", SystemUtil.deviceIpAddress(applicationContext))
            Log.d("APP", "${this.packageName}:${AppUtil.appIsLauncher(this, this.packageName)}")
            Log.d("APP", "${this.packageName}:${AppUtil.isTopActivity(this.packageName, this)}")
            RecentListUtil.getRecentList(applicationContext)
            RecentListUtil.getRecent(this)
            RecentListUtil.recentTaskList()
            val typeToken = object : TypeToken<HttpResponse<User>>() {}.type
            val httpResponse = json2Object<HttpResponse<User>>(json, typeToken)
            vb.tvData.text =
                "Gson : ${httpResponse.status} + ${httpResponse.data?.name} + ${httpResponse.data?.mobile}"
        }
        vb.btnGetJson.setOnClickListener {
            //            val httpResponse = Gson().fromJson<HttpResponse<User>>(json, HttpResponse<User>().javaClass)
//            tv_data.text = "Json : ${httpResponse.status} + ${httpResponse.data?.name} + ${httpResponse.data?.mobile}"

//            val view = Flutter.createView(this, lifecycle, "login")
//            val layout = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
//            addContentView(view, layout)
//            val fl: FlutterFragment = FlutterFragment.withNewEngine().initialRoute("/").build()
//            supportFragmentManager.beginTransaction()
//                    .add(R.id.fl_content, fl)
//                    .show(fl)
//                    .commit()
        }
        vb.tvData.setOnClickListener {
//            NativeCallFlutter.event?.success(tv_data.text)
//            startActivity(Intent(this, ServiceActivity::class.java))
        }
    }

    fun makeStatusBarTransparent(activity: Activity) {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //此处需要release包 不然黑屏
            val option =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun <T> json2Object(data: String, httpDataType: HttpDataType<T>): T {
        val gson = Gson()
        val t = gson.fromJson<T>(data, httpDataType.type)
        return t
    }

    private fun <T : Class<*>> json2Object(data: String): T {
        val gson = Gson()
        val token = object : TypeToken<T>() {}.type
        return gson.fromJson<T>(data, token)
    }

    private fun <T> json2Object(data: String, cls: Class<T>): T {
        val gson = Gson()
        val typeToken = cls.superclass
        return gson.fromJson<T>(data, typeToken)
    }

    private fun <T> json2Object(data: String, type: Type): T {
        val gson = Gson()
        return gson.fromJson(data, type)
    }

    fun okHttpGet() {
//        OkHttpUtil.instance.doGet(
//            BuildConfig.BASE_URL + "/user",
//            hashMapOf(),
//            User::class.java,
//            object : NetWorkCallback<User> {
//                @SuppressLint("SetTextI18n")
//                override fun onSucceed(data: User) {
//                    tv_data.text = "Okhttp : ${data.name} - ${data.mobile}"
//                    Toast.makeText(this@TestActivity, "${tv_data.text}", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onFailed(msg: String) {
//                    Toast.makeText(this@TestActivity, "", Toast.LENGTH_SHORT).show()
//                }
//            })
    }

    fun retrofitGet() {
//        RetrofitUtil.instance.initService(Api::class.java)
//            .getResultData().enqueue(object : Callback<User> {
//                override fun onFailure(call: Call<User>, t: Throwable) {
//                    Toast.makeText(this@TestActivity, "网络请求失败", Toast.LENGTH_SHORT).show()
//                }
//
//                @SuppressLint("SetTextI18n")
//                override fun onResponse(call: Call<User>, response: Response<User>) {
//                    val user = response.body() as User
//                    tv_data.text = "Retrofit : ${user.name} + ${user.mobile}"
//                    Toast.makeText(this@TestActivity, "${tv_data.text}", Toast.LENGTH_SHORT).show()
//                }
//            })
    }

    fun retrofitRxGet() {
//        RetrofitUtil.instance.initService(Api::class.java)
//            .postData<User>()
//            .observeOn(Schedulers.io())
//            .subscribeOn(Schedulers.newThread())
//            .subscribe(object : BaseResponseBean<User>() {
//
//            })


//                .enqueue(object : Callback<User> {
//                    override fun onFailure(call: Call<User>, t: Throwable) {
//                        Toast.makeText(this@TestActivity, "网络请求失败", Toast.LENGTH_SHORT).show()
//                    }
//
//                    @SuppressLint("SetTextI18n")
//                    override fun onResponse(call: Call<User>, response: Response<User>) {
//                        val user = response.body() as User
//                        tv_data.text = "Retrofit : ${user.name} + ${user.mobile}"
//                        Toast.makeText(this@TestActivity, "${tv_data.text}", Toast.LENGTH_SHORT).show()
//                    }
//                })
    }

}

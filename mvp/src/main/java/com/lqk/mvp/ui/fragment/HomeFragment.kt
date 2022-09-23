package com.lqk.mvp.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.lqk.base.BaseVBFragment
import com.lqk.mvp.R
import com.lqk.mvp.bean.FunctionGategoryBean
import com.lqk.mvp.bean.HomeHeadItemBean
import com.lqk.mvp.bean.HomeItemBean
import com.lqk.mvp.bean.ServiceBean
import com.lqk.mvp.databinding.FragmentHomeBinding
import com.lqk.mvp.widget.MyScrollView
import org.jetbrains.anko.textColor


/**
 * 首页
 */
@Route(path = "/mvp/HomeFragment")
class HomeFragment : BaseVBFragment<FragmentHomeBinding>(), MyScrollView.OnScrollMove {

    // 整体数据
    private var homeItemList: MutableList<HomeItemBean>? = null

    // 服务事项 数据
    private var serviceList: MutableList<ServiceBean>? = null

    // 2 * 2 方格内容数据
    private var functionList: MutableList<FunctionGategoryBean>? = null

    // 头部 数据
    private var headList: MutableList<HomeHeadItemBean>? = null

    override fun initLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun initVB(): View {
        return vb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // 加载数据
        initData()
//        // 下拉刷新
//        val refresh = view!!.findViewById<SwipeRefreshLayout>(R.id.srl_home_page) as RefreshLayout
//        refresh.setRefreshHeader(MyRefreshHeader(view.context))
//        refresh.setOnRefreshListener { refreshLayout ->
//            refreshLayout.layout.postDelayed({
//                toast("TT")
//                refreshLayout.finishRefresh()
//            }, 2000)
//        }
//
//        // 滑动修改搜索框位置
//        view.findViewById<MyScrollView>(R.id.msv).setOnScrollView(this)
//
//        view.findViewById<RecyclerView>(R.id.rcv_home_head).layoutManager = GridLayoutManager(this.context, 4)
//        val homeHeadAdapter = HomeHeadAdapter(headList!!)
//        homeHeadAdapter.setOnItemClickListener { _, _, position ->
//            run {
//                toast(headList!![position].name)
//            }
//        }
//        view.findViewById<RecyclerView>(R.id.rcv_home_head).adapter = homeHeadAdapter
//
//        // 搜索框点击事件
//        view.findViewById<TextView>(R.id.tv_search).setOnClickListener {
//            toast("输入搜索内容")
//            startActivity<RegisterActivity>()
//        }
//
//        // 首页整体布局
//        view.findViewById<RecyclerView>(R.id.rcv_home_item).layoutManager = LinearLayoutManager(this.context)
//        val homeAdapter = HomeAdapter(homeItemList!!)
//        homeAdapter.setOnItemClickListener { _, _, position ->
//            run {
//                toast("${homeItemList!![position].type}")
//            }
//        }
//        view.findViewById<RecyclerView>(R.id.rcv_home_item).adapter = homeAdapter

        return view
    }

    var list = mutableListOf(1, 2, 3, 4, 5, 6)

    fun setData(list: ArrayList<Int>) {
        this.list = list
        initData()
    }

    override fun initData() {

        headList = mutableListOf(
            HomeHeadItemBean("一码通称", "", R.drawable.qrcode),
            HomeHeadItemBean("扫一扫", "", R.drawable.sweep),
            HomeHeadItemBean("智能客服", "", R.drawable.ai_service),
            HomeHeadItemBean("办事指南", "", R.drawable.guide)
        )

        serviceList = initList(
            mutableListOf(
                ServiceBean("公积金", "", R.drawable.accumulation_und),
                ServiceBean("小客车摇号", "", R.drawable.accumulation_und),
                ServiceBean("交通出行", "", R.drawable.bus),
                ServiceBean("社保", "", R.drawable.umbrella),
                ServiceBean("福利彩票", "", R.drawable.lottery),
                ServiceBean("居住登记", "", R.drawable.registration),
                ServiceBean("房屋产权", "", R.drawable.property_rights),
                ServiceBean("出入境", "", R.drawable.entry),
                ServiceBean("教育缴费", "", R.drawable.book),
                ServiceBean("钱包查询", "", R.drawable.entry),
                ServiceBean("市民卡", "", R.drawable.entry),
                ServiceBean("水务", "", R.drawable.property_rights)
            )
        )

        functionList = mutableListOf(
            FunctionGategoryBean("我要办理", "登记审批", "", R.drawable.transaction),
            FunctionGategoryBean("我要缴费", "充值|收缴|罚款", "", R.drawable.pay),
            FunctionGategoryBean("我要查询", "凭证下载", "", R.drawable.query),
            FunctionGategoryBean("我要预约", "线上预约|线下办理", "", R.drawable.date)
        )

        val homeItemBean1 = HomeItemBean()
        homeItemBean1.type = list[0]
        homeItemBean1.serviceList = serviceList
        homeItemBean1.functionList = functionList

        val homeItemBean2 = HomeItemBean()
        homeItemBean2.type = list[1]
        homeItemBean2.serviceList = serviceList
        homeItemBean2.functionList = functionList

        val homeItemBean3 = HomeItemBean()
        homeItemBean3.type = list[2]
        homeItemBean3.serviceList = serviceList
        homeItemBean3.functionList = functionList

        val homeItemBean4 = HomeItemBean()
        homeItemBean4.type = list[3]
        homeItemBean4.serviceList = serviceList
        homeItemBean4.functionList = functionList

        val homeItemBean5 = HomeItemBean()
        homeItemBean5.type = list[4]
        homeItemBean5.serviceList = serviceList
        homeItemBean5.functionList = functionList

        val homeItemBean6 = HomeItemBean()
        homeItemBean6.type = list[5]
        homeItemBean6.serviceList = serviceList
        homeItemBean6.functionList = functionList

        homeItemList = mutableListOf(
            homeItemBean1,
            homeItemBean2,
            homeItemBean3,
            homeItemBean4,
            homeItemBean5,
            homeItemBean6
        )
    }

    private fun initList(list: MutableList<ServiceBean>): MutableList<ServiceBean> {
        while (list.size > 9) {
            list.removeAt(list.size - 1)
        }
        list.add(ServiceBean("全部", "", R.drawable.all))
        return list
    }


    @SuppressLint("ResourceAsColor")
    override fun onChange(l: Int, t: Int, oldl: Int, oldt: Int) {
        if (t == 0) {
            return
        }
//        var top = vb.head.height - (vb.llSearch.height / 2) - t
        var top = 0
        // 获取状态栏高度
        var height = 0
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = this.resources.getDimensionPixelSize(resourceId)
        }
//        if (top > vb.head.height - (vb.llSearch.height / 2)) {
//            top = vb.head.height - (vb.llSearch.height / 2)
//        } else if (top < height) {
//            top = height
//        }
        if (top == height) {
            // 首页头部收起来的效果
            if (Build.VERSION.SDK_INT >= 21) {
                val decorView = requireActivity().window.decorView
                val option = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE)
                decorView.systemUiVisibility = option
                requireActivity().window.navigationBarColor = resources.getColor(R.color.colorPrimary)
                requireActivity().window.statusBarColor = resources.getColor(R.color.colorPrimary)
            }
            vb.llSearchBackground.background = resources.getDrawable(R.drawable.bg_hide_search)
            vb.llSearchBackground.gravity = Gravity.CENTER
            vb.llSearchBackground.setPadding(0, 5, 0, 5)
            vb.tvSearch.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(R.drawable.search_white),
                null, null, null
            )
            vb.tvSearch.textColor = Color.WHITE
            vb.tvSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)


        } else {
            // 首页头部展开效果
            if (Build.VERSION.SDK_INT >= 21) {
                val decorView = requireActivity().window.decorView
                val option = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE)
                decorView.systemUiVisibility = option
                requireActivity().window.navigationBarColor = resources.getColor(R.color.colorPrimary)
                requireActivity().window.statusBarColor = resources.getColor(R.color.colorTransParent)
            }
            vb.llSearchBackground.background = resources.getDrawable(R.drawable.bg_search)
            vb.llSearchBackground.gravity = Gravity.CENTER_VERTICAL
            vb.llSearchBackground.setPadding(10, 10, 10, 10)
            vb.tvSearch.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(R.drawable.search),
                null, null, null
            )
            vb.tvSearch.textColor = Color.parseColor("#999999")
            vb.tvSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }

        val layoutParams: FrameLayout.LayoutParams =
            vb.llSearch.layoutParams as FrameLayout.LayoutParams
        layoutParams.setMargins(0, top, 0, 0)
        vb.llSearch.background = resources.getDrawable(R.color.colorPrimary)
        // 修改透明度
//        val alpha = 255 - (top / (vb.head.height.toDouble() - (vb.llSearch.height / 2).toDouble()) * 255)
//        vb.llSearch.background.alpha = alpha.toInt()
//        vb.llSearch.layoutParams = layoutParams
    }
}

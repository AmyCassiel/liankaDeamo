package com.lianka.liankadeamo.mian

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import butterknife.BindView
import butterknife.ButterKnife
import com.lianka.liankadeamo.R
import com.lianka.liankadeamo.classifypage.ClassifyFragment
import com.lianka.liankadeamo.homepage.HomePageFragment
import com.lianka.liankadeamo.mallpage.MallPageFragment
import com.lianka.liankadeamo.mian.adapter.BottomIconAdapter
import com.lianka.liankadeamo.mian.adapter.FragmentPageAdapter
import com.lianka.liankadeamo.mypage.MyFragment
import com.lianka.liankadeamo.shoppingcartpage.ShoppingCartFragment
import com.lianka.liankadeamo.utils.StatusBarUtil
import java.util.*

class MainActivity : FragmentActivity() {
    @BindView(R.id.viewpager)
    var viewpager: ViewPager? = null
    @BindView(R.id.rv)
    var rv: RecyclerView? = null
    private val TAG = MainActivity::class.java.simpleName
    private val fragments = ArrayList<Fragment>()
    private var iPosition = 0
    private var rvAdapter :BottomIconAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this@MainActivity,R.color.white_fff)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initFragment()
    }

    /**
     * 初始化Fragment
     */
    private fun initFragment() {
        fragments.clear() //清空
        val homePageFragment = HomePageFragment()
        val mallPageFragment = MallPageFragment()
        val classifyFragment = ClassifyFragment()
        val shoppingCartFragment = ShoppingCartFragment()
        val myFragment = MyFragment()
        fragments.add(homePageFragment)
        fragments.add(mallPageFragment)
        fragments.add(classifyFragment)
        fragments.add(shoppingCartFragment)
        fragments.add(myFragment)
        val mAdapter = FragmentPageAdapter(supportFragmentManager, fragments)
        viewpager!!.adapter = mAdapter
        viewpager!!.addOnPageChangeListener(pageListener)
        viewpager!!.offscreenPageLimit = 5
    }

    /**
     * ViewPager切换监听方法
     */
    var pageListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrollStateChanged(arg0: Int) {}
        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageSelected(position: Int) {
            viewpager!!.currentItem = position
            selectTab(position)
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private fun selectTab(tab_position: Int) {
        iPosition = tab_position
        if (tab_position == 4) {
            StatusBarUtil.setStatusBarColor(this@MainActivity,R.color.white_fff)
        }
        rvAdapter?.notifyDataSetChanged()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                Log.d(TAG, "onKeyDown KEYCODE_BACK")
                showDialog()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showDialog() {
        TODO("Not yet implemented")
    }
}
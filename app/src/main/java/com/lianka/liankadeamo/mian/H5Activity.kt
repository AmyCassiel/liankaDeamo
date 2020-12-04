package com.lianka.liankadeamo.mian

import android.content.Context
import android.os.Bundle
import com.lianka.liankadeamo.R
import com.lianka.liankadeamo.base.OnBaseActivity

class H5Activity : OnBaseActivity(){
    private lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

    }
    override fun init() {
        TODO("Not yet implemented")
    }

    override fun getContentView(): Int {
        return R.layout.activity_h5
    }

    override fun setBarBackground(): Int {
        TODO("Not yet implemented")
    }

    override fun setTitle(): Int {
        TODO("Not yet implemented")
    }
}
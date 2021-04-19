package com.tapi.android.example

import android.os.Bundle
import android.view.KeyEvent
import com.tapi.android.example.event.OnCallBackToFragment
import com.tapi.android.example.functions.bases.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mCallback: OnCallBackToFragment

    fun setOnCallBack(event: OnCallBackToFragment) {
        mCallback = event
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            mCallback.backPressToFrg()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
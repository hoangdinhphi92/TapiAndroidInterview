package com.tapi.android.example.functions.bases

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope


abstract class BaseActivity : AppCompatActivity() {


    fun sendAction(actionName: String, data: Any?) {
        lifecycleScope.launchWhenResumed {
            if (!onActionReceived(actionName, data)) {
                supportFragmentManager.fragments.forEach {
                    sendActionToFragment(it, actionName, data)
                }
            }
        }
    }

    protected open fun onActionReceived(actionName: String, data: Any?): Boolean {
        return false
    }

    private fun sendActionToFragment(fragment: Fragment, actionName: String, data: Any?) {
        if (fragment is BaseFragment) {
            if (fragment.onActionReceived(actionName, data)) {
                return
            }
        }
        fragment.childFragmentManager.fragments.forEach {
            sendActionToFragment(it, actionName, data)
        }
    }

}
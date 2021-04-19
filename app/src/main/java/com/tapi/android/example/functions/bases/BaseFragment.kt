package com.tapi.android.example.functions.bases

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tapi.android.example.MainActivity

abstract class BaseFragment : Fragment() {

    fun getMainActivity(): MainActivity? {
        if (activity != null && activity is MainActivity) {
            return activity as MainActivity
        }
        return null
    }

     fun getBaseActivity(): BaseActivity? {
        if (activity != null && activity is BaseActivity) {
            return activity as BaseActivity
        }
        return null
    }
    internal open fun onActionReceived(actionName: String, data: Any?): Boolean {
        return false
    }


    protected fun sendAction(actionName: String, data: Any? = null) {
        lifecycleScope.launchWhenResumed {
            getBaseActivity()?.sendAction(actionName, data)
        }
    }

}
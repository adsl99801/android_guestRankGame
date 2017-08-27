package com.lfo.guess

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics





/**
 * Created by home on 2017/8/27.
 */
class MApplication :Application() {
    lateinit var  mFirebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

//        val bundle = Bundle()
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}
package com.lfo.guess

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics



/**
 * Created by home on 2017/8/27.
 */
class GoogleHandler private constructor(){
    private lateinit var  mFirebaseAnalytics: FirebaseAnalytics
    private object Handler{
        val INSTANCE=GoogleHandler()
    }
    companion object {
        val instance:GoogleHandler by lazy {
            Handler.INSTANCE

        }
    }
    fun init( context: Context){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }
    fun sendAnalytics(id:String,name:String,type:String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type)
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }


}
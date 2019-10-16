package com.kawa1lg1rl.lotto.data

import android.content.Context
import android.content.SharedPreferences
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R


class MySharedPreferences (prefNameId : Int) {
    var context: Context? = null
    var prefs: SharedPreferences? = null
    init{
        context = App.context()
        prefs = context!!.getSharedPreferences(context!!.resources.getString(prefNameId),0)
    }


    fun addStrings(name : String, value : Array<Int>) {
        prefs!!.edit().putStringSet(name, value.map { it.toString() }.toMutableSet()).apply()
    }

    fun removeStrings(name:String) {
        prefs!!.edit().remove(name).apply()
    }

    fun clearStrings() {
        prefs!!.edit().clear().apply()
    }

    fun getStrings(name: String) : Array<String> {
        var list: Array<String>

        list = (prefs!!.getStringSet(name, null).map {
            it.toString()
        }).toTypedArray()

        return list
    }

    fun getAllSet() : Map<String, *> {
        return prefs!!.all
    }

    fun getAllKey() : Array<String> {
        var all = prefs!!.all
        var list : Array<String> = arrayOf()

        for (entry in all.entries) {
            list.plus(entry.key)
        }

        return list
    }
}


package com.kawa1lg1rl.lotto.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R


class MySharedPreferences (prefNameId : Int) {
    var context: Context? = null
    lateinit var prefs: SharedPreferences
    init{
        context = App.context()
        prefs = context!!.getSharedPreferences(context!!.resources.getString(prefNameId),0)
    }

    fun addString(name: String, value: String) {
        prefs.edit().putString(name, value).apply()
    }

    fun removeString(name: String) {
        prefs.edit().remove(name).apply()
    }


    fun addStrings(name : String, value : Array<Int>) {
        prefs.edit().putStringSet(name, value.map { it.toString() }.toMutableSet()).apply()
    }



    fun removeStrings(name:String) {
        prefs.edit().remove(name).apply()
    }

    fun clearStrings() {
        prefs.edit().clear().apply()
    }

    fun getString(name:String) : String {
        return prefs.getString(name, "")
    }

    fun getStrings(name: String) : Array<String> {
        var list: Array<String>

        list = (prefs.getStringSet(name, null).map {
            it.toString()
        }).toTypedArray()

        return list
    }

    fun getAllSet() : Map<String, *> {
        return prefs.all
    }

    fun getAllKey() : Array<String> {
        var all = prefs.all
        var list : Array<String> = arrayOf()

        for (entry in all.entries) {
            list.plus(entry.key)
        }

        return list
    }
}


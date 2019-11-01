package com.kawa1lg1rl.lotto.utils

import android.util.Log
import com.kawa1lg1rl.lotto.data.BoughtLottoNumbers
import com.kawa1lg1rl.lotto.data.RealmBoughtLottoNumbers
import io.realm.Realm
import io.realm.kotlin.where

/**
 * Created by kawa1lg1rl on 2019-11-01
 */

class RealmUtils  {

    fun insertBoughtNumbers(item: BoughtLottoNumbers, key: (Int) -> Unit) {
        Log.d("kawa1lg1rl_realm", "##### insertMyBarcodeRealm #####")
        val defaultRealm = Realm.getDefaultInstance()
        defaultRealm.executeTransaction {realmTransaction ->
            val currentKey = realmTransaction.where<RealmBoughtLottoNumbers>().max("key")
            val nextKey = currentKey?.toInt()?.plus(1) ?: 1

            realmTransaction.createObject(RealmBoughtLottoNumbers::class.java, nextKey).apply {
                count = item.count
                numbers.addAll(item.lottoNumbers)
            }
            key(nextKey)
        }
    }

    fun readBoughtNumbers(): MutableList<RealmBoughtLottoNumbers> {
        Log.d("kawa1lg1rl_realm", "##### readMyBarcodeRealm #####")
        val defaultRealm = Realm.getDefaultInstance()
        defaultRealm.where<RealmBoughtLottoNumbers>().sort("key").findAll()
            .let { realmResult ->
                return defaultRealm.copyFromRealm(realmResult)
            }
    }

    fun deleteMyBarcodeRealm(key: Int, isSuccess: (Boolean) -> Unit) {
        Log.d("kawa1lg1rl_realm", "##### deleteMyBarcodeRealm ##### key : ${key}")
        val defaultRealm = Realm.getDefaultInstance()

        defaultRealm.executeTransactionAsync({ realmTransaction ->
            realmTransaction.where<RealmBoughtLottoNumbers>().equalTo("key", key).findFirst().let { targetColumn ->
                targetColumn?.deleteFromRealm()
            }
            isSuccess(true)
        }, {

        }, {
            isSuccess(false)
        })
    }
}
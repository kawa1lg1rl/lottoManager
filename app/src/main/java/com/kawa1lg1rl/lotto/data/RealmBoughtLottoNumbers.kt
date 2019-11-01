package com.kawa1lg1rl.lotto.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by kawa1lg1rl on 2019-11-01
 */


open class RealmBoughtLottoNumbers : RealmObject() {
    @PrimaryKey
    open var key : Int = 0
    open var count : Int = 0
    open var numbers : RealmList<Int> = RealmList()

}
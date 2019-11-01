package com.kawa1lg1rl.lotto.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by kawa1lg1rl on 2019-10-22
 */

data class BoughtLottoNumbers (var lottoNumbers: Array<Int>, var count: Int)
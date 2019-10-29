package com.kawa1lg1rl.lotto.activity.fragment

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kawa1lg1rl.lotto.adapter.StatNumbersAdapter
import com.kawa1lg1rl.lotto.network.RequestLottoResult
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7._RecyclerView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.ctx

/**
 * Created by kawa1lg1rl on 2019-10-29
 */

class StatNumberFragment : Fragment() {

    lateinit var myRecyclerView : RecyclerView
    val items by lazy {
        RequestLottoResult.instance.getNumbersStat(0, 0, 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = UI {

        val main = verticalLayout {
            linearLayout {
                weightSum = 4f
                lparams(weight = 1f, width = matchParent, height = 0)
                val startEditText = editText ("1") {
                    inputType = InputType.TYPE_CLASS_NUMBER
                }.lparams(weight = 0.5f, height = matchParent, width = 0)

                val endEditText = editText ( RequestLottoResult.currentResult.count.toString() ) {
                    inputType = InputType.TYPE_CLASS_NUMBER
                }.lparams(weight = 0.5f, height = matchParent, width = 0)

                val isContainSecondNumber = spinner {
                    adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, listOf("포함", "미포함"))
                }.lparams(weight = 2f, height = matchParent, width = 0)

                button("조회") {

                }.lparams(weight = 1f, height = matchParent, width = 0)
                    .onClick {
                        val startCount = startEditText.text.toString().toInt()
                        val endCount = endEditText.text.toString().toInt()
                        val isSecond = isContainSecondNumber.selectedItem

                        Log.d("kawa1lg1rl_tag isSecond", isSecond.toString())

                        setStatView(startCount, endCount, if(isSecond.toString() == "포함") 1 else 0)
                    }
            }

            myRecyclerView = recyclerView {
                layoutManager = LinearLayoutManager(ctx)

            }.lparams(weight = 4f, width = matchParent, height = 0)
        }

        initStatView()
    }.view

    fun setStatView(startCount : Int, endCount : Int, bonus : Int) {
        var stats = RequestLottoResult.instance.getNumbersStat(startCount, endCount, bonus)

        stats.mapIndexed { index, s ->
            items.set(index, s)
        }

        myRecyclerView.adapter?.notifyDataSetChanged()
    }

    fun initStatView() {
        myRecyclerView.adapter = StatNumbersAdapter(myRecyclerView.context, items)
    }
}
package com.kawa1lg1rl.lotto.adapter

import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.item.LottoNumbersItem
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.data.MySharedPreferences
import org.jetbrains.anko.*


class SavedLottoNumbersAdapterAnko(val context: Context, var itemList:List<LottoNumbersItem>,
                                   var countItem: List<String>) :
    RecyclerView.Adapter<SavedLottoNumbersAdapterAnko.NumbersHolder>() {

    // 확장함수
    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumbersHolder {
        return NumbersHolder(
            BoughtNumbersRecyclerView().createView(AnkoContext.create(parent.context, parent))
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: NumbersHolder, position: Int) {
        var item = itemList[position]

        holder.countText.setText(countItem[position])

        holder.numbers.mapIndexed { index, imageView ->
            imageView.loadImage(
                context.resources.getIdentifier(
                    "number_${item.numbers!![index]}",
                    "drawable",
                    context.packageName
                )
            )

            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }

        holder.removeButton.setOnClickListener {
            MySharedPreferences(R.string.prefsBoughtNumbers).removeStrings(item.name!!)
            MySharedPreferences(R.string.prefsBoughtNumbersCount).removeStrings(item.name!!)

            itemList -= item
            this.notifyDataSetChanged()
        }
    }


    inner class NumbersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var numbers: Array<ImageView> = itemView.find<LinearLayout>(linear_images).let {
            var tempArray: Array<ImageView> = emptyArray()
            var i = 0
            while(i < it.childCount) {
                Log.d("kawa1lg1rl_tag", "loop : $i, ${it.getChildAt(i).toString()}")
                if( it.getChildAt(i) !is ImageView ) {
                    i += 1
                    continue
                }
                tempArray = tempArray.plus(it.getChildAt(i) as ImageView)
                i += 1
            }
            tempArray
        }

        var removeButton : Button = itemView.find(bt_remove)
        var countText : TextView = itemView.find(tv_count)
    }

    var tv_count = View.generateViewId()
    var iv_arrays: Array<Int> = Array(6, { View.generateViewId() } )
    var linear_images = View.generateViewId()
    var bt_remove = View.generateViewId()

    inner class BoughtNumbersRecyclerView : AnkoComponent<ViewGroup> {

        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = with(ui) {
                verticalLayout {
                    lparams(width = matchParent, height = dip(100))

                    textView {
                        id = tv_count
                    }.lparams {
                        width = matchParent
                        height = dip(30)
                    }

                    linearLayout {
                        lparams(width = matchParent, height = dip(70))
                        id = linear_images

                        for(i in 0..5) {
                            imageView {
                                id = iv_arrays[i]
                            }.lparams {
                                weight = 1f
                                height = matchParent
                                width = dip(0)
                            }
                        }

                        button("지우기") {
                            id = bt_remove
                        }.lparams {
                            weight = 1f
                            height = matchParent
                            width = dip(0)
                        }

                    }
                }
            }

            return itemView
        }


    }
}
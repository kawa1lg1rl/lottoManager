package com.kawa1lg1rl.lotto.adapter

import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.item.LottoNumbersItem
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.data.BoughtLottoNumbers
import com.kawa1lg1rl.lotto.data.MySharedPreferences
import com.kawa1lg1rl.lotto.network.RequestLottoResult
import com.kawa1lg1rl.lotto.utils.LottoResultUtils
import org.jetbrains.anko.*


class SavedLottoNumbersAdapterAnko(val context: Context, var itemList:List<BoughtLottoNumbers>,
                                   var keys: List<Int>) :
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

        holder.countText.setText("${item.count}회 당첨 확인하려면 터치")

        holder.numbers.mapIndexed { index, imageView ->
            imageView.loadImage(
                context.resources.getIdentifier(
                    "number_${item.lottoNumbers!![index]}",
                    "drawable",
                    context.packageName
                )
            )

            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }

        holder.totalView.setOnClickListener {
            var (myWinnings, winnings, rank) = LottoResultUtils.instance.isWinning(item)

            Toast.makeText(context, myWinnings.joinToString(separator = ",") + " 당첨번호는 " + winnings.joinToString(separator = "/") + " 내 순위는 " + rank, Toast.LENGTH_SHORT).show()
        }

        holder.removeButton.setOnClickListener {
            LottoResultUtils.instance.removeBoughtNumbers(keys[position])
            itemList -= item
            this.notifyDataSetChanged()
        }

    }


    inner class NumbersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var numbers: Array<ImageView> = itemView.find<LinearLayout>(linear_images).let {
            var tempArray: Array<ImageView> = emptyArray()
            var i = 0
            while(i < it.childCount) {
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
        var totalView : LinearLayout = itemView.find(linear)
    }

    var tv_count = View.generateViewId()
    var iv_arrays: Array<Int> = Array(6, { View.generateViewId() } )
    var linear_images = View.generateViewId()
    var bt_remove = View.generateViewId()
    var linear = View.generateViewId()

    // Anko를 이용한 Recycler View
    inner class BoughtNumbersRecyclerView : AnkoComponent<ViewGroup> {

        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = with(ui) {
                verticalLayout {
                    id = linear
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
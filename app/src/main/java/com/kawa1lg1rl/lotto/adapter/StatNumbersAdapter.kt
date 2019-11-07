package com.kawa1lg1rl.lotto.adapter

import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*

class StatNumbersAdapter(val context: Context, var itemList:Array<String>) :
    RecyclerView.Adapter<StatNumbersAdapter.StatNumbersHolder>() {

    // 확장함수
    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatNumbersHolder {
        return StatNumbersHolder(
            StatNumbersView().createView(AnkoContext.create(parent.context, parent))
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: StatNumbersHolder, position: Int) {
        var item = itemList[position]

        holder.count.loadImage(context.resources.getIdentifier("number_${position + 1}", "drawable", context.packageName))
        holder.stat.setText(item + "회")

    }

    inner class StatNumbersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val count : ImageView = itemView.findViewById(countView)
        val stat : TextView = itemView.findViewById(statView)
    }

    val countView = View.generateViewId()
    val statView = View.generateViewId()

    // Anko를 이용한 Recycler View
    inner class StatNumbersView : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            verticalLayout {
                lparams(height = dip(30), width = matchParent)

                linearLayout {
                    lparams(weight = 0.99f, height = 0, width = matchParent)
                    imageView {
                        id = countView
                    }.lparams(weight = 1f, height = matchParent, width = 0)

                    textView {
                        id = statView
                        gravity = Gravity.CENTER
                    }.lparams(weight = 1f, height = matchParent, width = 0)
                }

                view {
                    backgroundColor = Color.BLACK
                }.lparams(weight = 0.01f, height = 0, width = matchParent)
            }
        }
    }

}
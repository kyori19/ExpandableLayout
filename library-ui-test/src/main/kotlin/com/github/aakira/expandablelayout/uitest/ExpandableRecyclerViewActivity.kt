package com.github.aakira.expandablelayout.uitest

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aakira.expandablelayout.ExpandableLayout
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter
import com.github.aakira.expandablelayout.ExpandableLinearLayout
import com.github.aakira.expandablelayout.Utils
import kotlinx.android.synthetic.main.activity_expandable_recycler_view.*
import java.util.*

class ExpandableRecyclerViewActivity : AppCompatActivity() {

    companion object {
        const val DURATION = 200L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_recycler_view)
        supportActionBar?.title = ExpandableRelativeLayoutActivity3::class.java.simpleName

        val colors = arrayListOf(
                R.color.material_red_500 to R.color.material_red_300,
                R.color.material_pink_500 to R.color.material_pink_300,
                R.color.material_purple_500 to R.color.material_purple_300,
                R.color.material_deep_purple_500 to R.color.material_deep_purple_300,
                R.color.material_indigo_500 to R.color.material_indigo_300,
                R.color.material_blue_500 to R.color.material_blue_300,
                R.color.material_light_blue_500 to R.color.material_light_blue_300,
                R.color.material_cyan_500 to R.color.material_cyan_300,
                R.color.material_cyan_500 to R.color.material_cyan_300
        )
        val texts = arrayListOf(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" +
                        "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
                "cccccccccccccccccccccccccccccccccccccccccccccccccccccccc" +
                        "cccccccccccccccccccccccccccccccccccccccccccccccccccccccc" +
                        "cccccccccccccccccccccccccccccccccccccccccccccccccccccccc",
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
        )
        val data = ArrayList<ItemModel>()
        IntRange(0, 100).forEach {
            val color = colors[it % colors.size]
            data.add(ItemModel(" ------------------ $it ------------------",
                    texts[it % texts.size], color.first, color.second))
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(data)
    }

    data class ItemModel(val title: String, val description: String, var colorId1: Int, val colorId2: Int)

    class RecyclerViewAdapter(private val data: List<ItemModel>) : RecyclerView.Adapter<RecyclerViewAdapter.ExpandableViewHolder>() {
        private lateinit var context: Context
        private val expandState = SparseBooleanArray()

        init {
            for (i in data.indices) {
                expandState.append(i, false)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder {
            this.context = parent.context
            return ExpandableViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_list_row, parent, false))
        }

        override fun onBindViewHolder(holder: ExpandableViewHolder, position: Int) {
            val item = data[position]
            holder.setIsRecyclable(false)
            holder.title.text = item.title
            holder.description.text = item.description
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, item.colorId1))
            holder.expandableLayout.setInRecyclerView(true)
            holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, item.colorId2))
            holder.expandableLayout.setInterpolator(LinearInterpolator())
            holder.expandableLayout.isExpanded = expandState.get(position)
            holder.expandableLayout.setListener(object : ExpandableLayoutListenerAdapter() {
                override fun onPreOpen() {
                    createRotateAnimator(holder.buttonLayout, 0f, 180f).start()
                    expandState.put(position, true)
                }

                override fun onPreClose() {
                    createRotateAnimator(holder.buttonLayout, 180f, 0f).start()
                    expandState.put(position, false)
                }
            })

            holder.buttonLayout.rotation = if (expandState.get(position)) 180f else 0f
            holder.buttonLayout.setOnClickListener { onClickButton(holder.expandableLayout) }
        }

        private fun onClickButton(expandableLayout: ExpandableLayout) {
            expandableLayout.toggle()
        }

        override fun getItemCount(): Int {
            return data.size
        }

        class ExpandableViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var title: TextView = v.findViewById(R.id.titleText) as TextView
            var description: TextView = v.findViewById(R.id.descriptionText) as TextView
            var buttonLayout: RelativeLayout = v.findViewById(R.id.button) as RelativeLayout
            var expandableLayout: ExpandableLinearLayout = v.findViewById(R.id.expandableLayout) as ExpandableLinearLayout
        }

        fun createRotateAnimator(target: View, from: Float, to: Float): ObjectAnimator {
            val animator = ObjectAnimator.ofFloat(target, "rotation", from, to)
            animator.duration = DURATION
            animator.interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
            return animator
        }
    }
}

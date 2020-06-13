package com.github.aakira.expandablelayout.uitest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_expandable_linear_layout2.*

class ExpandableLinearLayoutActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_linear_layout2)
        supportActionBar?.title = ExpandableLinearLayoutActivity::class.java.simpleName

        expandButton.setOnClickListener { expandableLayout.toggle() }
        moveChildButton.setOnClickListener { expandableLayout.moveChild(1) }
        moveChildButton2.setOnClickListener { expandableLayout.moveChild(2) }
        moveTopButton.setOnClickListener { expandableLayout.move(0) }
        setCloseHeightButton.setOnClickListener {
            expandableLayout.closePosition = expandableLayout.currentPosition
        }
    }
}

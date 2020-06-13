package com.github.aakira.expandablelayout.uitest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_expandable_relative_layout_2.*

class ExpandableRelativeLayoutActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_relative_layout_2)
        supportActionBar?.title = ExpandableRelativeLayoutActivity2::class.java.simpleName

        expandButton.setOnClickListener { expandableLayout.toggle() }
        moveChildButton.setOnClickListener { expandableLayout.moveChild(1) }
    }
}

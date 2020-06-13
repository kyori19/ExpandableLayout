package com.github.aakira.expandablelayout.uitest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_expandable_weight_layout.*

class ExpandableWeightLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_weight_layout)
        supportActionBar?.title = ExpandableWeightLayoutActivity::class.java.simpleName

        expandButton.setOnClickListener { expandableLayout.toggle() }
        moveWeightButton.setOnClickListener { expandableLayout.move(2f, 0, null) }
    }
}

package com.github.aakira.expandablelayout.uitest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_expandable_linear_layout3.*
import rx.subscriptions.CompositeSubscription

/**
 * test for [com.github.aakira.expandablelayout.ExpandableLinearLayout#initlayout]
 *
 * The default value is  {@link android.view.animation.AccelerateDecelerateInterpolator}
 *
 */
class ExpandableLinearLayoutActivity3 : AppCompatActivity() {

    private val subscriptions: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_linear_layout3)
        supportActionBar?.title = ExpandableLinearLayoutActivity::class.java.simpleName

        expandButton.setOnClickListener { expandableLayout.toggle() }
        moveChildButton.setOnClickListener { expandableLayout.moveChild(0) }
        moveChildButton2.setOnClickListener { expandableLayout.moveChild(1) }

        // uncomment if you want to check the #ExpandableLayout.initLayout()
//        val child1 = findViewById(R.id.child1) as TextView
//        subscriptions.add(Observable.timer(5, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    child1.text =
//                            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                    expandableLayout.initLayout()
//                    expandableLayout.expand(0, null)
//                })
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }
}

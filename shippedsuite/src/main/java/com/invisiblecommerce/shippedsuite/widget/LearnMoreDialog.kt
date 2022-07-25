package com.invisiblecommerce.shippedsuite.widget

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.invisiblecommerce.shippedsuite.R

/**
`LearnMoreDialog` is a dialog to show the learn more page.
 */
class LearnMoreDialog internal constructor(context: Context) :
    AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_learn_more)
        findViewById<Button>(R.id.shipped_action_next)?.setOnClickListener { dismiss() }
    }

    companion object {
        fun show(context: Context) {
            val dialog = LearnMoreDialog(context)
            dialog.show()
            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }
    }
}

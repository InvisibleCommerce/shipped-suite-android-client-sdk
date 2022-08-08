package com.invisiblecommerce.shippedsuite.widget

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.invisiblecommerce.shippedsuite.R
import com.invisiblecommerce.shippedsuite.databinding.LearnMoreDialogBinding

/**
`LearnMoreDialog` is a dialog to show the learn more page.
 */
class LearnMoreDialog internal constructor(context: Context) :
    AppCompatDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(binding.getRoot())
        findViewById<Button>(R.id.shipped_done)?.setOnClickListener { dismiss() }
    }

    var offers: WidgetViewOffers = WidgetViewOffers.GREEN
        set(value) {
            field = value
            binding.shippedLogo.setImageDrawable(value.learnMoreLogo(context))
            binding.shippedTitle.text = value.learnMoreTitle(context)
            binding.shippedSubtitle.text = value.learnMoreSubtitle(context)
            binding.shippedTipInfoOne.text = value.learnMoreTip0(context)
            binding.shippedTipInfoTwo.text = value.learnMoreTip1(context)
            binding.shippedTipInfoThree.text = value.learnMoreTip2(context)
            if (value == WidgetViewOffers.SHIELD) {
                binding.banner.visibility = View.GONE
                binding.shippedBottom.setBackgroundColor(context.resources.getColor(R.color.light_gray))
            } else {
                binding.banner.visibility = View.VISIBLE
                binding.shippedBottom.setBackgroundColor(context.resources.getColor(R.color.white))
            }
            val params = binding.banner.layoutParams
            if (value == WidgetViewOffers.GREEN) {
                params.height = context.resources.getDimension(R.dimen.green_banner_height).toInt()
                binding.banner.setOnClickListener {
                    Log.d("", "Banner is clicked.")
                }
            } else if (value == WidgetViewOffers.GREEN_AND_SHIELD) {
                params.height =
                    context.resources.getDimension(R.dimen.green_shield_banner_height).toInt()
            }
            binding.banner.layoutParams = params
            binding.banner.setImageDrawable(value.learnMoreBanner(context))
        }

    private val binding by lazy {
        LearnMoreDialogBinding.inflate(LayoutInflater.from(context))
    }

    companion object {
        fun show(context: Context, offers: WidgetViewOffers) {
            val dialog = LearnMoreDialog(context)
            dialog.offers = offers
            dialog.show()
            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }
    }
}

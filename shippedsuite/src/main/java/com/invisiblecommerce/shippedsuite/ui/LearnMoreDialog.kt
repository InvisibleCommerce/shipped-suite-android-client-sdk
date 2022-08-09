package com.invisiblecommerce.shippedsuite.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
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

        binding.reportAnIssue.setOnClickListener {
            if (type == ShippedSuiteType.GREEN) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DOWNLOAD_SHIPPED_URL))
                startActivity(context, intent, null)
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(REPORT_AN_ISSUE_URL))
                startActivity(context, intent, null)
            }
        }
        binding.termsOfService.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_OF_SERVICE_URL))
            startActivity(context, intent, null)
        }
        binding.privacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL))
            startActivity(context, intent, null)
        }
    }

    var type: ShippedSuiteType = ShippedSuiteType.GREEN
        set(value) {
            field = value
            binding.shippedLogo.setImageDrawable(value.learnMoreLogo(context))
            binding.shippedTitle.text = value.learnMoreTitle(context)
            binding.shippedSubtitle.text = value.learnMoreSubtitle(context)
            binding.shippedTipInfoOne.text = value.learnMoreTip0(context)
            binding.shippedTipInfoTwo.text = value.learnMoreTip1(context)
            binding.shippedTipInfoThree.text = value.learnMoreTip2(context)
            if (value == ShippedSuiteType.SHIELD) {
                binding.banner.visibility = View.GONE
                binding.shippedBottom.setBackgroundColor(context.resources.getColor(R.color.light_gray))
            } else {
                binding.banner.visibility = View.VISIBLE
                binding.shippedBottom.setBackgroundColor(context.resources.getColor(R.color.white))
            }
            val params = binding.banner.layoutParams
            if (value == ShippedSuiteType.GREEN) {
                params.height = context.resources.getDimension(R.dimen.green_banner_height).toInt()
                binding.banner.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(SHIPPED_GREEN_URL))
                    startActivity(context, intent, null)
                }
                binding.reportAnIssue.text = "Download Shipped"
            } else if (value == ShippedSuiteType.GREEN_AND_SHIELD) {
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
        private const val DOWNLOAD_SHIPPED_URL = "https://www.shippedapp.co"
        private const val REPORT_AN_ISSUE_URL = "https://app.shippedapp.co/claim"
        private const val TERMS_OF_SERVICE_URL = "https://www.invisiblecommerce.com/terms"
        private const val PRIVACY_POLICY_URL = "https://www.invisiblecommerce.com/privacy"
        private const val SHIPPED_GREEN_URL = "https://app.shippedapp.co/green"

        fun show(context: Context, offers: ShippedSuiteType) {
            val dialog = LearnMoreDialog(context)
            dialog.type = offers
            dialog.show()
            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }
    }
}

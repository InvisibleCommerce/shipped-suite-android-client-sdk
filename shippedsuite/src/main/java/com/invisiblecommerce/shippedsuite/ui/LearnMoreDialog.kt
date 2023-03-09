package com.invisiblecommerce.shippedsuite.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
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
            if (configuration.type == ShippedSuiteType.GREEN) {
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

    var configuration: ShippedSuiteConfiguration = ShippedSuiteConfiguration()
        set(value) {
            field = value
            updateLayouts()
        }

    private fun updateLayouts() {
        binding.shippedLogo.setImageDrawable(configuration.type.learnMoreLogo(context))
        binding.shippedTitle.text = configuration.type.learnMoreTitle(context)
        val subtitle = when (configuration.type) {
            ShippedSuiteType.GREEN -> context.getString(if (configuration.isInformational) R.string.learn_more_subtitle_green_informational else R.string.learn_more_subtitle_green)
            ShippedSuiteType.SHIELD -> context.getString(if (configuration.isInformational) R.string.learn_more_subtitle_shield_informational else R.string.learn_more_subtitle_shield)
            ShippedSuiteType.GREEN_AND_SHIELD -> context.getString(if (configuration.isInformational) R.string.learn_more_subtitle_green_shield_informational else R.string.learn_more_subtitle_green_shield)
        }
        binding.shippedSubtitle.text = subtitle
        val showTips = showTips()
        binding.shippedSubtitle.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            if (configuration.isInformational) 16.0F else 17.0F
        )
        if (showTips) {
            binding.tipTickOne.visibility = ConstraintLayout.VISIBLE
            binding.shippedTipInfoOne.visibility = ConstraintLayout.VISIBLE
            binding.shippedTipInfoOne.text = configuration.type.learnMoreTip0(context)
            binding.tipTickTwo.visibility = ConstraintLayout.VISIBLE
            binding.shippedTipInfoTwo.visibility = ConstraintLayout.VISIBLE
            binding.shippedTipInfoTwo.text = configuration.type.learnMoreTip1(context)
            binding.tipTickThree.visibility = ConstraintLayout.VISIBLE
            binding.shippedTipInfoThree.visibility = ConstraintLayout.VISIBLE
            binding.shippedTipInfoThree.text = configuration.type.learnMoreTip2(context)
        } else {
            binding.tipTickOne.visibility = ConstraintLayout.GONE
            binding.shippedTipInfoOne.visibility = ConstraintLayout.GONE
            binding.tipTickTwo.visibility = ConstraintLayout.GONE
            binding.shippedTipInfoTwo.visibility = ConstraintLayout.GONE
            binding.tipTickThree.visibility = ConstraintLayout.GONE
            binding.shippedTipInfoThree.visibility = ConstraintLayout.GONE
        }
        if (configuration.type == ShippedSuiteType.SHIELD) {
            binding.banner.visibility = View.GONE
            binding.shippedBottom.setBackgroundColor(context.resources.getColor(R.color.light_gray))
        } else {
            binding.banner.visibility = View.VISIBLE
            binding.shippedBottom.setBackgroundColor(context.resources.getColor(R.color.white))
        }
        val params = binding.banner.layoutParams
        if (configuration.type == ShippedSuiteType.GREEN) {
            params.height = context.resources.getDimension(R.dimen.green_banner_height).toInt()
            binding.reportAnIssue.text = "Download Shipped"
        } else if (configuration.type == ShippedSuiteType.GREEN_AND_SHIELD) {
            params.height =
                context.resources.getDimension(R.dimen.green_shield_banner_height).toInt()
        }
        binding.banner.layoutParams = params

        if (configuration.type == ShippedSuiteType.GREEN || (configuration.type == ShippedSuiteType.GREEN_AND_SHIELD && configuration.isInformational)) {
            binding.banner.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(SHIPPED_GREEN_URL))
                startActivity(context, intent, null)
            }
        }

        val bannerRes = when (configuration.type) {
            ShippedSuiteType.GREEN -> context.getDrawable(R.drawable.green_banner)
            ShippedSuiteType.SHIELD -> null
            ShippedSuiteType.GREEN_AND_SHIELD -> if (configuration.isInformational) context.getDrawable(
                R.drawable.green_banner
            ) else context.getDrawable(
                R.drawable.green_shield_banner
            )
        }
        binding.banner.setImageDrawable(bannerRes)
    }

    private val binding by lazy {
        LearnMoreDialogBinding.inflate(LayoutInflater.from(context))
    }

    private fun showTips(): Boolean {
        if (configuration.isInformational) {
            if (configuration.type == ShippedSuiteType.SHIELD) {
                return true
            }
            return false
        }
        return true
    }

    companion object {
        private const val DOWNLOAD_SHIPPED_URL = "https://www.shippedapp.co"
        private const val REPORT_AN_ISSUE_URL = "https://app.shippedapp.co/claim"
        private const val TERMS_OF_SERVICE_URL = "https://www.invisiblecommerce.com/terms"
        private const val PRIVACY_POLICY_URL = "https://www.invisiblecommerce.com/privacy"
        private const val SHIPPED_GREEN_URL = "https://www.shippedapp.co/green"

        fun show(context: Context, configuration: ShippedSuiteConfiguration) {
            val dialog = LearnMoreDialog(context)
            dialog.configuration = configuration
            dialog.show()
            dialog.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }
    }
}

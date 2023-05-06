package com.invisiblecommerce.shippedsuite.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.invisiblecommerce.shippedsuite.APIRepository
import com.invisiblecommerce.shippedsuite.R
import com.invisiblecommerce.shippedsuite.ShippedAPIRepository
import com.invisiblecommerce.shippedsuite.ShippedPlugins.widgetViewIsSelected
import com.invisiblecommerce.shippedsuite.databinding.ShippedWidgetViewBinding
import com.invisiblecommerce.shippedsuite.exception.APIException
import com.invisiblecommerce.shippedsuite.exception.ShippedException
import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import com.invisiblecommerce.shippedsuite.model.ShippedRequest
import kotlinx.coroutines.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.log10
import kotlin.math.round

enum class ShippedSuiteAppearance(val value: String) {
    LIGHT("light"), DARK("dark"), AUTO("auto");

    private fun isSystemNightMode(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun widgetTitleColor(context: Context): Int {
        return when(this) {
            LIGHT -> context.resources.getColor(R.color.widget_title_light_color)
            DARK -> context.resources.getColor(R.color.widget_title_dark_color)
            AUTO -> if (isSystemNightMode(context)) context.resources.getColor(R.color.widget_title_dark_color) else context.resources.getColor(R.color.widget_title_light_color)
        }
    }

    fun widgetLearnMoreColor(context: Context): Int {
        return when(this) {
            LIGHT -> context.resources.getColor(R.color.widget_learn_more_light_color)
            DARK -> context.resources.getColor(R.color.widget_learn_more_dark_color)
            AUTO -> if (isSystemNightMode(context)) context.resources.getColor(R.color.widget_learn_more_dark_color) else context.resources.getColor(R.color.widget_learn_more_light_color)
        }
    }

    fun widgetFeeColor(context: Context): Int {
        return when(this) {
            LIGHT -> context.resources.getColor(R.color.widget_title_light_color)
            DARK -> context.resources.getColor(R.color.widget_title_dark_color)
            AUTO -> if (isSystemNightMode(context)) context.resources.getColor(R.color.widget_title_dark_color) else context.resources.getColor(R.color.widget_title_light_color)
        }
    }

    fun widgetDescColor(context: Context): Int {
        return when(this) {
            LIGHT -> context.resources.getColor(R.color.widget_info_light_color)
            DARK -> context.resources.getColor(R.color.widget_info_dark_color)
            AUTO -> if (isSystemNightMode(context)) context.resources.getColor(R.color.widget_info_dark_color) else context.resources.getColor(R.color.widget_info_light_color)
        }
    }

}

enum class ShippedSuiteType(val value: String) {
    GREEN("green"), SHIELD("shield"), GREEN_AND_SHIELD("green_shield");

    fun widgetTitle(context: Context): String {
        return when (this) {
            GREEN -> context.getString(R.string.green_title)
            SHIELD -> context.getString(R.string.shield_title)
            GREEN_AND_SHIELD -> context.getString(R.string.green_shield_title)
        }
    }

    fun widgetDesc(context: Context): String {
        return when (this) {
            GREEN -> context.getString(R.string.green_desc)
            SHIELD -> context.getString(R.string.shield_desc)
            GREEN_AND_SHIELD -> context.getString(R.string.green_shield_desc)
        }
    }

    fun widgetFee(offers: ShippedOffers, context: Context): String {
        val fee = when (this) {
            GREEN -> offers.greenFee
            SHIELD -> offers.shieldFee
            GREEN_AND_SHIELD -> if (offers.greenFee != null && offers.shieldFee != null) offers.greenFee + offers.shieldFee else null
        }

        val currency = offers.shieldFeeWithCurrency?.currency
        if (fee != null && currency != null) {
            val space = if (currency.symbol.length > 2) " " else ""

            val otherSymbols = DecimalFormatSymbols()
            otherSymbols.decimalSeparator = currency.decimalMark.first()
            otherSymbols.groupingSeparator = currency.thousandsSeparator.first()
            val df = DecimalFormat("#,###.##", otherSymbols)
            val fractionDigits = round(log10(currency.subunitToUnit.toDouble()))
            df.maximumFractionDigits = fractionDigits.toInt()

            if (currency.symbolFirst) {
                return "\u202A" + currency.symbol + "\u202C" + space + df.format(fee)
            }
            return "\u202A" + df.format(fee) + space + currency.symbol + "\u202C"
        }
        return context.getString(R.string.shipped_fee_default)
    }

    fun learnMoreLogo(context: Context): Drawable? {
        return when (this) {
            GREEN -> context.getDrawable(R.drawable.green_logo)
            SHIELD -> context.getDrawable(R.drawable.shield_logo)
            GREEN_AND_SHIELD -> context.getDrawable(R.drawable.green_shield_logo)
        }
    }

    fun learnMoreTitle(context: Context): String {
        return when (this) {
            GREEN -> context.getString(R.string.learn_more_title_green)
            SHIELD -> context.getString(R.string.learn_more_title_shield)
            GREEN_AND_SHIELD -> context.getString(R.string.learn_more_title_green_shield)
        }
    }

    fun learnMoreSubtitle(context: Context): String {
        return when (this) {
            GREEN -> context.getString(R.string.learn_more_subtitle_green)
            SHIELD -> context.getString(R.string.learn_more_subtitle_shield)
            GREEN_AND_SHIELD -> context.getString(R.string.learn_more_subtitle_green_shield)
        }
    }

    fun learnMoreBanner(context: Context): Drawable? {
        return when (this) {
            GREEN -> context.getDrawable(R.drawable.green_banner)
            SHIELD -> null
            GREEN_AND_SHIELD -> context.getDrawable(R.drawable.green_shield_banner)
        }
    }

    fun learnMoreTip0(context: Context): String {
        if (this == ShippedSuiteType.GREEN) {
            return context.getString(R.string.shipped_green_tip_0)
        }

        return context.getString(R.string.shipped_default_tip_0)
    }

    fun learnMoreTip1(context: Context): String {
        if (this == ShippedSuiteType.GREEN) {
            return context.getString(R.string.shipped_green_tip_1)
        }

        return context.getString(R.string.shipped_default_tip_1)
    }

    fun learnMoreTip2(context: Context): String {
        if (this == ShippedSuiteType.GREEN) {
            return context.getString(R.string.shipped_green_tip_2)
        }

        return context.getString(R.string.shipped_default_tip_2)
    }
}

data class ShippedSuiteConfiguration(
    var type: ShippedSuiteType = ShippedSuiteType.SHIELD,
    var isInformational: Boolean = false,
    var isMandatory: Boolean = false,
    var isRespectServer: Boolean = false,
    var currency: String = "USD",
    var appearance: ShippedSuiteAppearance = ShippedSuiteAppearance.AUTO
)

/**
A widget view which shows the shield fee.
 */
class WidgetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        const val IS_SELECTED_KEY = "isSelected"
        const val TOTAL_FEE_KEY = "totalFee"
        const val ERROR_KEY = "error"
    }

    interface Callback<T> {
        fun onResult(result: Map<String, Any>)
    }

    var configuration: ShippedSuiteConfiguration = ShippedSuiteConfiguration()
        set(value) {
            field = value
            updateTexts()
            hideToggleIfMandatory(value.isMandatory, value.isInformational)
            hideFeeIfInformational(value.isInformational)
        }

    private var cachedOffers: ShippedOffers? = null
        set(value) {
            field = value
            if (value != null) {
                configuration.isMandatory =
                    (cachedOffers?.isMandatory ?: false) || configuration.isMandatory
                updateWidgetIfConfigsMismatch(value)
                hideToggleIfMandatory(configuration.isMandatory, configuration.isInformational)
            }
        }

    var callback: Callback<BigDecimal>? = null

    private var job: Job? = null

    private val apiRepository: APIRepository by lazy {
        ShippedAPIRepository()
    }

    private val binding by lazy {
        ShippedWidgetViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        binding.learnMore.paintFlags = binding.learnMore.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.learnMore.setOnClickListener {
            LearnMoreDialog.show(context, configuration)
        }
        binding.shippedSwitch.isChecked = widgetViewIsSelected
        binding.shippedSwitch.setOnCheckedChangeListener { _, isChecked ->
            widgetViewIsSelected = isChecked
            triggerWidgetChangeWithError()
        }
    }

    fun updateOrderValue(orderValue: BigDecimal) {
        // Cancel job first
        cancelJob()

        job = CoroutineScope(Dispatchers.IO).launch {
            val result = runCatching {
                requireNotNull(
                    apiRepository.getOffersFee(
                        ShippedAPIRepository.ShippedRequestOptions(
                            request = ShippedRequest.Builder().setOrderValue(orderValue).setCurrency(configuration.currency).build()
                        )
                    )
                )
            }
            withContext(Dispatchers.Main) {
                result.fold(
                    onSuccess = {
                        onResult(offers = it)
                    },
                    onFailure = {
                        onResult(error = handleError(it))
                    }
                )
            }
        }
    }

    private fun onResult(offers: ShippedOffers? = null, error: ShippedException? = null) {
        when {
            offers != null -> {
                cachedOffers = offers
            }
            error != null -> {
                updateWidgetIfError(error)
            }
        }
    }

    private fun updateTexts() {
        binding.widgetTitle.text = configuration.type.widgetTitle(context)
        binding.widgetTitle.setTextColor(configuration.appearance.widgetTitleColor(context))
        binding.learnMore.setTextColor(configuration.appearance.widgetLearnMoreColor(context))
        binding.fee.setTextColor(configuration.appearance.widgetFeeColor(context))
        binding.widgetDesc.text = configuration.type.widgetDesc(context)
        binding.widgetDesc.setTextColor(configuration.appearance.widgetDescColor(context))
        binding.shippedLogo.setImageDrawable(configuration.type.learnMoreLogo(context))
    }

    private fun hideFeeIfInformational(isInformational: Boolean) {
        if (isInformational) {
            binding.fee.visibility = GONE
            binding.learnMore.gravity = Gravity.END
        } else {
            binding.fee.visibility = VISIBLE
            binding.learnMore.gravity = Gravity.START
        }
    }

    private fun hideToggleIfMandatory(isMandatory: Boolean, isInformational: Boolean) {
        if (isMandatory || isInformational) {
            binding.shippedSwitch.visibility = GONE
            binding.shippedLogo.visibility = VISIBLE
        } else {
            binding.shippedSwitch.visibility = VISIBLE
            binding.shippedLogo.visibility = GONE
        }
        if (isMandatory) {
            binding.shippedSwitch.isChecked = true
        }
    }

    private fun updateWidgetIfConfigsMismatch(offers: ShippedOffers) {
        var shouldUpdate = false
        var isShild =
            configuration.type == ShippedSuiteType.SHIELD || configuration.type == ShippedSuiteType.GREEN_AND_SHIELD
        var isGreen =
            configuration.type == ShippedSuiteType.GREEN || configuration.type == ShippedSuiteType.GREEN_AND_SHIELD

        if (isShild && !offers.isShieldAvailable()) {
            isShild = false
            shouldUpdate = true
        } else if (!isShild && offers.isShieldAvailable() && configuration.isRespectServer) {
            isShild = true
            shouldUpdate = true
        }

        if (isGreen && !offers.isGreenAvailable()) {
            isGreen = false
            shouldUpdate = true
        } else if (!isGreen && offers.isGreenAvailable() && configuration.isRespectServer) {
            isGreen = true
            shouldUpdate = true
        }

        if ((!offers.isShieldAvailable() && offers.isGreenAvailable()) ||
            (offers.isShieldAvailable() && !offers.isGreenAvailable())
        ) {
            if (offers.isShieldAvailable() && !isShild) {
                isShild = true
                isGreen = false
                shouldUpdate = true
            } else if (offers.isGreenAvailable() && !isGreen) {
                isShild = false
                isGreen = true
                shouldUpdate = true
            }
        }

        if (shouldUpdate) {
            configuration.type = if (isShild && !isGreen) {
                ShippedSuiteType.SHIELD
            } else if (!isShild && isGreen) {
                ShippedSuiteType.GREEN
            } else if (isShild && isGreen) {
                ShippedSuiteType.GREEN_AND_SHIELD
            } else {
                ShippedSuiteType.SHIELD
            }
            updateTexts()
        }

        binding.fee.text = configuration.type.widgetFee(offers, context)
        triggerWidgetChangeWithError()
    }

    private fun updateWidgetIfError(error: ShippedException) {
        binding.fee.text = context.getString(R.string.shipped_fee_default)
        cachedOffers = null
        triggerWidgetChangeWithError(error)
    }

    private fun triggerWidgetChangeWithError(error: ShippedException? = null) {
        var values: MutableMap<String, Any> = mutableMapOf()
        values[IS_SELECTED_KEY] = widgetViewIsSelected
        cachedOffers?.let { offers ->
            if (configuration.type == ShippedSuiteType.SHIELD) {
                offers.shieldFee?.let {
                    values[TOTAL_FEE_KEY] = it
                }
            }
            if (configuration.type == ShippedSuiteType.GREEN) {
                offers.greenFee?.let {
                    values[TOTAL_FEE_KEY] = it
                }
            }
            if (configuration.type == ShippedSuiteType.GREEN_AND_SHIELD) {
                offers.greenFee?.let { greenFee ->
                    offers.shieldFee?.let { shieldFee ->
                        values[TOTAL_FEE_KEY] = greenFee + shieldFee
                    }
                }
            }
        }

        if (error != null) {
            values[ERROR_KEY] = error
        }
        callback?.onResult(values)
    }

    private fun handleError(throwable: Throwable): ShippedException {
        return if (throwable is ShippedException) {
            throwable
        } else {
            APIException(message = throwable.message)
        }
    }

    override fun onDetachedFromWindow() {
        cancelJob()
        super.onDetachedFromWindow()
    }

    private fun cancelJob() {
        job?.cancel()
        job = null
    }
}

package com.invisiblecommerce.shippedsuite.ui

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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
import java.text.NumberFormat

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
        if (fee != null) {
            return NumberFormat.getCurrencyInstance().format(fee)
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
        const val SHIELD_FEE_KEY = "shieldFee"
        const val GREEN_FEE_KEY = "greenFee"
        const val ERROR_KEY = "error"
    }

    interface Callback<T> {
        fun onResult(result: Map<String, Any>)
    }

    var type: ShippedSuiteType = ShippedSuiteType.GREEN
        set(value) {
            field = value
            binding.widgetTitle.text = type.widgetTitle(context)
            binding.widgetDesc.text = type.widgetDesc(context)
        }

    var isRespectServer: Boolean = false

    var callback: Callback<BigDecimal>? = null

    private var job: Job? = null

    private var cachedOffers: ShippedOffers? = null

    private val apiRepository: APIRepository by lazy {
        ShippedAPIRepository()
    }

    private val binding by lazy {
        ShippedWidgetViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        binding.learnMore.paintFlags = binding.learnMore.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.learnMore.setOnClickListener {
            LearnMoreDialog.show(context, type)
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
                            request = ShippedRequest.Builder().setOrderValue(orderValue).build()
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
                updateWidgetIfConfigsMismatch(offers)
            }
            error != null -> {
                updateWidgetIfError(error)
            }
        }
    }

    private fun updateWidgetIfConfigsMismatch(offers: ShippedOffers) {
        var shouldUpdate = false
        var isShild = type == ShippedSuiteType.SHIELD || type == ShippedSuiteType.GREEN_AND_SHIELD
        var isGreen = type == ShippedSuiteType.GREEN || type == ShippedSuiteType.GREEN_AND_SHIELD

        if (isShild && !offers.isShieldAvailable()) {
            isShild = false
            shouldUpdate = true
        } else if (!isShild && offers.isShieldAvailable() && isRespectServer) {
            isShild = true
            shouldUpdate = true
        }

        if (isGreen && !offers.isGreenAvailable()) {
            isGreen = false
            shouldUpdate = true
        } else if (!isGreen && offers.isGreenAvailable() && isRespectServer) {
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
            type = if (isShild && !isGreen) {
                ShippedSuiteType.SHIELD
            } else if (!isShild && isGreen) {
                ShippedSuiteType.GREEN
            } else if (isShild && isGreen) {
                ShippedSuiteType.GREEN_AND_SHIELD
            } else {
                ShippedSuiteType.SHIELD
            }
        }

        binding.fee.text = type.widgetFee(offers, context)
        cachedOffers = offers
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
        if (type == ShippedSuiteType.SHIELD || type == ShippedSuiteType.GREEN_AND_SHIELD) {
            cachedOffers?.shieldFee?.let {
                values[SHIELD_FEE_KEY] = it
            }
        }
        if (type == ShippedSuiteType.GREEN || type == ShippedSuiteType.GREEN_AND_SHIELD) {
            cachedOffers?.greenFee?.let {
                values[GREEN_FEE_KEY] = it
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

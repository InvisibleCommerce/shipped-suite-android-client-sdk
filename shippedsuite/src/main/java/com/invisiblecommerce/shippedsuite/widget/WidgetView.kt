package com.invisiblecommerce.shippedsuite.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.invisiblecommerce.shippedsuite.APIRepository
import com.invisiblecommerce.shippedsuite.R
import com.invisiblecommerce.shippedsuite.ShippedAPIRepository
import com.invisiblecommerce.shippedsuite.ShippedPlugins.widgetViewIsSelected
import com.invisiblecommerce.shippedsuite.databinding.ViewShieldWidgetBinding
import com.invisiblecommerce.shippedsuite.exception.APIException
import com.invisiblecommerce.shippedsuite.exception.ShippedException
import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import com.invisiblecommerce.shippedsuite.model.ShippedRequest
import kotlinx.coroutines.*
import java.math.BigDecimal
import java.text.NumberFormat

enum class WidgetViewOffers(val value: String) {
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

    fun widgetFee(offers: ShippedOffers): BigDecimal {
        return when(this) {
            GREEN -> offers.greenFee
            SHIELD -> offers.shieldFee
            GREEN_AND_SHIELD -> offers.greenFee + offers.shieldFee
        }
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

    var offers: WidgetViewOffers = WidgetViewOffers.GREEN
        get() = field
        set(value) {
            field = value
            binding.widgetTitle.text = offers.widgetTitle(context)
            binding.widgetDesc.text = offers.widgetDesc(context)
        }

    var callback: Callback<BigDecimal>? = null

    private var job: Job? = null

    private var cacheResult: MutableMap<String, Any> = mutableMapOf()

    private val apiRepository: APIRepository by lazy {
        ShippedAPIRepository()
    }

    private val binding by lazy {
        ViewShieldWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        binding.learnMore.paintFlags = binding.learnMore.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.learnMore.setOnClickListener {
            LearnMoreDialog.show(context)
        }
        binding.shippedSwitch.isChecked = widgetViewIsSelected
        binding.shippedSwitch.setOnCheckedChangeListener { _, isChecked ->
            widgetViewIsSelected = isChecked
            onResult()
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
                        onResult(ShippedOffers = it)
                        binding.fee.text = NumberFormat.getCurrencyInstance().format(offers.widgetFee(it))
                    },
                    onFailure = {
                        onResult(error = handleError(it))
                    }
                )
            }
        }
    }

    private fun onResult(ShippedOffers: ShippedOffers? = null, error: ShippedException? = null) {
        when {
            ShippedOffers != null -> {
                if ((offers == WidgetViewOffers.SHIELD || offers == WidgetViewOffers.GREEN_AND_SHIELD) && ShippedOffers.shieldFee != null) {
                    cacheResult[SHIELD_FEE_KEY] = ShippedOffers.shieldFee
                }
                if ((offers == WidgetViewOffers.GREEN || offers == WidgetViewOffers.GREEN_AND_SHIELD) && ShippedOffers.greenFee != null) {
                    cacheResult[GREEN_FEE_KEY] = ShippedOffers.greenFee
                }
            }
            error != null -> {
                cacheResult[ERROR_KEY] = error
            }
        }
        cacheResult[IS_SELECTED_KEY] = widgetViewIsSelected
        callback?.onResult(cacheResult)
    }

    private fun handleError(throwable: Throwable): ShippedException {
        return if (throwable is ShippedException) {
            throwable
        } else {
            APIException(message = throwable.message)
        }
    }

    override fun onDetachedFromWindow() {
        cacheResult.clear()
        cancelJob()
        super.onDetachedFromWindow()
    }

    private fun cancelJob() {
        job?.cancel()
        job = null
    }
}

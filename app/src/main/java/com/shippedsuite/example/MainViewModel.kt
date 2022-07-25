package com.shippedsuite.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.invisiblecommerce.shippedsuite.ShippedSuite
import com.invisiblecommerce.shippedsuite.exception.ShippedException
import com.invisiblecommerce.shippedsuite.model.ShippedOffers
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal

internal class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val shippedSuite by lazy { ShippedSuite() }

    val shippedLiveData = MutableLiveData<ShippedOfferStatus>()

    val searchKey: MutableStateFlow<String?> = MutableStateFlow(null)

    sealed class ShippedOfferStatus {
        data class Success(val ShippedOffers: ShippedOffers) : ShippedOfferStatus()
        data class Fail(val exception: ShippedException) : ShippedOfferStatus()
    }

    fun getOffersFee(orderValue: BigDecimal) {
        shippedSuite.getOffersFee(
            orderValue,
            object : ShippedSuite.Listener<ShippedOffers> {
                override fun onSuccess(response: ShippedOffers) {
                    shippedLiveData.value = ShippedOfferStatus.Success(response)
                }

                override fun onFailed(exception: ShippedException) {
                    shippedLiveData.value = ShippedOfferStatus.Fail(exception)
                }
            }
        )
    }

    internal class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                application
            ) as T
        }
    }
}

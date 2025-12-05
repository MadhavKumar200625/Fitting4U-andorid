// CheckoutViewModel.kt
package com.fitting4u.fitting4u.presentation.Fabric.Checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.Data.remote.dto.order.boutiques.nearby.Boutique
import com.fitting4u.fitting4u.Data.remote.dto.order.create_order.Order
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.CheckoutAddress
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.ConfirmOrderRequest
import com.fitting4u.fitting4u.Data.remote.request_model.order.verify.VerifyRequestModel
import com.fitting4u.fitting4u.domain.use_case.checkout.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CheckoutStep {
    object Cart : CheckoutStep()
    object Shipping : CheckoutStep()
    object BoutiquePickup : CheckoutStep()
    object Payment : CheckoutStep()   // retained but not used by UI in your flow
    object Success : CheckoutStep()
}

data class CheckoutUiState(
    val step: CheckoutStep = CheckoutStep.Cart,
    val shippingType: String = "HOME",
    val loading: Boolean = false,
    val error: String? = null,
    val nearbyBoutiques: List<Boutique> = emptyList(),
    val selectedBoutique: Boutique? = null,
    val razorOrder: Order? = null,
    val paymentVerified: Boolean = false,
    val successOrderId: String? = null,
    val address: CheckoutAddress? = null,
    val shippingSummary: Map<String, Any>? = null, // address map or boutique map
    val shippingConfirmed: Boolean = false
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase,
    private val getNearbyBoutiquesUseCase: GetNearbyBoutiquesUseCase,
    private val verifyPaymentUseCase: VerifyPaymentUseCase,
    private val confirmOrderUseCase: ConfirmOrderUseCase,
    private val getLatLngFromPincodeUseCase: GetLatLngFromPincodeUseCase
) : ViewModel() {

    private val _ui = MutableStateFlow(CheckoutUiState())
    val ui: StateFlow<CheckoutUiState> = _ui.asStateFlow()

    // ----------------------------
    // Simple setters
    // ----------------------------
    fun setShippingType(type: String) {
        _ui.update { it.copy(shippingType = type) }
    }

    fun selectBoutique(b: Boutique?) {
        _ui.update { it.copy(selectedBoutique = b) }
    }

    fun updateAddress(addr: CheckoutAddress) {
        _ui.update { it.copy(address = addr) }
    }

    fun clearError() {
        _ui.update { it.copy(error = null) }
    }

    // ----------------------------
    // Navigation helpers used by UI
    // ----------------------------
    fun goTo(step: CheckoutStep) {
        _ui.update { it.copy(step = step, error = null) }
    }

    /**
     * Called when user taps Continue in Shipping panel.
     * This saves shippingSummary & returns the flow to Cart step (so Cart shows the shipping preview).
     *
     * summary: a Map with keys to show in cart (e.g. "type" -> "HOME"/"BOUTIQUE", "address" or "boutique")
     */
    fun confirmShippingAndReturnToCart(summary: Map<String, Any>) {
        _ui.update {
            it.copy(
                shippingSummary = summary,
                shippingConfirmed = true,
                step = CheckoutStep.Cart,
                error = null
            )
        }
    }

    fun clearShippingPreview() {
        _ui.update { it.copy(shippingSummary = null, shippingConfirmed = false) }
    }

    // ----------------------------
    // Load Nearby Boutiques (usecase wraps network)
    // ----------------------------
    fun loadNearby(lat: Double, long: Double) {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }

            when (val res = getNearbyBoutiquesUseCase(lat, long)) {
                is Resource.Loading -> _ui.update { it.copy(loading = true) }
                is Resource.Success -> _ui.update {
                    it.copy(
                        loading = false,
                        nearbyBoutiques = res.data.boutiques ?: emptyList(),
                        error = null
                    )
                }
                is Resource.Error -> _ui.update { it.copy(loading = false, error = res.message) }
            }
        }
    }

    fun searchPincode(pin: String) {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }

            when (val loc = getLatLngFromPincodeUseCase(pin)) {
                is Resource.Success -> {
                    val (lat, long) = loc.data!!
                    loadNearby(lat, long)
                }

                is Resource.Error -> {
                    _ui.update { it.copy(loading = false, error = loc.message) }
                }

                else -> {}
            }
        }
    }

    // ----------------------------
    // Create Razorpay order on backend (usecase)
    // ----------------------------
    fun createRazorOrder(amount: Double) {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }

            when (val res = createOrderUseCase(amount)) {
                is Resource.Loading -> _ui.update { it.copy(loading = true) }
                is Resource.Success -> _ui.update { it.copy(loading = false, razorOrder = res.data.order) }
                is Resource.Error -> _ui.update { it.copy(loading = false, error = res.message) }
            }
        }
    }

    // ----------------------------
    // Verify Payment after native razorpay returns a result (call this from your activity/fragment)
    // ----------------------------
    fun verifyPayment(
        razorReq: VerifyRequestModel,
        onVerified: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }

            when (val res = verifyPaymentUseCase(razorReq)) {
                is Resource.Success -> {
                    _ui.update { it.copy(loading = false, paymentVerified = true) }
                    onVerified(true)
                }
                is Resource.Error -> {
                    _ui.update { it.copy(loading = false, error = res.message) }
                    onVerified(false)
                }
                else -> {}
            }
        }
    }

    // ----------------------------
    // Confirm order to backend after successful verification
    // Build ConfirmOrderRequest on the caller side and pass here.
    // ----------------------------
    fun confirmOrder(
        req: ConfirmOrderRequest,
        onResult: (Boolean, String?) -> Unit = { _, _ -> }
    ) {
        viewModelScope.launch {
            _ui.update { it.copy(loading = true, error = null) }

            when (val res = confirmOrderUseCase(req)) {
                is Resource.Success -> {
                    _ui.update {
                        it.copy(
                            loading = false,
                            step = CheckoutStep.Success,
                            successOrderId = res.data.orderId
                        )
                    }
                    onResult(true, res.data.orderId)
                }
                is Resource.Error -> {
                    _ui.update { it.copy(loading = false, error = res.message) }
                    onResult(false, null)
                }
                else -> {}
            }
        }
    }
}
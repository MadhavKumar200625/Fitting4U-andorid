package com.fitting4u.fitting4u.presentation.Fabric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.Data.remote.request_model.cart.Item
import com.fitting4u.fitting4u.Data.remote.request_model.cart.RequestCart
import com.fitting4u.fitting4u.domain.repository.CartRepository
import com.fitting4u.fitting4u.domain.use_case.cart.AddToCartUseCase
import com.fitting4u.fitting4u.domain.use_case.cart.DecreaseCartQtyUseCase
import com.fitting4u.fitting4u.domain.use_case.cart.GetCartQtyUseCase
import com.fitting4u.fitting4u.domain.use_case.cart.GetCartUseCase
import com.fitting4u.fitting4u.domain.use_case.cart.GetLocalCartUseCase
import com.fitting4u.fitting4u.domain.use_case.cart.ObserveItemQtyUseCase
import com.fitting4u.fitting4u.domain.use_case.cart.RemoveFromCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addToCart: AddToCartUseCase,
    private val decrease: DecreaseCartQtyUseCase,
    private val remove: RemoveFromCartUseCase,
    private val getCartQty: GetCartQtyUseCase,
    private val observeItemQtyUseCase: ObserveItemQtyUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getLocalCart: GetLocalCartUseCase
) : ViewModel() {

    private val _totalQty = MutableStateFlow(0.0)
    val totalQty: StateFlow<Double> = _totalQty

    private val _cartSummary = MutableStateFlow(CartSummaryUiState())
    val cartSummary: StateFlow<CartSummaryUiState> = _cartSummary

    init {
        // initial load
        viewModelScope.launch { refreshAndEmit() }
    }

    /**
     * Refresh only the total meter count (fast).
     */
    fun refresh() {
        viewModelScope.launch {
            _totalQty.value = getCartQty()
        }
    }

    /**
     * Public add — ensures DB write completes, then refreshes summary and totals.
     * Caller (UI) only needs to call vm.add(...) — no manual reloads.
     */
    fun add(id: String, qty: Double) {
        viewModelScope.launch {
            addToCart(id, qty)                // suspend: DB write completes here
            refreshAndEmit()                  // suspend helper that fetches room+server + emits
        }
    }

    fun decreaseQty(id: String, minus: Double) {
        viewModelScope.launch {
            decrease(id, minus)               // suspend: DB write completes here
            refreshAndEmit()
        }
    }

    fun removeItem(id: String) {
        viewModelScope.launch {
            remove(id)                        // suspend: DB write completes here
            refreshAndEmit()
        }
    }

    fun observeItemQty(id: String): Flow<Double> {
        return observeItemQtyUseCase(id)
    }

    /**
     * Public launcher for screens that want to explicitly load summary.
     * It simply launches the same suspend helper.
     */
    fun loadCartSummary() {
        viewModelScope.launch {
            refreshAndEmit()
        }
    }

    /**
     * SUSPENDING helper — reads room cart, calls API and emits UI states.
     * Because it's suspend, callers inside the same coroutine (above) will wait
     * for DB writes to complete before running this.
     */
    private suspend fun refreshAndEmit() {
        try {
            _cartSummary.value = CartSummaryUiState(loading = true)

            // 1) read local room cart (suspend)
            val roomCart = getLocalCart()

            if (roomCart.isEmpty()) {
                // make sure totals are zeroed & UI shows empty
                _totalQty.value = 0.0
                _cartSummary.value = CartSummaryUiState(
                    loading = false,
                    error = null,
                    data = null
                )
                return
            }

            // 2) build request using up-to-date roomCart (suspend)
            val request = RequestCart(
                items = roomCart.map {
                    Item(id = it.id, qty = it.qtyMeters)
                }
            )

            // 3) call server (suspend) with correct, freshly read roomCart
            val result = getCartUseCase(request)

            // 4) update totals and UI state (emit)
            _cartSummary.value = CartSummaryUiState(
                loading = false,
                data = result
            )

            // update total qty (recalculate from local or use GetCartQtyUseCase)
            _totalQty.value = roomCart.sumOf { it.qtyMeters }

        } catch (e: Exception) {
            _cartSummary.value = CartSummaryUiState(
                loading = false,
                error = e.message
            )
        }
    }
}
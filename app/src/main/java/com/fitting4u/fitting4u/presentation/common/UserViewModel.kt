package com.fitting4u.fitting4u.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitting4u.fitting4u.domain.use_case.user.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val verifyBackendUseCase: VerifyBackendUseCase,
    private val saveAuthPrefsUseCase: SaveAuthPrefsUseCase,
    getPhoneUseCase: GetPhoneUseCase,
    getIsBoutiqueUseCase: GetIsBoutiqueUseCase
) : ViewModel() {

    // UI State
    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    // Persisted login
    val phoneFlow = getPhoneUseCase()
    val boutiqueFlow = getIsBoutiqueUseCase()

    // Pending action after successful login
    private var pendingAction: (() -> Unit)? = null

    fun openLoginSheetFor(action: () -> Unit) {
        pendingAction = action
        _ui.update { it.copy(showLoginSheet = true) }
    }

    fun closeLoginSheet() {
        _ui.update { it.copy(showLoginSheet = false) }
    }

    fun resumePendingAction() {
        pendingAction?.invoke()
        pendingAction = null
    }

    fun onPhoneChanged(newPhone: String) {
        _ui.update { it.copy(phone = newPhone) }
    }

    fun sendOtp(verificationId: String?) {
        if (verificationId == null) {
            _ui.update { it.copy(error = "OTP error") }
            return
        }
        _ui.update {
            it.copy(
                verificationId = verificationId,
                isOtpSent = true,
                isLoading = false,
                error = null
            )
        }
    }

    fun sendOtpAgain(verificationId: String?) {
        if (verificationId == null) return
        _ui.update {
            it.copy(
                verificationId = verificationId,
                isOtpSent = true,
                error = null
            )
        }
    }

    fun verifyOtpAndLogin(code: String) {
        val verificationId = _ui.value.verificationId ?: return

        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            try {
                verifyOtpUseCase(verificationId, code)

                val backend = verifyBackendUseCase(_ui.value.phone)

                backend.fold(
                    onSuccess = { pair ->
                        val isBoutique = pair.first
                        val normalized = pair.second

                        saveAuthPrefsUseCase(normalized, isBoutique)

                        _ui.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                error = null,
                                showLoginSheet = false
                            )
                        }

                        resumePendingAction()
                    },

                    onFailure = {
                        _ui.update { it.copy(isLoading = false, error = it.error) }
                    }
                )

            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unexpected error"
                    )
                }
            }
        }
    }
}
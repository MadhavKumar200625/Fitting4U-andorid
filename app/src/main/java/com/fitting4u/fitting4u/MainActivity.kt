package com.fitting4u.fitting4u

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.fitting4u.fitting4u.Data.remote.request_model.order.verify.VerifyRequestModel
import com.fitting4u.fitting4u.presentation.AppNavigation
import com.fitting4u.fitting4u.presentation.Fabric.Checkout.CheckoutViewModel
import com.fitting4u.fitting4u.ui.theme.Fitting4UTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() , PaymentResultListener{
//    @Inject lateinit var checkoutVm: CheckoutViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // UI goes behind system bars

        setContent {

            SideEffect {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            val view = LocalView.current

            SideEffect {
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = true
            }

            Fitting4UTheme {

                    AppNavigation()

            }
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String) {
        // Important: signature & order_id are also passed — follow SDK docs
        val verifyReq = VerifyRequestModel(
            razorpay_order_id = "<order id from razorpay callback>",
            razorpay_payment_id = razorpayPaymentId,
            razorpay_signature = "<signature from callback>"
        )
//        checkoutVm.verifyPayment(verifyReq) { verified ->
//            if (verified) {
//                // build ConfirmOrderRequest and call checkoutVm.confirmOrder(...)
//            }
//        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        // show toast / update UI
    }
}

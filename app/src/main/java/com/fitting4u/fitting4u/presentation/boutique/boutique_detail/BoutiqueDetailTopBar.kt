package com.fitting4u.fitting4u.presentation.boutique.boutique_detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

val Primary = Color(0xFF003466)
private val InputBg = Color(0xFFF9FBFF)
val MutedText = Color(0xFF6B6B6B)

@Composable
fun BoutiqueDetailTopBar(
    onNavigateToSearch: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val glow by animateDpAsState(
        targetValue = if (isFocused) 14.dp else 0.dp,
        animationSpec = tween(350, easing = FastOutSlowInEasing)
    )

    // always show empty static text
    val tfValue = remember { mutableStateOf(TextFieldValue("")) }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(glow, RoundedCornerShape(18.dp), ambientColor = Primary)
            .clickable {
                focusManager.clearFocus()
                onNavigateToSearch()
            }
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            tonalElevation = 4.dp
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = tfValue.value,
                    onValueChange = {},     // no typing allowed
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .onFocusChanged { isFocused = it.isFocused }
                        .clickable {
                            focusManager.clearFocus()
                            onNavigateToSearch()
                        },
                    readOnly = true,         // user can't type
                    singleLine = true,
                    placeholder = {
                        Text(
                            "Search boutiques…",
                            color = Color.Gray.copy(alpha = 0.7f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Primary
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = InputBg,
                        unfocusedContainerColor = InputBg,
                        cursorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                // Fake search button (just navigates to search)
                Button(
                    onClick = onNavigateToSearch,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Search", color = Color.White)
                }
            }
        }
    }
}
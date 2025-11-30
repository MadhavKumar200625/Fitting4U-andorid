package com.fitting4u.fitting4u.presentation.boutique.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Primary = Color(0xFF003466)
private val InputBg = Color(0xFFF9FBFF)

@Composable
fun SearchBarWithFilters(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFilterClicked: () -> Unit,
    modifier: Modifier = Modifier,
    debounceMs: Long = 400L
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var tfValue by remember { mutableStateOf(TextFieldValue(text = value)) }
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    var isFocused by remember { mutableStateOf(false) }

    // Sync external changes
    LaunchedEffect(value) {
        if (value != tfValue.text) {
            tfValue = tfValue.copy(text = value)
        }
    }

    // ✨ Animated glow when focused
    val glow by animateDpAsState(
        targetValue = if (isFocused) 14.dp else 0.dp,
        animationSpec = tween(350, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(glow, RoundedCornerShape(18.dp), ambientColor = Primary)
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
                    value = tfValue,
                    onValueChange = { newValue ->
                        tfValue = newValue
                        onValueChange(newValue.text)

                        debounceJob?.cancel()

                        if (newValue.text.length >= 2) {
                            debounceJob = scope.launch {
                                delay(debounceMs)
                                onSearch(newValue.text)
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .onFocusChanged { isFocused = it.isFocused },
                    singleLine = true,
                    placeholder = {
                        Text(
                            "Search boutiques, designers, cities…",
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
                    trailingIcon = {
                        Row {

                            // 🎛 Filter Button
                            IconButton(
                                onClick = {
                                    focusManager.clearFocus()
                                    onFilterClicked()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = "Filters",
                                    tint = Primary
                                )
                            }

                            // ❌ Clear Search
                            if (tfValue.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    tfValue = TextFieldValue("")
                                    onValueChange("")
                                    onSearch("")
                                    debounceJob?.cancel()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear Search",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = InputBg,
                        unfocusedContainerColor = InputBg,
                        cursorColor = Primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            debounceJob?.cancel()
                            onSearch(tfValue.text)
                            focusManager.clearFocus()
                        }
                    )
                )

                // 🔍 Search Button
                Button(
                    onClick = {
                        debounceJob?.cancel()
                        onSearch(tfValue.text)
                        focusManager.clearFocus()
                    },
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
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Search", color = Color.White)
                }
            }
        }
    }
}
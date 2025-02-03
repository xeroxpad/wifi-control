package com.example.wificontrol.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.primaryLight
import com.example.wificontrol.R
import com.example.wificontrol.utils.FormatPhoneNumber

const val countNumberPhone = 10

@Composable
fun EnterPhoneNumber(
    modifier: Modifier = Modifier,
    placeholder: Int,
    onValueChange: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .border(width = 1.dp, shape = RoundedCornerShape(16.dp), color = Color.Black)
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(shape = RoundedCornerShape(14.dp))
            .background(Color.Gray.copy(alpha = 0.2f)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            BasicTextField(
                value = phoneNumber,
                onValueChange = {
                    val digitsOnly = it.filter { it.isDigit() }
                    if (digitsOnly.length <= countNumberPhone) {
                        phoneNumber = digitsOnly
                        onValueChange(digitsOnly)
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    lineHeight = 56.sp,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Phone
                ),
                visualTransformation = FormatPhoneNumber(),
                decorationBox = { innerTextField ->
                    if (phoneNumber.isEmpty()) {
                        Text(
                            text = stringResource(placeholder),
                            color = primaryLight.copy(alpha = 0.5f),
                            style = TextStyle(
                                lineHeight = 56.sp,
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp,
                                letterSpacing = 2.sp
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextFieldAuth(
    modifier: Modifier = Modifier,
    placeholder: Int,
    text: String,
    isTextFieldForPassword: Boolean = false,
    textChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(Color.Gray.copy(alpha = 0.2f))
            .border(
                width = 1.dp,
                color = if (isFocused) Color.Gray else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp)
            .height(56.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    val trimmedText = newText.trim()
                    if (trimmedText != text)
                        textChange(trimmedText)
                },
                modifier = Modifier
                    .weight(0.8f)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    color = colors.onSurface,
                    lineHeight = 56.sp,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp
                ),
                maxLines = 1,
                singleLine = true,
                cursorBrush = SolidColor(Color.Gray),
                visualTransformation = if (isTextFieldForPassword && !passwordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                keyboardOptions = if (isTextFieldForPassword) {
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        keyboardType = KeyboardType.Password
                    )
                } else {
                    KeyboardOptions(capitalization = KeyboardCapitalization.None)
                },
                decorationBox = { innerTextField ->
                    when {
                        text.isEmpty() -> {
                            Text(
                                text = stringResource(id = placeholder),
                                color = colors.onSurfaceVariant.copy(0.6f),
                                style = TextStyle(lineHeight = 56.sp),
                                fontWeight = FontWeight.W500,
                                fontSize = 14.sp,
                                letterSpacing = 2.sp,
                            )
                        }
                    }
                    innerTextField()
                }
            )
            if (isTextFieldForPassword && text.isNotEmpty()) {
                Icon(
                    painter = painterResource(
                        id = when {
                            passwordVisible -> {
                                R.drawable.ic_eye
                            }
                            else -> {
                                R.drawable.ic_eye_open
                            }
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(5.dp)
                        .size(22.dp)
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { passwordVisible = !passwordVisible }
                        )
                )
            }
        }
    }
}
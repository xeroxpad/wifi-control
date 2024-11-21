package com.example.wificontrol.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class FormatPhoneNumber: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val string = text.text
        val formattedString = buildString {
            for (i in string.indices) {
                append(string[i])
                when (i) {
                    2 -> append(" ")
                    5, 7 -> append("-")
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset + 1
                    offset <= 7 -> offset + 2
                    offset <= 9 -> offset + 3
                    else -> formattedString.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 6 -> offset - 1
                    offset <= 9 -> offset - 2
                    offset <= 11 -> offset - 3
                    else -> text.length
                }
            }
        }

        return TransformedText(
            AnnotatedString(formattedString),
            offsetMapping
        )
    }
}
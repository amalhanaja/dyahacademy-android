package com.amalcodes.dyahacademy.android.component

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.DrawableRes

class ConfirmationDialogViewEntity(
    val title: String,
    val description: String,
    @DrawableRes
    val drawableRes: Int = 0,
    val drawableTint: ColorStateList? = null,
    val cancelButton: Pair<String, (View) -> Unit>,
    val confirmButton: Pair<String, (View) -> Unit>
)
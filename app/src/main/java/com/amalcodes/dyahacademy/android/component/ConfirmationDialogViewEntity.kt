package com.amalcodes.dyahacademy.android.component

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.DrawableRes

class ConfirmationDialogViewEntity(
    val title: CharSequence,
    val description: CharSequence,
    @DrawableRes
    val drawableRes: Int = 0,
    val drawableTint: ColorStateList? = null,
    val cancelButton: Pair<CharSequence, (View) -> Unit>,
    val confirmButton: Pair<CharSequence, (View) -> Unit>
)
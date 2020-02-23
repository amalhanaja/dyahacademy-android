package com.amalcodes.dyahacademy.android.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConfirmationDialogViewModel : ViewModel() {

    var viewEntity: ConfirmationDialogViewEntity? = null

    object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ConfirmationDialogViewModel() as T
        }
    }

}
package com.haroldo.searchforflights

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(message: String, actionTitle: Int? = null, action: () -> Unit = {}) {
    with(Snackbar.make(this, message, Snackbar.LENGTH_LONG)) {
        actionTitle?.let {
            setAction(it) {
                action()
            }
        }
        show()
    }
}

fun View.changeVisibility(visible: Boolean) {
    this.visibility = if (visible) { View.VISIBLE } else View.GONE
}
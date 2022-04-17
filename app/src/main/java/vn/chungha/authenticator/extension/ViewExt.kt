package vn.chungha.authenticator.extension

import android.view.View
import vn.chungha.authenticator.utils.SafeOnClickListener

fun View.onClick(safe: Boolean = true, action: (View) -> Unit) = setOnClickListener(
    SafeOnClickListener(safe, action)
)
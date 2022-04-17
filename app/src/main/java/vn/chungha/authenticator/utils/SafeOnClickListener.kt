package vn.chungha.authenticator.utils

import android.view.View
import vn.chungha.authenticator.extension.nowUtcMillis

private const val SAFE_INTERVAL = 750

class SafeOnClickListener(
    private val isSafe: Boolean,
    private val action: (view: View) -> Unit
) : View.OnClickListener {
    private var lastClickTimestamp = 0L

    override fun onClick(clickedView: View) {
        if (!isSafe) {
            action(clickedView)
            return
        }

        val currentTimestamp = nowUtcMillis()
        if (lastClickTimestamp == 0L || currentTimestamp - lastClickTimestamp > SAFE_INTERVAL) {
            action(clickedView)
            lastClickTimestamp = currentTimestamp
        }
    }
}
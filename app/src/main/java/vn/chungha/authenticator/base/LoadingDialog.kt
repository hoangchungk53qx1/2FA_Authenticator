package vn.chungha.authenticator.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import vn.chungha.authenticator.R

class LoadingDialog(context: Context) : Dialog(context) {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.loading_state_layout)
    setCancelable(false)
    setCanceledOnTouchOutside(false)
    window?.setBackgroundDrawableResource(R.color.transparent)
  }
}

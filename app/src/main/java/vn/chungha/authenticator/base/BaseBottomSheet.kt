package vn.chungha.authenticator.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<B : ViewBinding> : BottomSheetDialogFragment() {
  private var viewBinding: B? = null

  protected val binding: B
    get() = checkNotNull(viewBinding)

  final override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val binding = onInflateView(inflater, container)
    viewBinding = binding
    return binding.root
  }

  override fun onDestroyView() {
    viewBinding = null
    super.onDestroyView()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return super.onCreateDialog(savedInstanceState)
  }

  fun showLoading(isShow: Boolean) {
    (activity as? BaseActivity<*>)?.showLoading(isShow)
  }

  private fun showAlertError(message: String) {
    BaseAlertError.show(childFragmentManager, message)
  }

  protected abstract fun onInflateView(inflater: LayoutInflater, container: ViewGroup?): B

}

package vn.chungha.authenticator.customview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import com.google.android.material.card.MaterialCardView
import vn.chungha.authenticator.R
import vn.chungha.authenticator.databinding.ItemTokenLayoutBuilderBinding
import vn.chungha.authenticator.extension.gone
import vn.chungha.authenticator.extension.onLongClick
import vn.chungha.authenticator.extension.visible
import vn.chungha.authenticator.model.OtpTokenType
import vn.chungha.authenticator.model.Token
import vn.chungha.authenticator.model.TokenCode

class TokenLayoutBuilder(context: Context) : MaterialCardView(context), Runnable {
    private var binding: ItemTokenLayoutBuilderBinding
    private var mTokenCode: TokenCode? = null
    private var mTokenType: OtpTokenType? = null
    private var mPlaceHolder: String? = null
    private var mStartTime: Long = 0

    init {
        binding = ItemTokenLayoutBuilderBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bindData(token: Token?, onLongClick: (Token?) -> Unit) {
        mTokenCode = null
        // Cancel all active animations.
        isEnabled = true
        binding.circularTimer.gone()
        val tokenData = token ?: return
        val placeHolder = tokenData.digits?.let { CharArray(it) }
        for (i in placeHolder!!.indices) {
            placeHolder[i] = '*'
        }
        mPlaceHolder = String(placeHolder)

        // set content
        binding.tvCode.text = mPlaceHolder
        binding.root.onLongClick { onLongClick.invoke(token) }
    }

    private fun animate(view: View, anim: Int, animate: Boolean) {
        val a = AnimationUtils.loadAnimation(view.context, anim)
        if (!animate)
            a.duration = 0
        view.startAnimation(a)
    }

    fun start(type: OtpTokenType, codes: TokenCode, animate: Boolean) {
        mTokenCode = codes
        mTokenType = type
        binding.circularTimer.visible()
        animate(binding.circularTimer, R.anim.token_image_fadeout, animate)
        isEnabled = when (type) {
            OtpTokenType.HOTP -> true
            OtpTokenType.TOTP -> {
                true
            }
        }
        mStartTime = System.currentTimeMillis()
        post(this)
    }

    override fun run() {
        val code = mTokenCode?.currentCode ?: run {
            binding.tvCode.text = mPlaceHolder
            binding.circularTimer.gone()
            return
        }

        val currentProgress = mTokenCode?.currentProgress ?: run {
            return
        }

        val totalProgress = mTokenCode?.totalProgress ?: run {
            return
        }
        if (!isEnabled)
            isEnabled = System.currentTimeMillis() - mStartTime > 5000
        binding.tvCode.text = code
        binding.circularTimer.progress = totalProgress.toFloat()
        postDelayed(this, 100)

    }
}
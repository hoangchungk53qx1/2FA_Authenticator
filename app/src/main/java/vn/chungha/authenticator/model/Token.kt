package vn.chungha.authenticator.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.PrimaryKey

data class Token(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val ordinal: Long,
    val issuer: String? = null,
    val label: String? = null,
    val imagePath: String? = null,
    val tokenType: OtpTokenType? = null,
    val algorithm: String? = null,
    val secret: String? = null,
    val digits: Int? = null,
    val counter: Long? = null,
    val period: Int? = null,
    val encryptionType: EncryptionType? = null
)
val TOKEN_MODEL_DIFF = TokenModelItemBack()

class TokenModelItemBack :  DiffUtil.ItemCallback<Token>() {
    override fun areItemsTheSame(oldItem: Token, newItem: Token): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Token, newItem: Token): Boolean {
        return  oldItem == newItem
    }

}
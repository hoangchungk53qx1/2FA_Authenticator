package vn.chungha.authenticator.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vn.chungha.authenticator.databinding.ItemTokenBinding
import vn.chungha.authenticator.model.TOKEN_MODEL_DIFF
import vn.chungha.authenticator.model.Token

class HomeAdapter(private val onClick: (Token) -> Unit) :
    ListAdapter<Token, HomeAdapter.ListTokenVH>(TOKEN_MODEL_DIFF) {
    inner class ListTokenVH(val view: ItemTokenBinding) : RecyclerView.ViewHolder(view.root) {
        internal fun bindData(token: Token) {
            view.neuCardToken.setOnClickListener {
                onClick.invoke(token)
            }
            with(view) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTokenVH {
        return ListTokenVH(
            ItemTokenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListTokenVH, position: Int) {
        holder.bindData(getItem(position))
    }
}
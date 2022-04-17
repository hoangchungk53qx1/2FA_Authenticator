package vn.chungha.authenticator.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<Item, B : ViewBinding>(
  data: List<Item>
) : RecyclerView.Adapter<BaseAdapter.ItemVH<B>>() {

  protected var currentList: MutableList<Item> = mutableListOf()

  init {
    currentList = data.toMutableList()
  }

  abstract fun onCreateVH(parent: ViewGroup, viewType: Int): ItemVH<B>

  abstract fun onBindVH(holder: ItemVH<B>, position: Int)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH<B> =
    onCreateVH(parent, viewType)

  override fun onBindViewHolder(holder: ItemVH<B>, position: Int) = onBindVH(holder, position)

  override fun getItemCount(): Int = currentList.size

  protected open fun getItem(position: Int) = currentList[position]

  @SuppressLint("NotifyDataSetChanged")
  open fun submitList(list: List<Item>) {
    currentList.clear()
    currentList.addAll(list)
    notifyDataSetChanged()
  }

  class ItemVH<B : ViewBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)
}

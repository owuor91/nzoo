package io.owuor91.nzooimport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.owuor91.nzoo.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.text_list_item.view.checkBox

class ListRvAdapter : RecyclerView.Adapter<ListRvAdapter.StringViewholder>() {
  var data: List<String> = emptyList()
    set(newList) {
      field = newList
    }
  
  override fun getItemCount(): Int = data.size
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewholder {
    return StringViewholder(
      LayoutInflater.from(parent.context)
        .inflate(R.layout.text_list_item, parent, false)
    )
  }
  
  override fun onBindViewHolder(holder: ListRvAdapter.StringViewholder, position: Int) {
    holder.bind(data[position])
  }
  
  inner class StringViewholder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(item: String) = with(itemView) {
      checkBox.text = item
    }
  }
}

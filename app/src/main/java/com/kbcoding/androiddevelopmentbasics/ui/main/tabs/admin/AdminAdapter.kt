package com.kbcoding.androiddevelopmentbasics.ui.main.tabs.admin

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.ItemTreeElementBinding
import com.kbcoding.androiddevelopmentbasics.model.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.ui.main.tabs.settings.SettingsAdapter

class AdminAdapter(
    private val listener: Listener
) : ListAdapter<AdminTreeItem, AdminAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTreeElementBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: AdminAdapter.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == true) {
                holder.bind(getItem(position))
            }
        }
    }

    inner class ViewHolder(
        val binding: ItemTreeElementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: AdminTreeItem

        fun bind(item: AdminTreeItem) {

            this.item = item

            with(binding) {
                attributesTextView.text = prepareAttributesText()
                expandCollapseIndicatorImageView.setImageResource(getExpansionIcon())
                adjustStartOffset(attributesTextView)
                binding.root.isClickable = item.expansionStatus != ExpansionStatus.NOT_EXPANDABLE
            }

            itemView.setOnClickListener {
                listener.onItemToggled(item)
            }
        }

        private fun prepareAttributesText(): CharSequence {
            // just a bit of HTML for easier implementation.
            // please note it may work bad on devices with small screens
            val attributesText = item.attributes.entries.joinToString("<br>") {
                if (it.value.isNotBlank()) {
                    "<b>${it.key}</b>: ${it.value}"
                } else {
                    "<b>${it.key}</b>"
                }
            }
            return Html.fromHtml(attributesText)
        }

        @DrawableRes
        private fun getExpansionIcon(): Int {
            return when (item.expansionStatus) {
                ExpansionStatus.EXPANDED -> R.drawable.ic_minus
                ExpansionStatus.COLLAPSED -> R.drawable.ic_plus
                else -> R.drawable.ic_dot
            }
        }

        private fun adjustStartOffset(attributesTextView: TextView) {
            val context = attributesTextView.context
            val spacePerLevel = context.resources.getDimensionPixelSize(R.dimen.tree_level_size)
            val totalSpace = (item.level + 1) * spacePerLevel

            val layoutParams = attributesTextView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginStart = totalSpace
            attributesTextView.layoutParams = layoutParams
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<AdminTreeItem>() {
        override fun areItemsTheSame(
            oldItem: AdminTreeItem,
            newItem: AdminTreeItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AdminTreeItem,
            newItem: AdminTreeItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: AdminTreeItem, newItem: AdminTreeItem): Any? {
            return if (oldItem.expansionStatus != newItem.expansionStatus) true else null
        }
    }

    interface Listener {
        /**
         * Called when user toggles the specified item.
         */
        fun onItemToggled(item: AdminTreeItem)
    }
}
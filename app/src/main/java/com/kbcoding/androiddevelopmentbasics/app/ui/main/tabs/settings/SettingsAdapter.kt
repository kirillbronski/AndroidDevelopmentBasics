package com.kbcoding.androiddevelopmentbasics.app.ui.main.tabs.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.databinding.ItemSettingsBinding

class SettingsAdapter(
    private val listener: Listener
) : ListAdapter<BoxAndSettings, SettingsAdapter.Holder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingsBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == true) {
                holder.bind(getItem(position))
            }
        }
    }

    inner class Holder(
        val binding: ItemSettingsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: BoxAndSettings
        private val context = binding.root.context
        fun bind(item: BoxAndSettings) {

            this.item = item

            if (binding.checkbox.isChecked != item.isActive) {
                binding.checkbox.isChecked = item.isActive
            }

            val colorName = item.box.colorName
            binding.checkbox.text = context.getString(R.string.enable_checkbox, colorName)

            itemView.setOnClickListener {
                if (binding.checkbox.isChecked) {
                    listener.enableBox(item.box)
                } else {
                    listener.disableBox(item.box)
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<BoxAndSettings>() {
        override fun areItemsTheSame(
            oldItem: BoxAndSettings,
            newItem: BoxAndSettings
        ): Boolean {
            return oldItem.box.id == newItem.box.id
        }

        override fun areContentsTheSame(
            oldItem: BoxAndSettings,
            newItem: BoxAndSettings
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: BoxAndSettings, newItem: BoxAndSettings): Any? {
            return if (oldItem.isActive != newItem.isActive) true else null
        }
    }

    interface Listener {
        fun enableBox(box: Box)
        fun disableBox(box: Box)
    }
}
package com.kbcoding.androiddevelopmentbasics.presentation.main.tabs.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.ItemSettingsBinding
import com.kbcoding.androiddevelopmentbasics.domain.model.Box

class SettingsAdapter(
    private val listener: Listener
) : ListAdapter<BoxSetting, SettingsAdapter.Holder>(DiffCallback) {


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

        private lateinit var item: BoxSetting
        private val context = binding.root.context
        fun bind(item: BoxSetting) {

            this.item = item

            if (binding.checkbox.isChecked != item.enabled) {
                binding.checkbox.isChecked = item.enabled
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

    object DiffCallback : DiffUtil.ItemCallback<BoxSetting>() {
        override fun areItemsTheSame(
            oldItem: BoxSetting,
            newItem: BoxSetting
        ): Boolean {
            return oldItem.box.id == newItem.box.id
        }

        override fun areContentsTheSame(
            oldItem: BoxSetting,
            newItem: BoxSetting
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: BoxSetting, newItem: BoxSetting): Any? {
            return if (oldItem.enabled != newItem.enabled) true else null
        }
    }

    interface Listener {
        fun enableBox(box: Box)
        fun disableBox(box: Box)
    }
}
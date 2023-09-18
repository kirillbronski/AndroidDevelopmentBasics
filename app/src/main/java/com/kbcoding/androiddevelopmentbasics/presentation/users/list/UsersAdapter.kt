package com.kbcoding.androiddevelopmentbasics.presentation.users.list

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.databinding.ItemUserBinding
import com.kbcoding.androiddevelopmentbasics.model.User
import com.kbcoding.androiddevelopmentbasics.model.UserListItem

class UsersAdapter(
    private val userActionListener: UserActionListener
) : ListAdapter<UserListItem, UsersAdapter.UsersViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<UserListItem>() {
        override fun areItemsTheSame(
            oldItem: UserListItem,
            newItem: UserListItem
        ): Boolean {
            return oldItem.user.id == newItem.user.id
        }

        override fun areContentsTheSame(
            oldItem: UserListItem,
            newItem: UserListItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: UserListItem

        fun bind(userListItem: UserListItem) {

            this.item = userListItem

            updateItemUi()
        }

        private fun updateItemUi() {
            with(binding) {
                val context = userNameTextView.context
                userNameTextView.text = item.user.name
                userCompanyTextView.text =
                    if (item.user.company.isNotBlank()) item.user.company else context.getString(R.string.unemployed)
                if (item.user.photo.isNotBlank()) {
                    Glide.with(context)
                        .load(item.user.photo)
                        .circleCrop()
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .error(R.drawable.baseline_account_circle_24)
                        .into(photoImageView)
                } else {
                    Glide.with(context).clear(photoImageView)
                    photoImageView.setImageResource(R.drawable.baseline_account_circle_24)
                    // you can also use the following code instead of these two lines ^
                    // Glide.with(photoImageView.context)
                    //        .load(R.drawable.ic_user_avatar)
                    //        .into(photoImageView)
                }
                val isInProgress = item.isInProgress
                if (isInProgress) {
                    binding.moreImageViewButton.visibility = View.INVISIBLE
                    binding.itemProgressBar.visibility = View.VISIBLE
                    initListeners()
                } else {
                    binding.moreImageViewButton.visibility = View.VISIBLE
                    binding.itemProgressBar.visibility = View.GONE
                    initListeners()
                }
            }
        }

        private fun initListeners() {
            itemView.setOnClickListener {
                if (item.isInProgress) return@setOnClickListener
                userActionListener.onUserDetails(item.user)
            }
            binding.moreImageViewButton.setOnClickListener {
                if (item.isInProgress) return@setOnClickListener
                showPopupMenu(it)
            }
        }

        private fun showPopupMenu(view: View) {
            val context = view.context
            val popupMenu = PopupMenu(context, view)
            val position = currentList.indexOfFirst { it.user.id == item.user.id }

            popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up))
                .apply {
                    isEnabled = position > 0
                }
            popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down))
                .apply {
                    isEnabled = position < currentList.size - 1
                }
            popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

            if (item.user.company.isNotBlank()) {
                popupMenu.menu.add(0, ID_FIRE, Menu.NONE, context.getString(R.string.fire))
            }

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    ID_MOVE_UP -> {
                        userActionListener.onUserMove(item.user, -1)
                    }

                    ID_MOVE_DOWN -> {
                        userActionListener.onUserMove(item.user, 1)
                    }

                    ID_REMOVE -> {
                        userActionListener.onUserDelete(item.user)
                    }

                    ID_FIRE -> {
                        userActionListener.onUserFire(item.user)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val ID_FIRE = 4
    }
}
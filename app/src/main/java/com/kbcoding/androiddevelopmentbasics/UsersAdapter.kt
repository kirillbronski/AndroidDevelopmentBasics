package com.kbcoding.androiddevelopmentbasics

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kbcoding.androiddevelopmentbasics.databinding.ItemUserBinding
import com.kbcoding.androiddevelopmentbasics.model.User

class UsersAdapter(
    private val userActionListener: UserActionListener
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    interface UserActionListener {
        fun onUserMove(user: User, moveBy: Int)

        fun onUserDelete(user: User)

        fun onUserDetails(user: User)
    }

    var usersList: List<User> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(usersList[position])
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    inner class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: User

        fun bind(user: User) {

            this.item = user

            updateItemUi()
            initListeners()
        }

        private fun updateItemUi() {
            with(binding) {
                userNameTextView.text = item.name
                userCompanyTextView.text = item.company
                if (item.photo.isNotBlank()) {
                    Glide.with(photoImageView.context)
                        .load(item.photo)
                        .circleCrop()
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .error(R.drawable.baseline_account_circle_24)
                        .into(photoImageView)
                } else {
                    Glide.with(photoImageView.context).clear(photoImageView)
                    photoImageView.setImageResource(R.drawable.baseline_account_circle_24)
                }
            }
        }

        private fun initListeners() {
            itemView.setOnClickListener {
                userActionListener.onUserDetails(item)
            }
            binding.moreImageViewButton.setOnClickListener {
                showPopupMenu(it)
            }
        }

        private fun showPopupMenu(view: View) {
            val context = view.context
            val popupMenu = PopupMenu(context, view)
            val position = usersList.indexOfFirst { it.id == item.id }

            popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up))
                .apply {
                    isEnabled = position > 0
                }
            popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down))
                .apply {
                    isEnabled = position < usersList.size - 1
                }
            popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    ID_MOVE_UP -> {
                        userActionListener.onUserMove(item, -1)
                    }

                    ID_MOVE_DOWN -> {
                        userActionListener.onUserMove(item, 1)
                    }

                    ID_REMOVE -> {
                        userActionListener.onUserDelete(item)
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
    }
}
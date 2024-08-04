package com.tta.phonebookapplication.ui.component.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tta.phonebookapplication.R
import com.tta.phonebookapplication.data.model.Contact
import com.tta.phonebookapplication.databinding.LayoutItemContactBinding

class ContactAdapter :
    RecyclerView.Adapter<ContactAdapter.ItemContactViewHolder>() {
    private var listContact: List<Contact> = listOf()
    private lateinit var context: Context

    @SuppressLint("NotifyDataSetChanged")
    fun setListContact(listContact: List<Contact>, context: Context) {
        this.listContact = listContact
        this.context = context
        notifyDataSetChanged()
    }

    private var onClickDeleteData: ((i: Int) -> Unit)? = null
    fun deleteItem(position: ((i: Int) -> Unit)) {
        onClickDeleteData = position
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemContactBinding.inflate(inflater, parent, false)
        return ItemContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemContactViewHolder, position: Int) {
        val data = listContact[position]

        holder.binding.tvName.text = data.name
        holder.binding.tvPhone.text = "Phone: ${data.phone}"
        holder.binding.tvEmail.text = "Email: ${data.email}"

        var expand = false
        holder.binding.imgExpand.setOnClickListener {
            if (expand) {
                expand = false
                holder.binding.imgExpand.setImageResource(R.drawable.baseline_expand_more_24)
                holder.binding.tvPhone.isVisible = false
                holder.binding.tvEmail.isVisible = false
            } else {
                expand = true
                holder.binding.imgExpand.setImageResource(R.drawable.baseline_expand_less_24)
                holder.binding.tvPhone.isVisible = true
                holder.binding.tvEmail.isVisible = true
            }
        }

//        holder.binding.itemContact.setOnClickListener {
//            onClickDeleteData?.let {
//                it(position)
//            }
//        }
    }

    override fun getItemCount(): Int {
        return listContact.size
    }

    class ItemContactViewHolder(
        val binding: LayoutItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
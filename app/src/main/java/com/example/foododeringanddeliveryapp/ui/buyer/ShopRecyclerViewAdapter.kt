package com.joytekmotion.yemilicious.ui.buyer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joytekmotion.yemilicious.R
import com.joytekmotion.yemilicious.models.Shop
import com.joytekmotion.yemilicious.models.User
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.shops_list.view.*

class ShopViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(shop: Shop?, seller: User, listener: ShopRecyclerViewAdapter.OnShopClickListener) {
        containerView.txtShopName.text = shop?.name
        containerView.txtShopAddress.text = shop?.address

        containerView.shopListLayout.setOnClickListener {
            listener.onClickShop(seller)
        }
    }
}

class ShopRecyclerViewAdapter(
    private var users: ArrayList<User>?,
    private val listener: OnShopClickListener
) : RecyclerView.Adapter<ShopViewHolder>() {
    interface OnShopClickListener {
        fun onClickShop(seller: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shops_list, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val users = users
        if (!users.isNullOrEmpty()) {
            val user = users[position]
            val shop = user.shop
            holder.bind(shop, user, listener)
        }
    }

    override fun getItemCount(): Int {
        return if (!users.isNullOrEmpty()) users!!.size else 0
    }

    fun updateList(newUsers: ArrayList<User>?) {
        if (newUsers != users) {
            val numItems = itemCount
            users = newUsers
            if (newUsers != null) notifyDataSetChanged()
            else notifyItemRangeRemoved(0, numItems)
        }
    }
}
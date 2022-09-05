package com.astro.test.aqil.features.dashboard.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astro.R
import com.astro.test.aqil.features.dashboard.data.dto.ProfilesDto
import kotlinx.android.synthetic.main.item_list_profile.view.*

class ProfilesAdapter (
    private val context: Context,
    private val listProfiles: List<ProfilesDto>,
    private val listProfilesDb: List<String>
    , val onItemCallBack: OnItemCallBack
) : RecyclerView.Adapter<ProfilesAdapter.Holder>(){

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_profile, parent, false))
    }

    override fun getItemCount(): Int = listProfiles.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val findData = listProfilesDb.find { s -> s == listProfiles[position].login }
        if (!findData.isNullOrEmpty()) { holder.view.img_fav.setImageResource(R.drawable.ic_favorite_filled) }
        else { holder.view.img_fav.setImageResource(R.drawable.ic_favorite_blank) }

        holder.view.tv_name.text = listProfiles[position].login

        holder.view.img_fav.setOnClickListener {
            onItemCallBack.onItemClicked(position, listProfiles[position])
        }
    }

    interface OnItemCallBack {
        fun onItemClicked(position: Int, profile: ProfilesDto?)
    }
}
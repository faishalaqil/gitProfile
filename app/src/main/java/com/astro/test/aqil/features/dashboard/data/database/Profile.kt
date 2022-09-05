package com.astro.test.aqil.features.dashboard.data.database

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class Profile(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var login: String? = null,
//    val id: String? = null,
//    val node_id: String? = null,
//    val avatar_url: String? = null,
//    val flag: Boolean? = null,
)
package com.example.paraf_test_project.model

import com.google.gson.annotations.SerializedName

data class VenueResponse(
    @SerializedName("response")
    val response: Response? = null
)

data class Response(
    @SerializedName("groups")
    val group: List<Group>? = null
)

data class Group(
    @SerializedName("items")
    val item: List<Item>? = null
)

data class Item(
    val venue: Venue? = null
)






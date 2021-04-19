package com.tapi.android.example.service.models

import com.google.gson.annotations.SerializedName
import java.util.*

class Urls {
    @SerializedName("raw")
    var raw: String? = null
    @SerializedName("full")
    var full: String? = null
    @SerializedName("regular")
    var regular: String? = null
    @SerializedName("small")
    var small: String? = null
    @SerializedName("thumb")
    var thumb: String? = null
}

class Links {
    @SerializedName("self")
    var self: String? = null
    @SerializedName("html")
    var html: String? = null
    @SerializedName("download")
    var download: String? = null
    @SerializedName("download_location")
    var download_location: String? = null
    @SerializedName("photos")
    var photos: String? = null
    @SerializedName("likes")
    var likes: String? = null
    @SerializedName("portfolio")
    var portfolio: String? = null
    @SerializedName("following")
    var following: String? = null
    @SerializedName("followers")
    var followers: String? = null
}

class ProfileImage {
    @SerializedName("small")
    var small: String? = null
    @SerializedName("medium")
    var medium: String? = null
    @SerializedName("large")
    var large: String? = null
}

class Sponsor {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("updated_at")
    var updated_at: Date? = null
    @SerializedName("username")
    var username: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("first_name")
    var first_name: String? = null
    @SerializedName("last_name")
    var last_name: Any? = null
    @SerializedName("twitter_username")
    var twitter_username: String? = null
    @SerializedName("portfolio_url")
    var portfolio_url: String? = null
    @SerializedName("bio")
    var bio: String? = null
    @SerializedName("location")
    var location: String? = null
    @SerializedName("links")
    var links: Links? = null
    @SerializedName("profile_image")
    var profile_image: ProfileImage? = null
    @SerializedName("instagram_username")
    var instagram_username: String? = null
    @SerializedName("total_collections")
    var total_collections = 0
    @SerializedName("total_likes")
    var total_likes = 0
    @SerializedName("total_photos")
    var total_photos = 0
    @SerializedName("accepted_tos")
    var accepted_tos = false
    @SerializedName("for_hire")
    var for_hire = false
}

class Sponsorship {
    @SerializedName("impression_urls")
    var impression_urls: List<String>? = null
    @SerializedName("tagline")
    var tagline: String? = null
    @SerializedName("tagline_url")
    var tagline_url: String? = null
    @SerializedName("sponsor")
    var sponsor: Sponsor? = null
}

class User {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("updated_at")
    var updated_at: Date? = null
    @SerializedName("username")
    var username: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("first_name")
    var first_name: String? = null
    @SerializedName("last_name")
    var last_name: String? = null
    @SerializedName("twitter_username")
    var twitter_username: String? = null
    @SerializedName("portfolio_url")
    var portfolio_url: String? = null
    @SerializedName("bio")
    var bio: String? = null
    @SerializedName("location")
    var location: String? = null
    @SerializedName("links")
    var links: Links? = null
    @SerializedName("profile_image")
    var profile_image: ProfileImage? = null
    @SerializedName("instagram_username")
    var instagram_username: String? = null
    @SerializedName("total_collections")
    var total_collections = 0
    @SerializedName("total_likes")
    var total_likes = 0
    @SerializedName("total_photos")
    var total_photos = 0
    @SerializedName("accepted_tos")
    var accepted_tos = false
    @SerializedName("for_hire")
    var for_hire = false
}

class RootObject {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("created_at")
    var created_at: Date? = null
    @SerializedName("updated_at")
    var updated_at: Date? = null
    @SerializedName("promoted_at")
    var promoted_at: Date? = null
    @SerializedName("width")
    var width = 0
    @SerializedName("height")
    var height = 0
    @SerializedName("color")
    var color: String? = null
    @SerializedName("blur_hash")
    var blur_hash: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("alt_description")
    var alt_description: String? = null
    @SerializedName("urls")
    var urls: Urls? = null
    @SerializedName("links")
    var links: Links? = null
    @SerializedName("categories")
    var categories: List<Any>? = null
    @SerializedName("likes")
    var likes = 0
    @SerializedName("liked_by_user")
    var liked_by_user = false
    @SerializedName("current_user_collections")
    var current_user_collections: List<Any>? = null
    @SerializedName("sponsorship")
    var sponsorship: Sponsorship? = null
    @SerializedName("user")
    var user: User? = null
}

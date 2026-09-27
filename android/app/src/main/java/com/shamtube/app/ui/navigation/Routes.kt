package com.shamtube.app.ui.navigation

object Routes {
    const val Home = "home"
    const val Shorts = "shorts"
    const val Upload = "upload"
    const val Subscriptions = "subscriptions"
    const val Library = "library"
    const val Search = "search"
    const val Notifications = "notifications"
    const val Settings = "settings"
    const val Channel = "channel/{channelId}"
    const val Player = "player/{videoId}"
    const val Comments = "comments/{videoId}"
    const val Login = "login"
    const val Register = "register"
    const val ForgotPassword = "forgot-password"

    fun channel(channelId: String) = "channel/$channelId"
    fun player(videoId: String) = "player/$videoId"
    fun comments(videoId: String) = "comments/$videoId"
}
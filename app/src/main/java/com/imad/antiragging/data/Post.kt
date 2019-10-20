package com.imad.antiragging.data

import com.google.firebase.Timestamp

data class Post (
        var name: String = "",
        var date: Timestamp = Timestamp.now(),
        var post: String = "",
        var image: String = "",
        var userid: String = ""
)
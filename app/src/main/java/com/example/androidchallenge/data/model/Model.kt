package com.example.androidchallenge.data.model

import com.google.firebase.firestore.Exclude

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
open class Model {
    @Exclude
    var id: String? = null
}
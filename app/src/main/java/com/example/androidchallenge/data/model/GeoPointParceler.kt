package com.example.androidchallenge.data.model

import android.os.Parcel
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parceler

object GeoPointParceler : Parceler<GeoPoint?> {
    override fun create(parcel: Parcel) = GeoPoint(parcel.readDouble(), parcel.readDouble())

    override fun GeoPoint?.write(parcel: Parcel, flags: Int) {
        this?.let {
            parcel.writeDouble(latitude)
            parcel.writeDouble(longitude)
        }
    }
}
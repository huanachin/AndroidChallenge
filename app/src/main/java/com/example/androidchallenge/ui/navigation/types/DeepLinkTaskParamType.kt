package com.example.androidchallenge.ui.navigation.types

import android.os.Bundle
import androidx.navigation.NavType
import com.example.androidchallenge.data.model.DeepLinkTaskModel
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.ui.util.extractModel

class DeepLinkTaskParamType : NavType<DeepLinkTaskModel?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): DeepLinkTaskModel? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): DeepLinkTaskModel? {
        return value.extractModel()
    }

    override fun put(bundle: Bundle, key: String, value: DeepLinkTaskModel?) {
        bundle.putParcelable(key, value)
    }
}
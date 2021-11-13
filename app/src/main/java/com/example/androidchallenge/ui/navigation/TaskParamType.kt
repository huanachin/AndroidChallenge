package com.example.androidchallenge.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.ui.util.extractModel
import com.google.gson.Gson

class TaskParamType : NavType<TaskModel?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): TaskModel? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): TaskModel? {
        return value.extractModel()
    }

    override fun put(bundle: Bundle, key: String, value: TaskModel?) {
        bundle.putParcelable(key, value)
    }


}
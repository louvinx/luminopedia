package com.luminopedia

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class TaskStorage(private val context: Context) {
    private val gson = Gson()
    private val fileName = "tasks.json"

    fun saveTasks(tasks: List<Task>) {
        try {
            val jsonString = gson.toJson(tasks)
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                output.write(jsonString.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadTasks(): List<Task> {
        return try {
            context.openFileInput(fileName).use { input ->
                val type = object : TypeToken<List<Task>>() {}.type
                val jsonString = input.bufferedReader().use { it.readText() }
                gson.fromJson(jsonString, type) ?: emptyList()
            }
        } catch (e: FileNotFoundException) {
            emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}
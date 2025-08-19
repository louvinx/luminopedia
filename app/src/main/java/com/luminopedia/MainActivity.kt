package com.luminopedia

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button

class MainActivity : AppCompatActivity() {
    private lateinit var taskStorage: TaskStorage
    private val tasks = mutableListOf<Task>()
    private var nextId = 1
    private lateinit var adapter: TaskAdapter
    private lateinit var buttonAdd: Button
    private lateinit var editTextTask: EditText
    private lateinit var listViewTasks: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation avec findViewById
        buttonAdd = findViewById<Button>(R.id.buttonAdd)
        editTextTask = findViewById<EditText>(R.id.editTextTask)
        listViewTasks = findViewById<ListView>(R.id.listViewTasks)

        taskStorage = TaskStorage(this)

        // Charger les tâches existantes
        tasks.addAll(taskStorage.loadTasks())
        nextId = if (tasks.isNotEmpty()) tasks.maxBy { it.id }.id + 1 else 1

        // Configurer l'adapteur
        adapter = TaskAdapter(
            this,
            R.layout.task_item,
            tasks,
            onTaskUpdated = { notifyDataSetChanged() },
            taskStorage
        )

        listViewTasks.adapter = adapter

        // Ajouter une nouvelle tâche
        buttonAdd.setOnClickListener {
            val taskTitle = editTextTask.text.toString().trim()
            if (taskTitle.isNotEmpty()) {
                val newTask = Task(nextId++, taskTitle)
                tasks.add(newTask)
                taskStorage.saveTasks(tasks)
                adapter.notifyDataSetChanged()
                editTextTask.text.clear()
            }
        }
    }

    private fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }
}
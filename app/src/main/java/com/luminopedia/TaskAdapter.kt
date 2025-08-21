package com.luminopedia

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Button
import android.widget.TextView

class TaskAdapter(
    context: Context,
    private val resource: Int,
    private val tasks: MutableList<Task>,
    private val onTaskUpdated: () -> Unit,
    private val taskStorage: TaskStorage
) : ArrayAdapter<Task>(context, resource, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val task = getItem(position) ?: return view

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTaskTitle)
        val checkBoxTask = view.findViewById<CheckBox>(R.id.checkBoxTask)
        val buttonEdit = view.findViewById<Button>(R.id.buttonEdit)
        val buttonDelete = view.findViewById<Button>(R.id.buttonDelete)

        textViewTitle.text = task.title
        checkBoxTask.isChecked = task.isCompleted

        checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            taskStorage.saveTasks(tasks)
            onTaskUpdated()
        }

        buttonEdit.setOnClickListener {
            showEditDialog(task, position)
        }

        buttonDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Supprimer la tâche")
                .setMessage("Voulez-vous vraiment supprimer cette tâche ?")
                .setPositiveButton("Supprimer") { _, _ ->
                    tasks.removeAt(position)
                    taskStorage.saveTasks(tasks)
                    onTaskUpdated()
                }
                .setNegativeButton("Annuler", null)
                .show()
        }

        return view
    }

    private fun showEditDialog(task: Task, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null)
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTaskTitle)
        val checkBoxCompleted = dialogView.findViewById<CheckBox>(R.id.checkBoxCompleted)

        editTextTitle.setText(task.title)
        checkBoxCompleted.isChecked = task.isCompleted

        AlertDialog.Builder(context)
            .setTitle("Modifier la tâche")
            .setView(dialogView)
            .setPositiveButton("Sauvegarder") { _, _ ->
                task.title = editTextTitle.text.toString()
                task.isCompleted = checkBoxCompleted.isChecked
                taskStorage.saveTasks(tasks)
                onTaskUpdated()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}

package com.example.plantin.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class NotesViewModel : ViewModel() {

    val reportDao = MainApplication.database.getNotesdao()

    val notesList: LiveData<List<Notes>> = reportDao.getAllNotes()

    fun addReport(title: String, description: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            reportDao.addNotes(
                Notes(
                    title = title,
                    description = description,
                    createdAt = Date(System.currentTimeMillis())
                )
            )
        }

    }

    fun deleteReport(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            reportDao.deleteNotes(id)
        }
    }
}
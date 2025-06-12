package com.example.plantin.notes.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.plantin.notes.Notes

@Dao
interface NotesDao {

    @Query("SELECT * FROM NOTES")
    fun getAllNotes(): LiveData<List<Notes>>

    @Insert
    fun addNotes(notes: Notes)

    @Query("Delete FROM Notes where id = :id")
    fun deleteNotes(id: Int)


}
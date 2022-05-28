package com.example.rpicommunicator_v1.Database.Note.second_level

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note?)

    @Update
    fun update(note: Note?)

    @Delete
    fun delete(note: Note?)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("DELETE FROM note_table WHERE idListe==:id")
    fun deleteAllNotesListe(id: Int)

    @get:Query("SELECT* FROM NOTE_TABLE ORDER BY priority DESC")
    val allNotes: LiveData<List<Note>>

    @Query("SELECT* FROM NOTE_TABLE WHERE idListe==:listeNummer ORDER BY priority DESC")
    fun getListNotes(listeNummer: Int): LiveData<List<Note>>
}
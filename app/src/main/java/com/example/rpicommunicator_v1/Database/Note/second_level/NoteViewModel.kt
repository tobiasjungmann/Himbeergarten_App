package com.example.rpicommunicator_v1.Database.Note.second_level

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>


    init {
        repository = NoteRepository(application)
        allNotes = repository.getAllNotes()
    }

    fun insert(note: Note?) {
        repository.insert(note)
    }

    fun update(note: Note?) {
        repository.update(note)
    }

    fun delete(note: Note?) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun deleteAllNotesListe(id: Int) {
        repository.deleteAllNotesListe(id)
    }

    fun getListeNotes(listeID: Int): LiveData<List<Note>> {
        return repository.getListeNotes(listeID)
    }

    fun getListNotesByPosition(position: Int): Note{
        return allNotes.value!![position]
    }
}
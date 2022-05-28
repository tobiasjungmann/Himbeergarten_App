package com.example.rpicommunicator_v1.Database.Note.second_level

import android.app.Application
import com.example.rpicommunicator_v1.Database.Note.second_level.NoteDatabase.Companion.getInstance
import androidx.lifecycle.LiveData


class NoteRepository(application: Application?) {
    private val notedao: NoteDao
    val allNotes: LiveData<List<Note>>
    private val listeNotes: LiveData<List<Note>>? = null
    fun insert(note: Note?) {
        InsertNoteThread(notedao,note).start()
    }

    fun update(note: Note?) {
        UpdateNoteThread(notedao,note).start()
    }

    fun delete(note: Note?) {
        DeleteNoteThread(notedao,note).start()
    }

    fun deleteAllNotes() {
        DeleteAllNotesThread(notedao).start()
    }

    fun deleteAllNotesListe(id: Int) {
        DeleteAllNoteListeThread(notedao, id).start()
    }

    fun getListeNotes(listeID: Int): LiveData<List<Note>> {
        return notedao.getListNotes(listeID)
    }

    private class InsertNoteThread(private val notedao: NoteDao, private val notes: Note?) :
        Thread() {
        override fun run() {
            notedao.insert(notes)
        }
    }

    private class UpdateNoteThread(private val notedao: NoteDao, private val notes: Note?) :
        Thread() {
        override fun run() {
            notedao.update(notes)
        }
    }

    private class DeleteNoteThread(private val notedao: NoteDao, private val notes: Note?) :
        Thread() {

        override fun run() {
            notedao.delete(notes)
        }
    }

    private class DeleteAllNotesThread(private val notedao: NoteDao) :
        Thread() {
        override fun run() {
            notedao.deleteAllNotes()
        }
    }

    private class DeleteAllNoteListeThread(private val notedao: NoteDao, private val id: Int) :
        Thread() {
        override fun run() {
            notedao.deleteAllNotesListe(id)
        }
    }

    init {
        val database = getInstance(application!!)
        notedao = database!!.noteDao()
        allNotes = notedao.allNotes
    }
}
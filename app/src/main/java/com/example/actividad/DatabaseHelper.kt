package com.example.actividad

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "miBD.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val createTable = ("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "email TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }



    fun insertarUsuario(usuario: Usuario): Long {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("email", usuario.email)
        }
        val id = db.insert("usuarios", null, valores)
        db.close()
        return id
    }


    fun obtenerUsuarios(): List<Usuario> {
        val listaUsuarios = mutableListOf<Usuario>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                listaUsuarios.add(Usuario(id, nombre, email))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listaUsuarios
    }


    fun actualizarUsuario(usuario: Usuario): Int {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("email", usuario.email)
        }
        val filas = db.update("usuarios", valores, "id = ?", arrayOf(usuario.id.toString()))
        db.close()
        return filas
    }


    fun eliminarUsuario(id: Int): Int {
        val db = writableDatabase
        val filas = db.delete("usuarios", "id = ?", arrayOf(id.toString()))
        db.close()
        return filas
    }
}
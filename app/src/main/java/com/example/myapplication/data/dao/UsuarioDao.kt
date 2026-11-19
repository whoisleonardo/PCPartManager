package com.example.myapplication.data.dao

import android.content.ContentValues
import com.example.myapplication.data.AppDatabase

data class UsuarioPerfil(val id: Long, val nome: String, val email: String)

class UsuarioDao(private val database: AppDatabase) {
    fun perfilAtual(): UsuarioPerfil? {
        val sql = "SELECT u.id, u.nome, u.email FROM usuarios u INNER JOIN sessao s ON s.usuario_id = u.id WHERE s.id = 1"
        return database.readableDatabase.rawQuery(sql, null).use { c ->
            if (c.moveToFirst()) UsuarioPerfil(c.getLong(0), c.getString(1), c.getString(2)) else null
        }
    }

    fun atualizarPerfil(nome: String, email: String): Boolean {
        val usuario = perfilAtual() ?: return false
        if (nome.isBlank() || email.isBlank()) return false
        return try {
            database.writableDatabase.update("usuarios", ContentValues().apply {
                put("nome", nome.trim()); put("email", email.trim())
            }, "id=?", arrayOf(usuario.id.toString())) > 0
        } catch (_: android.database.sqlite.SQLiteConstraintException) { false }
    }

    fun excluirPerfil(): Boolean {
        val usuario = perfilAtual() ?: return false
        val db = database.writableDatabase
        db.beginTransaction()
        return try {
            val excluidos = db.delete("usuarios", "id=?", arrayOf(usuario.id.toString()))
            if (excluidos > 0) { db.delete("sessao", "usuario_id=?", arrayOf(usuario.id.toString())); db.setTransactionSuccessful() }
            excluidos > 0
        } finally { db.endTransaction() }
    }

    fun cadastrar(nome: String, email: String, senha: String): Boolean = try {
        database.writableDatabase.insertOrThrow("usuarios", null, ContentValues().apply {
            put("nome", nome); put("email", email); put("senha", senha)
        }) != -1L
    } catch (_: Exception) { false }

    fun login(email: String, senha: String): Boolean {
        val cursor = database.readableDatabase.query("usuarios", arrayOf("id"), "email=? AND senha=?", arrayOf(email, senha), null, null, null)
        val usuarioId = cursor.use { if (it.moveToFirst()) it.getLong(0) else -1L }
        if (usuarioId == -1L) return false
        database.writableDatabase.delete("sessao", null, null)
        database.writableDatabase.insert("sessao", null, ContentValues().apply { put("id", 1); put("usuario_id", usuarioId) })
        return true
    }

    fun temSessao(): Boolean = database.readableDatabase.query("sessao", null, null, null, null, null, null).use { it.count > 0 }
    fun sair() { database.writableDatabase.delete("sessao", null, null) }
}

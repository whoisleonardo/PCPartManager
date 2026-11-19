package com.example.myapplication.data.dao

import android.content.ContentValues
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.models.Desejo

class DesejoDao(private val database: AppDatabase) {
    fun listar(): List<Desejo> = database.readableDatabase.query("desejos", null, null, null, null, null, "id DESC").use { c ->
        buildList {
            while (c.moveToNext()) add(Desejo(
                nome = c.getString(c.getColumnIndexOrThrow("nome")),
                categoria = c.getString(c.getColumnIndexOrThrow("categoria")),
                destino = c.getString(c.getColumnIndexOrThrow("destino")),
                prioridade = c.getString(c.getColumnIndexOrThrow("prioridade")),
                precoAtual = c.getString(c.getColumnIndexOrThrow("preco_atual")),
                precoAnterior = c.getString(c.getColumnIndexOrThrow("preco_anterior")),
                tendencia = c.getString(c.getColumnIndexOrThrow("tendencia")),
                caiu = c.getInt(c.getColumnIndexOrThrow("caiu")) != 0,
                loja = c.getString(c.getColumnIndexOrThrow("loja")),
                id = c.getLong(c.getColumnIndexOrThrow("id"))
            ))
        }
    }

    fun salvar(desejo: Desejo) {
        val valores = ContentValues().apply {
            put("nome", desejo.nome)
            put("categoria", desejo.categoria)
            put("destino", desejo.destino)
            put("prioridade", desejo.prioridade)
            put("preco_atual", desejo.precoAtual)
            put("preco_anterior", desejo.precoAnterior)
            put("tendencia", desejo.tendencia)
            put("caiu", if (desejo.caiu) 1 else 0)
            put("loja", desejo.loja)
        }
        if (desejo.id == 0L) database.writableDatabase.insert("desejos", null, valores)
        else database.writableDatabase.update("desejos", valores, "id=?", arrayOf(desejo.id.toString()))
    }

    fun deletar(id: Long) { database.writableDatabase.delete("desejos", "id=?", arrayOf(id.toString())) }
}

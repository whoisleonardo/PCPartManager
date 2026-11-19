package com.example.myapplication.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabase(context: Context) : SQLiteOpenHelper(context, "pc_parts.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, email TEXT UNIQUE, senha TEXT)")
        db.execSQL("CREATE TABLE sessao (id INTEGER PRIMARY KEY, usuario_id INTEGER)")
        db.execSQL("CREATE TABLE pecas (id TEXT PRIMARY KEY, nome TEXT, categoria TEXT, marca TEXT, modelo TEXT, status TEXT, localizacao TEXT, preco TEXT, data_compra TEXT, numero_serie TEXT, loja TEXT)")
        db.execSQL("CREATE TABLE computadores (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, status TEXT, compatibilidade TEXT, compativel INTEGER, valor_total TEXT, consumo TEXT, data_montagem TEXT)")
        db.execSQL("CREATE TABLE desejos (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, categoria TEXT NOT NULL, destino TEXT NOT NULL, prioridade TEXT NOT NULL, preco_atual TEXT NOT NULL, preco_anterior TEXT NOT NULL, tendencia TEXT NOT NULL, caiu INTEGER NOT NULL, loja TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) db.execSQL("CREATE TABLE IF NOT EXISTS desejos (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, categoria TEXT NOT NULL, destino TEXT NOT NULL, prioridade TEXT NOT NULL, preco_atual TEXT NOT NULL, preco_anterior TEXT NOT NULL, tendencia TEXT NOT NULL, caiu INTEGER NOT NULL, loja TEXT NOT NULL)")
    }

}

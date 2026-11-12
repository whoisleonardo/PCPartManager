package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.dao.UsuarioDao

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val banco = UsuarioDao(AppDatabase(application))
    var sessaoAtiva by mutableStateOf(banco.temSessao()); private set
    fun login(email: String, senha: String) = banco.login(email, senha).also { sessaoAtiva = it }
    fun cadastrar(nome: String, email: String, senha: String) = banco.cadastrar(nome, email, senha)
    fun sair() { banco.sair(); sessaoAtiva = false }
}


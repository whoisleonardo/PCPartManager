package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.dao.UsuarioDao
import com.example.myapplication.data.dao.UsuarioPerfil

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val banco = UsuarioDao(AppDatabase(application))
    var perfil by mutableStateOf<UsuarioPerfil?>(null); private set
    fun carregarPerfil() { perfil = banco.perfilAtual() }
    fun atualizarPerfil(nome: String, email: String): Boolean = banco.atualizarPerfil(nome, email).also { if (it) carregarPerfil() }
    fun excluirPerfil(): Boolean = banco.excluirPerfil().also { if (it) { perfil = null; sessaoAtiva = false } }
    var sessaoAtiva by mutableStateOf(banco.temSessao()); private set
    fun login(email: String, senha: String) = banco.login(email, senha).also { sessaoAtiva = it }
    fun cadastrar(nome: String, email: String, senha: String) = banco.cadastrar(nome, email, senha)
    fun sair() { banco.sair(); perfil = null; sessaoAtiva = false }
}


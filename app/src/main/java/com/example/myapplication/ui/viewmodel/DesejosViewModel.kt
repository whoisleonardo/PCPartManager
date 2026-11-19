package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.dao.DesejoDao
import com.example.myapplication.models.Desejo

class DesejosViewModel(application: Application) : AndroidViewModel(application) {
    private val banco = DesejoDao(AppDatabase(application))
    var desejos by mutableStateOf(emptyList<Desejo>()); private set
    fun listar() { desejos = banco.listar() }
    fun salvar(desejo: Desejo) { banco.salvar(desejo); listar() }
    fun deletar(id: Long) { banco.deletar(id); listar() }
}

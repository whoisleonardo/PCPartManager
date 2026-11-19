package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.CotacaoClient
import kotlinx.coroutines.launch
import java.util.Locale

class CotacaoViewModel : ViewModel() {
    var carregando by mutableStateOf(false); private set
    var cotacao by mutableStateOf<String?>(null); private set
    var erro by mutableStateOf<String?>(null); private set
    private var consultado = false

    fun consultar() {
        if (carregando) return
        consultado = true
        carregando = true
        erro = null
        viewModelScope.launch {
            try {
                val resposta = CotacaoClient.api.cotacao()
                val taxa = resposta.rates["BRL"] ?: error("Moeda BRL indisponível")
                cotacao = "US$ 1 = R$ ${String.format(Locale("pt", "BR"), "%.2f", taxa)} · ${resposta.date}"
            } catch (_: Exception) {
                erro = "Não foi possível consultar a cotação. Verifique a conexão."
            } finally {
                carregando = false
            }
        }
    }

    fun consultarInicial() {
        if (!consultado) consultar()
    }
}

package com.example.myapplication.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.dao.UsuarioPerfil

@Composable
fun TelaPerfil(perfil: UsuarioPerfil?, onAtualizar: (String, String) -> Boolean, onExcluir: () -> Unit) {
    var editando by remember { mutableStateOf(false) }
    var confirmandoExclusao by remember { mutableStateOf(false) }
    var nome by remember(perfil) { mutableStateOf(perfil?.nome ?: "") }
    var email by remember(perfil) { mutableStateOf(perfil?.email ?: "") }
    var erro by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Meu perfil", style = MaterialTheme.typography.headlineSmall)
        if (perfil == null) Text("Carregando perfil...")
        else {
            Text("Nome: ${perfil.nome}")
            Text("E-mail: ${perfil.email}")
            Button(onClick = { editando = true }) { Text("Editar perfil") }
            OutlinedButton(onClick = { confirmandoExclusao = true }) { Text("Excluir minha conta") }
        }
    }
    if (editando) AlertDialog(
        onDismissRequest = { editando = false },
        title = { Text("Editar perfil") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(nome, { nome = it }, label = { Text("Nome") }, singleLine = true)
                OutlinedTextField(email, { email = it }, label = { Text("E-mail") }, singleLine = true)
                if (erro) Text("Confira os campos. O e-mail pode já estar cadastrado.", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = { TextButton(onClick = {
            erro = !onAtualizar(nome, email)
            if (!erro) editando = false
        }, enabled = nome.isNotBlank() && email.isNotBlank()) { Text("Salvar") } },
        dismissButton = { TextButton(onClick = { editando = false }) { Text("Cancelar") } }
    )
    if (confirmandoExclusao) AlertDialog(
        onDismissRequest = { confirmandoExclusao = false },
        title = { Text("Excluir conta?") },
        text = { Text("Esta ação é permanente e removerá seu cadastro e sua sessão.") },
        confirmButton = { TextButton(onClick = { confirmandoExclusao = false; onExcluir() }) { Text("Excluir definitivamente") } },
        dismissButton = { TextButton(onClick = { confirmandoExclusao = false }) { Text("Cancelar") } }
    )
}

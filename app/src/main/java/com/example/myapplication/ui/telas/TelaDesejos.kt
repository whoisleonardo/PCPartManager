package com.example.myapplication.ui.telas

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.models.Desejo
import com.example.myapplication.ui.viewmodel.CotacaoViewModel

@Composable
fun TelaDesejos(desejos: List<Desejo>, onSalvar: (Desejo) -> Unit, onDeletar: (Long) -> Unit, modifier: Modifier = Modifier) {
    var novo by remember { mutableStateOf(false) }
    var editando by remember { mutableStateOf<Desejo?>(null) }
    var filtro by remember { mutableStateOf("Todas") }
    val cotacaoViewModel: CotacaoViewModel = viewModel()
    LaunchedEffect(Unit) { cotacaoViewModel.consultarInicial() }

    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Lista de desejos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Button(onClick = { novo = true }) { Text("Novo desejo") }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Cotação USD/BRL · API Frankfurter", fontWeight = FontWeight.Bold)
                if (cotacaoViewModel.carregando) LinearProgressIndicator(Modifier.fillMaxWidth())
                cotacaoViewModel.cotacao?.let { Text(it) }
                cotacaoViewModel.erro?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                TextButton(enabled = !cotacaoViewModel.carregando, onClick = { cotacaoViewModel.consultar() }) {
                    Text("Atualizar cotação")
                }
            }
        }
        Text("${desejos.size} itens cadastrados")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Todas", "Alta", "Média", "Baixa").forEach { prioridade ->
                FilterChip(selected = filtro == prioridade, onClick = { filtro = prioridade }, label = { Text(prioridade) })
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(desejos.filter { filtro == "Todas" || it.prioridade == filtro }, key = { it.id }) { desejo ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (desejo.fotoUri.isNotBlank()) {
                            AsyncImage(
                                model = desejo.fotoUri,
                                contentDescription = "Foto de ${desejo.nome}",
                                modifier = Modifier.size(72.dp)
                            )
                        }
                        Column {
                            Text(desejo.nome, fontWeight = FontWeight.Bold)
                            Text("${desejo.categoria} · ${desejo.prioridade} · ${desejo.destino}")
                            Text("${desejo.precoAtual} · ${desejo.loja}")
                            Row {
                                TextButton(onClick = { editando = desejo }) { Text("Alterar") }
                                TextButton(onClick = { onDeletar(desejo.id) }) { Text("Deletar") }
                            }
                        }
                    }
                }
            }
        }
    }
    if (novo) DesejoDialog(null, onDismiss = { novo = false }, onSalvar = { onSalvar(it); novo = false })
    editando?.let { desejo -> DesejoDialog(desejo, onDismiss = { editando = null }, onSalvar = { onSalvar(it); editando = null }) }
}

@Composable
private fun DesejoDialog(desejo: Desejo?, onDismiss: () -> Unit, onSalvar: (Desejo) -> Unit) {
    var nome by remember(desejo) { mutableStateOf(desejo?.nome ?: "") }
    var categoria by remember(desejo) { mutableStateOf(desejo?.categoria ?: "") }
    var destino by remember(desejo) { mutableStateOf(desejo?.destino ?: "") }
    var prioridade by remember(desejo) { mutableStateOf(desejo?.prioridade ?: "Média") }
    var preco by remember(desejo) { mutableStateOf(desejo?.precoAtual ?: "") }
    var precoAnterior by remember(desejo) { mutableStateOf(desejo?.precoAnterior ?: "") }
    var loja by remember(desejo) { mutableStateOf(desejo?.loja ?: "") }
    var fotoUri by remember(desejo) { mutableStateOf(desejo?.fotoUri ?: "") }
    val context = LocalContext.current
    val selecionarFoto = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                fotoUri = it.toString()
            } catch (_: SecurityException) {
                // Não salva URI sem permissão persistente.
            }
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (desejo == null) "Novo desejo" else "Editar desejo") },
        text = {
            Column(Modifier.heightIn(max = 440.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(nome, { nome = it }, label = { Text("Nome") }, singleLine = true)
                OutlinedTextField(categoria, { categoria = it }, label = { Text("Categoria") }, singleLine = true)
                OutlinedTextField(destino, { destino = it }, label = { Text("Computador de destino") }, singleLine = true)
                Text("Prioridade")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("Alta", "Média", "Baixa").forEach { p ->
                        FilterChip(selected = prioridade == p, onClick = { prioridade = p }, label = { Text(p) })
                    }
                }
                OutlinedTextField(preco, { preco = it }, label = { Text("Preço atual") }, singleLine = true)
                OutlinedTextField(precoAnterior, { precoAnterior = it }, label = { Text("Preço anterior") }, singleLine = true)
                OutlinedTextField(loja, { loja = it }, label = { Text("Loja") }, singleLine = true)
                OutlinedButton(onClick = { selecionarFoto.launch(arrayOf("image/*")) }) {
                    Text(if (fotoUri.isBlank()) "Selecionar foto da galeria" else "Trocar foto")
                }
                if (fotoUri.isNotBlank()) {
                    AsyncImage(model = fotoUri, contentDescription = "Foto selecionada", modifier = Modifier.size(96.dp))
                    TextButton(onClick = { fotoUri = "" }) { Text("Remover foto") }
                }
            }
        },
        confirmButton = {
            TextButton(enabled = nome.isNotBlank(), onClick = {
                onSalvar(Desejo(
                    nome = nome.trim(), categoria = categoria.trim(), destino = destino.trim(),
                    prioridade = prioridade, precoAtual = preco.trim(), precoAnterior = precoAnterior.trim(),
                    tendencia = desejo?.tendencia ?: "", caiu = desejo?.caiu ?: false,
                    loja = loja.trim(), id = desejo?.id ?: 0L, fotoUri = fotoUri
                ))
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

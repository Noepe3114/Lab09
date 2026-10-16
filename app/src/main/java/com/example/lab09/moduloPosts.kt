package com.example.lab09
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
@Composable
fun ScreenPosts(navController: NavHostController, servicio: PostApiService) {
    val listaPosts: SnapshotStateList<PostModel> = remember { mutableStateListOf() }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        try {
            val listado = servicio.getUserPosts()
            listado.forEach { listaPosts.add(it) }
        } catch (e: Exception) {
            Log.e("POSTS", "Error al obtener posts: ${e.message}")
            error = "No se pudieron cargar los posts. Revisa tu conexión."
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        error?.let { Text(text = it, modifier = Modifier.padding(16.dp)) }
        LazyColumn {
            items(listaPosts) { item ->
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = item.id.toString(),
                        modifier = Modifier.weight(0.05f),
                        textAlign = TextAlign.End
                    )
                    Spacer(Modifier.padding(horizontal = 1.dp))
                    Text(text = item.title, modifier = Modifier.weight(0.7f))
                    IconButton(
                        onClick = {
                            navController.navigate("postsVer/${item.id}")
                            Log.e("POSTS", "ID = ${item.id}")
                        },
                        modifier = Modifier.weight(0.1f)
                    ) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "Ver")
                    }
                }
            }
        }
    }
}
@Composable
fun ScreenPost(navController: NavHostController, servicio: PostApiService, id: Int) {
    var post by remember { mutableStateOf<PostModel?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        try {
            post = servicio.getUserPostById(id)
        } catch (e: Exception) {
            Log.e("POSTS", "Error al obtener el post $id: ${e.message}")
            error = "No se pudo cargar el post."
        }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        error?.let { Text(text = it) }

        post?.let { p ->
            CampoSoloLectura("id", p.id.toString())
            CampoSoloLectura("userId", p.userId.toString())
            CampoSoloLectura("title", p.title)
            CampoSoloLectura("body", p.body)
        }
    }
}

@Composable
private fun CampoSoloLectura(etiqueta: String, valor: String) {
    OutlinedTextField(
        value = valor,
        onValueChange = {},
        label = { Text(etiqueta) },
        readOnly = true
    )
}
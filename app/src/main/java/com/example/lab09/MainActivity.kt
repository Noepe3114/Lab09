package com.example.lab09
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab09.ui.theme.Lab09Theme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab09Theme {
                ProgPrincipal9()
            }
        }
    }
}
@Composable
fun ProgPrincipal9() {
    val urlBase = "https://json-placeholder.mock.beeceptor.com/"
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val servicio = remember { retrofit.create(PostApiService::class.java) }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") { ScreenInicio() }
        composable("posts") { ScreenPosts(navController, servicio) }
        composable(
            "postsVer/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            ScreenPost(navController, servicio, it.arguments!!.getInt("id"))
        }
    }
}
@Composable
fun ScreenInicio() {
    Text("INICIO")
}
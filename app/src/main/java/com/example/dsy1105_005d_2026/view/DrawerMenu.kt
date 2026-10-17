package com.example.dsy1105_005d_2026.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

//salida x pantalla
@Composable

fun DrawerMenu(
    username:String,
    navController: NavController
)
{// inicio Drawer
//Usar columna completa
Column(modifier=Modifier.fillMaxSize())
{//inicio columna
    Box(
    modifier= Modifier
        .fillMaxWidth() //ancho de pantalla
        .height(120.dp)
        .background(MaterialTheme.colorScheme.primary)
    )//fin Box
    {//contenido caja
        Text(
           text="Categorias  usuario: $username",
            style= MaterialTheme.typography.headlineSmall,
            color=MaterialTheme.colorScheme.onPrimary,
            modifier= Modifier
                .align(Alignment.BottomStart)  //alinear texto
        )
    }//fin caja

//LazyColum permite dezplazar verticalmente


    LazyColumn(modifier= Modifier.weight(1f))
    {//inicio Lazy

        item{ //item 1
        NavigationDrawerItem(  //permite seleccionar dentro del menu

            label={Text("Hamburguesa Picante")},
            selected = false,
            onClick = {/* accion */},
            icon={Icon(Icons.Default.LocalFireDepartment,
                contentDescription = "Picante"
            )
        } //fin icons
        )} // fin //item 1

        item{ //item 2
            NavigationDrawerItem(  //permite seleccionar dentro del menu

                label={Text("Hamburguesa Italiana")},
                selected = false,
                onClick = {/* accion */},
                icon={Icon(Icons.Default.Grass,
                    contentDescription = "Italiana"
                )
                } //fin icons
            )} // fin //item 2

        item{ //item 2
            NavigationDrawerItem(  //permite seleccionar dentro del menu

                label={Text("Hamburguesa Clasica")},
                selected = false,
                onClick = {
                val nombre= Uri.encode("Hamburguesa Clasica")
                val precio="5000"
                navController.navigate("ProductoFormScreen/$nombre/$precio")

                },
                icon={Icon(Icons.Default.Fastfood,
                    contentDescription = "Clasica"
                )
                } //fin icons
            )} // fin //item 2





     } //fin Lazy




}//fin inicio columna


}// Fin inicio Drawer


//para desplegar el diseño que despues conectaremos al AppNav

@Preview(showBackground = true)
@Composable
fun DrawerMenuPreview(){
    //simulamos los parametros
    val navController = rememberNavController()
    DrawerMenu(username="Usuario Prueba", navController=navController)
}






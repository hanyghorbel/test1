package com.example.quotegenerator.ui


import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quotegenerator.ui.theme.QuoteGeneratorTheme
import com.example.quotegenerator.viewmodel.QuoteViewModel
import com.example.quotegenerator.model.Quote
import com.example.quotegenerator.viewmodel.QuoteUIState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuoteGeneratorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    ScaffoldApp()
                }
            }
        }
    }
}

@Composable
fun QuoteScreen(quoteViewModel: QuoteViewModel= viewModel(),navController: NavController) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Game Of Thrones\nQuote Generator",
            color = MaterialTheme.colors.primary,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            label = { Text(text = "number of quotes to generate")},
            value = quoteViewModel.n,
            onValueChange = {quoteViewModel.changeN(it.replace(',','.'))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                quoteViewModel.convert()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text= "Generate")
        }


        Screen(uiState = quoteViewModel.quoteUIState)
    }
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(8.dp)
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate("info")
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            shape = RoundedCornerShape(25.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = "Add FAB",
                tint = Color.White,
            )
        }
    }


}
@Composable
fun ScreenTopBar(title: String, navController: NavController){
    TopAppBar(
        title = { Text(title)},
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp()}) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)

            }
        }
    )
}
@Composable
fun InfoScreen(navController: NavController){
    Scaffold(
        topBar = { ScreenTopBar("Info", navController)},
        content = {padding -> Column(modifier=Modifier.padding(padding)

        ) {
            Text(text = "API application that access the date under the link 'https://api.gameofthronesquotes.xyz/v1/random/' and generates n random quotes from" +
                    " the TV Series Game Of Thrones. The app handles errors and displays different statuses on UI")


        }}
    )
}
@Composable
fun ScaffoldApp(){
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = "Home"){
        composable(route="Home"){
            QuoteScreen(navController= navController)
        }
        composable(route="Info"){
            InfoScreen(navController = navController)
        }
    }
}
@Composable
fun QuoteList(quotes: List<Quote>){

    LazyColumn(
        modifier= Modifier.padding(8.dp)
    ){
        items(quotes){
                quote->
            Text(
                text= quote.sentence + "\n                       ~ " + quote.character.name + " ~",
                modifier = Modifier.padding(top=4.dp,bottom=4.dp)
            )
            Spacer(modifier = Modifier
                .height(7.dp)
            )
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(7.dp))
        }
    }
}
@Composable
fun LoadingScreen(){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(){
    Text(text = "Error retrieving data from API.")
}
@Composable
fun EmptyScreen(){
    Text( color= Color.Gray,
        text = "Nothing over here for now. Tap a number ;)")
}

@Composable
fun Screen(uiState: QuoteUIState){
    when(uiState){
        is QuoteUIState.Loading -> LoadingScreen()
        is QuoteUIState.Success -> QuoteList(uiState.quotes)
        is QuoteUIState.Success1 -> QuoteList(listOf( uiState.quote))
        is QuoteUIState.Error -> ErrorScreen()
        is QuoteUIState.Empty -> EmptyScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuoteGeneratorTheme {

        ScaffoldApp()
    }
}
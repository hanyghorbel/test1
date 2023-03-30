package com.example.quotegenerator.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.example.quotegenerator.model.Quote
import com.example.quotegenerator.model.QuotesApi
import kotlinx.coroutines.launch

sealed interface QuoteUIState {
    data class Success(val quotes: List<Quote>): QuoteUIState
    data class Success1(val quote: Quote): QuoteUIState
    object Error: QuoteUIState
    object Loading: QuoteUIState
    object Empty: QuoteUIState
}

class QuoteViewModel: ViewModel(){
    var quoteUIState: QuoteUIState by mutableStateOf<QuoteUIState>(QuoteUIState.Loading)

    var n by  mutableStateOf("")
        private set
    fun changeN(newValue: String){
        n= newValue
    }
    fun convert(){
        getQuotesList()
    }


    init {
        getQuotesList()
    }

    private fun getQuotesList() {
        viewModelScope.launch {
            var quotesApi: QuotesApi? = null
            try {
                quotesApi = QuotesApi!!.getInstance()
                val num = n.toIntOrNull() ?: -1
                if( num == 1){
                    quoteUIState = QuoteUIState.Success1(quotesApi.getQuote("https://api.gameofthronesquotes.xyz/v1/random/"))
                }
                else if(num==0 || n==""){
                    quoteUIState= QuoteUIState.Empty
                }
                else if( num!=0 ){
                    quoteUIState = QuoteUIState.Success(quotesApi.getQuotes("https://api.gameofthronesquotes.xyz/v1/random/"+ n))
                }
                else{
                    quoteUIState= QuoteUIState.Error
                }

            } catch (e: Exception) {
                Log.d("QUOTESVIEWMODEL", e.message.toString())
                quoteUIState= QuoteUIState.Error
            }
        }
    }

}
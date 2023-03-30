package com.example.quotegenerator.model
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


data class Quote(
    var sentence: String,
    var character: Character,
)
data class Character(
    var name: String,
    var slug: String,
    var house: House
)
data class House(
    var name: String,
    var slug: String
)

const val BASE_URL = "https://api.gameofthronesquotes.xyz/v1/random/"
interface QuotesApi {
    @GET
    suspend fun getQuotes(@Url url:String): List<Quote>
    @GET
    suspend fun getQuote(@Url url:String): Quote
    companion object {
        var quotesService: QuotesApi?=null
        fun getInstance(): QuotesApi{
            if (quotesService ===null){
                quotesService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(QuotesApi::class.java)
            }
            return quotesService!!
        }
    }

}

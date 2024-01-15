package pt.ulusofona.deisi.cm2223.g21805799.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g21805799.data.local.entities.Cinema
import java.io.IOException
import pt.ulusofona.deisi.cm2223.g21805799.model.DataManager
import pt.ulusofona.deisi.cm2223.g21805799.model.Movie
import java.io.InputStream
import java.net.URL

class OMDBOkHttp (
  private val baseUrl: String,
  private val client: OkHttpClient,
  var bmp: Bitmap? = null

) : DataManager() {

  override fun getAllMovies(onFinished: (List<Movie>) -> Unit) {
    TODO("Not yet implemented")
  }

  override fun getMovieName(query: String, onFinished: (Result<String>) -> Unit) {

    lateinit var movie: String

    // Aqui estamos a preparar o pedido. Precisamos da apiKey e do url
    val request: Request = Request.Builder()
      .url("$baseUrl/$query")
      .addHeader("Authorization", "Bearer")
      .build()

    // Nesta linha executamos o pedido ao servidor
    // em caso caso de erro, o método onFailure será invocado (ex: timeout)
    // se tudo correr bem, teremos a resposta ao pedido no método onResponse
    client.newCall(request).enqueue(object : Callback {

      override fun onFailure(call: Call, e: IOException) {
        onFinished(Result.failure(e))
      }

      // Processar a resposta ao pedido
      override fun onResponse(call: Call, response: Response) {
        // Se a resposta devolver um erro, ex: 403 acesso negado ao web service
        if (!response.isSuccessful) {
          onFinished(Result.failure(IOException("Unexpected code $response")))
        }
        else {
          val body = response.body?.string()
          if (body != null) {
            // Estamos a guardar o objeto assinalado a amarelo no exemplo aqui
            val jsonObject = JSONObject(body)
            if (jsonObject.optString("Error").isNullOrEmpty()) {
              movie = jsonObject.getString("Title")
              // Devolve o filme em formato Movie
              onFinished(Result.success(movie))
            } else {
              onFinished(Result.failure(Exception("Movie not found!")))
            }
          }
        }
      }
    })
  }



  override fun getMovie(query: String, cinema: String, latitude: Double, longitude: Double, evaluation: Int, date: Long, observations: String, watched: Boolean, onFinished: (Result<Movie>) -> Unit) {

    lateinit var movie: Movie

    // Aqui estamos a preparar o pedido. Precisamos da apiKey e do url
    val request: Request = Request.Builder()
      .url("$baseUrl/$query")
      .addHeader("Authorization", "Bearer")
      .build()

    // Nesta linha executamos o pedido ao servidor
    // em caso caso de erro, o método onFailure será invocado (ex: timeout)
    // se tudo correr bem, teremos a resposta ao pedido no método onResponse
    client.newCall(request).enqueue(object : Callback {

      override fun onFailure(call: Call, e: IOException) {
        onFinished(Result.failure(e))
      }

      // Processar a resposta ao pedido
      override fun onResponse(call: Call, response: Response) {
        // Se a resposta devolver um erro, ex: 403 acesso negado ao web service
        if (!response.isSuccessful) {
          onFinished(Result.failure(IOException("Unexpected code $response")))
        }
        else {
          val body = response.body?.string()
          if (body != null) {

            // Estamos a guardar o objeto assinalado a amarelo no exemplo aqui
            val jsonObject = JSONObject(body)
            if (jsonObject.optString("Error").isNullOrEmpty()) {
              CoroutineScope(Dispatchers.IO).launch {
                val `in`: InputStream = URL(jsonObject.getString("Poster")).openStream()
                bmp = BitmapFactory.decodeStream(`in`)
                withContext (Dispatchers.Main) {
                  //update the ImageView
                  //binding.imageView.setImageBitmap(bmp)
                  movie = Movie(
                    jsonObject.getString("imdbID"),
                    jsonObject.getString("Title"),
                    cinema,
                    latitude,
                    longitude,
                    evaluation,
                    date,
                    observations,
                    true,
                    jsonObject.getString("Genre"),
                    jsonObject.getString("Poster"),
                    jsonObject.getString("Plot"),
                    jsonObject.getString("Released"),
                    jsonObject.getString("imdbRating").toDouble(),
                    "https://www.imdb.com/title/${jsonObject.getString("imdbID")}"
                  )

                  // Devolve o filme em formato Movie
                  onFinished(Result.success(movie))
                }
              }

            } else {
              onFinished(Result.failure(Exception("Movie not found!")))
            }
          }
        }
      }
    })
  }

  override fun insertAllMovies(movies: List<Movie>, onFinished: () -> Unit) {
    TODO("Not yet implemented")
  }

  override fun clearAllMovies(onFinished: () -> Unit) {
    TODO("Not yet implemented")
  }

  override fun insertMovie(movie: Movie, onFinished: () -> Unit) {
    TODO("Not yet implemented")
  }

  override fun getCinemas(onFinished: (List<Cinema>) -> Unit) {
    TODO("Not yet implemented")
  }

}

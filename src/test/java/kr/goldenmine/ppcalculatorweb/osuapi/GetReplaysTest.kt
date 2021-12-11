package kr.goldenmine.ppcalculatorweb.osuapi

import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuScore
import kr.goldenmine.ppcalculatorweb.osuapi.api.RetrofitOsuApiClient
import kr.goldenmine.ppcalculatorweb.osuapi.api.RetrofitOsuApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*

fun main() {
    val apiKey = File("APIKey.txt").readText()

    val service = RetrofitOsuApiClient.instance.create(RetrofitOsuApiService::class.java)

    service.requestScoreRanking(apiKey, 1805627, limit=100).enqueue(object : Callback<Array<OsuScore>> {
        override fun onFailure(call: Call<Array<OsuScore>>, t: Throwable) {
            t.printStackTrace()
        }

        //https://osu.ppy.sh/scores/osu/3106051627/download

        override fun onResponse(call: Call<Array<OsuScore>>, response: Response<Array<OsuScore>>) {
            if(response.isSuccessful) {
                println("succeed getting scores...")
                response.body()?.iterator()?.forEach {
                    println(it.scoreId)
                }
            } else {
                println("failed to get")
            }
        }
    })
}

fun decodeBase64(encoded: String): ByteArray {
    val decoder = Base64.getDecoder()

    return decoder.decode(encoded)
}

fun writeFile(inputStream: InputStream, fileName: String) {
    val folder = File("replays")
    folder.mkdirs()

    val file = File("replays/$fileName")
    if(!file.exists()) file.createNewFile()

    val outputStream = BufferedOutputStream(FileOutputStream(file))

    inputStream.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

//    val buffer = ByteArray(4096)
//
//    val outputStream = BufferedOutputStream(FileOutputStream(file))
//    var length: Int
//    while (true) {
//        length = inputStream.read(buffer)
//        if(length <= 0) break
//
//        outputStream.write(buffer, 0, length)
//    }
}
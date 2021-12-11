package kr.goldenmine.ppcalculatorweb.osuapi

import kr.goldenmine.ppcalculatorweb.osuapi.api.RetrofitOsuApiClient
import kr.goldenmine.ppcalculatorweb.osuapi.api.RetrofitOsuApiService
import kr.goldenmine.ppcalculatorweb.osuapi.api.selenium.downloadReplays
import kr.goldenmine.ppcalculatorweb.osuapi.api.selenium.mapCrawling
import java.io.File

fun main() {
    val service = RetrofitOsuApiClient.instance.create(RetrofitOsuApiService::class.java)

    val argument = "m=0"
    val id = File("osuAccount.txt").readLines()[0]
    val pw = File("osuAccount.txt").readLines()[1]
    val apiKey = File("APIKey.txt").readText()

    val route = File("D:/Develop/osu/replays")

    // finding map ids up to 1000.
    val beatmapSetIds = mapCrawling("https://osu.ppy.sh/beatmapsets?$argument", id, pw, true, 1000).toList()

    Thread.sleep(10000L)

    val iterator = beatmapSetIds.listIterator()

    while(iterator.hasNext()) {
        val mapsetId = iterator.next()

        try {
            val requestBeatmap = service.requestBeatmaps(apiKey, mapsetId, limit = 10).execute()
            val beatmaps = requestBeatmap.body()
            val beatmap = beatmaps?.filter { it.beatmapsetId.toInt() == mapsetId }?.maxBy { it.starRating }

            println("beatmap request state: ${beatmap != null}, mapsetid: $mapsetId")

            if (!requestBeatmap.isSuccessful) {
                println(requestBeatmap.errorBody()?.string())
            }

            Thread.sleep(500L)

            if (beatmap != null) {
                val beatmapId = beatmap.beatmapId.toInt()
                val scores = service.requestScoreRanking(apiKey, beatmapId, limit = 100).execute().body()?.toMutableList()

                Thread.sleep(500L)

                println("score request state: ${scores != null}, id: ${beatmap.beatmapId}, setid: ${beatmap.beatmapsetId}")
                if (scores != null) {
                    val file = File(route.path + "/$beatmapId")
                    val alreadyDownloaded = getAlreadyDownloadedReplay(file).toSet()

                    scores.removeIf { alreadyDownloaded.contains(it.scoreId) }

                    println("downloading replays... (will start if not downloaded replay count is at least 5) ${scores.size} ${scores.map { it.scoreId }.joinToString { it.toString() }}")
                    if (scores.size >= 5) {
                        downloadReplays(id, pw, file, scores.map { it.scoreId }, false)
                        println("downloaded replay $beatmapId")
                    } else {
                        println("already downloaded $beatmapId")
                    }
                } else {
                    println("api requested a lot maybe. sleeping 30s and trying again.")
                    Thread.sleep(30000L)
                    if(iterator.hasPrevious())
                        iterator.previous()
                }
            } else {
                println("api requested a lot maybe. sleeping 30s and trying again.")
                Thread.sleep(30000L)
                if(iterator.hasPrevious())
                    iterator.previous()
            }
        } catch(ex: Exception) {
            ex.printStackTrace()
            println("an error occured. but tries running.")
        }
    }
}

fun getAlreadyDownloadedReplay(folder: File): List<Long> {
    return folder.listFiles()
            ?.filter { !it.endsWith("crdownload")}
            ?.map {
                val first = it.name.lastIndexOf('_')
                val second = it.name.lastIndexOf('.')

                it.name.substring(first + 1, second).toLong()
            }?.toList() ?: emptyList()
}
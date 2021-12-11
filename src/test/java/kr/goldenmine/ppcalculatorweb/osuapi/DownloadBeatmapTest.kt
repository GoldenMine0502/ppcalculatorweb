package kr.goldenmine.ppcalculatorweb.osuapi

import kr.goldenmine.ppcalculatorweb.osuapi.api.RetrofitOsuApiService
import kr.goldenmine.ppcalculatorweb.osuapi.normal.RetrofitNormalApiClient
import kr.goldenmine.ppcalculatorweb.osuapi.normal.RetrofitNormalApiService
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

fun main() {
    val folder = File("D:\\Develop\\osu\\replays")

    val service = RetrofitNormalApiClient.instance.create(RetrofitNormalApiService::class.java)

    val files = folder.listFiles()?.toMutableList() ?: ArrayList()

//    files.removeIf { original ->
//        files.any { current ->
////            println(original.name)
//            current.isFile && original.startsWith(current.name.substring(0, current.name.indexOf('.')))
//        }
//    }

    val directories = files.filter { it.isDirectory }.toMutableList()

    directories.removeIf { original ->
        files.any { current ->
            current.isFile && original.name == current.name.substring(0, current.name.indexOf('.'))
        }
    }

    println("${directories.size} files will be downloaded: ${directories.joinToString { it.name }}")

    directories.forEach {
        val fileName = it.name
        val beatmapId = fileName.toInt()
//        val beatmapId = if(fileName.indexOf('.') >= 0) fileName.substring(0, fileName.indexOf('.')).toInt() else -1

        if(beatmapId != -1) {
            val inputStream = service.requestDownloadBeatmap(beatmapId).execute().body()?.byteStream()

            if (inputStream != null) {
                val file = File(folder.path, "$fileName.osu")
                if (!file.exists()) file.createNewFile()

                val outputStream = BufferedOutputStream(FileOutputStream(file))

                inputStream.use {
                    outputStream.use {
                        inputStream.copyTo(outputStream)
                    }
                }
                println("downloaded ${file.path}")

                Thread.sleep(5000L)
            }
        } else {
            println("beatmap id is -1 $fileName")
        }
    }
}
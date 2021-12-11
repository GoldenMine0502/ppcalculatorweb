package kr.goldenmine.ppcalculatorweb.osuapi.api

import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuBeatmap
import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuReplayResponse
import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuScore
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitOsuApiService {
    @GET("/api/get_user_best")
    fun requestUserBest(
            @Query("k") apiKey: String,
            @Query("u") userId: Int,
            @Query("m") mode: Int,
            @Query("limit") resultAmount: Int = 100
    ) : Call<Array<OsuScore>>

    @GET("/api/get_scores")
    fun requestScoreRanking(
            @Query("k") apiKey: String,
            @Query("b") beatmapId: Int,
            @Query("m") mode: Int = 0,
            @Query("limit") limit: Int = 100
    ) : Call<Array<OsuScore>>

    @GET("/api/get_beatmaps")
    fun requestBeatmaps(
            @Query("k") apiKey: String,
            @Query("s") beatmapSetId: Int,
            @Query("limit") limit: Int = 1
    ) : Call<Array<OsuBeatmap>>

    @GET("/api/get_replay")
    fun requestOsuReplayFile(
            @Query("k") apiKey: String,
            @Query("s") scoreId: Long
    ) : Call<OsuReplayResponse>
//    @GET("/api/get_beatmaps")
//    fun requestBeatmap(
//            @Query("k") apiKey: String,
////            @Query("since") date: Date,
//            @Query("s") beatmapsetId: Int
//    )
}
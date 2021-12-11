package kr.goldenmine.ppcalculatorweb.osuapi.normal

import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuBeatmap
import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuReplayResponse
import kr.goldenmine.ppcalculatorweb.osuapi.api.OsuScore
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitNormalApiService {
    @GET("/osu/{path}")
    fun requestDownloadBeatmap(
            @Path("path") beatmapId: Int
    ) : Call<ResponseBody>
}
package com.kbcoding.androiddevelopmentbasics.app.data.boxes

import com.kbcoding.androiddevelopmentbasics.app.data.boxes.entities.GetBoxResponseEntity
import com.kbcoding.androiddevelopmentbasics.app.data.boxes.entities.UpdateBoxRequestEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BoxesApi {

    @GET("boxes")
    suspend fun getBoxes(@Query("active") isActive: Boolean?): List<GetBoxResponseEntity>

    @PUT("boxes/{boxId}")
    suspend fun setIsActive(
        @Path("boxId") boxId: Long,
        @Body body: UpdateBoxRequestEntity
    )

}
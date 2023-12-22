package com.kbcoding.androiddevelopmentbasics.sources.boxes

import com.google.gson.reflect.TypeToken
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.BoxesSource
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.sources.base.BaseOkHttpSource
import com.kbcoding.androiddevelopmentbasics.sources.base.OkHttpConfig
import com.kbcoding.androiddevelopmentbasics.sources.boxes.entities.GetBoxResponseEntity
import com.kbcoding.androiddevelopmentbasics.sources.boxes.entities.UpdateBoxRequestEntity
import kotlinx.coroutines.delay
import okhttp3.Request

class OkHttpBoxesSource(
    config: OkHttpConfig
) : BaseOkHttpSource(config), BoxesSource {

    override suspend fun getBoxes(boxesFilter: BoxesFilter): List<BoxAndSettings> {
        delay(500)
        val args = if (boxesFilter == BoxesFilter.ONLY_ACTIVE) "?active=true" else ""
        val request = Request.Builder()
            .get()
            .endpoint("/boxes$args")
            .build()
        val call = client.newCall(request)
        val typeToken = object : TypeToken<List<GetBoxResponseEntity>>() {}
        val response = call.suspendEnqueue().parseJsonResponse(typeToken)
        return response.map { it.toBoxAndSettings() }
    }

    override suspend fun setIsActive(boxId: Long, isActive: Boolean) {
        val updateBoxRequestEntity = UpdateBoxRequestEntity(isActive)
        val request = Request.Builder()
            .put(updateBoxRequestEntity.toJsonRequestBody())
            .endpoint("/boxes/${boxId}")
            .build()
        val call = client.newCall(request)
        call.suspendEnqueue()
    }

}
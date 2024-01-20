package com.kbcoding.androiddevelopmentbasics.app.data.boxes

import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.BoxesSource
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.data.base.BaseRetrofitSource
import com.kbcoding.androiddevelopmentbasics.app.data.base.RetrofitConfig
import com.kbcoding.androiddevelopmentbasics.app.data.boxes.entities.UpdateBoxRequestEntity
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitBoxesSource @Inject constructor(
    config: RetrofitConfig
) : BaseRetrofitSource(config), BoxesSource {

    private val boxesApi = retrofit.create(BoxesApi::class.java)

    override suspend fun getBoxes(
        boxesFilter: BoxesFilter
    ): List<BoxAndSettings> = wrapRetrofitExceptions {
        delay(500)
        val isActive: Boolean? = if (boxesFilter == BoxesFilter.ONLY_ACTIVE)
            true
        else
            null

        boxesApi.getBoxes(isActive).map { it.toBoxAndSettings() }
    }

    override suspend fun setIsActive(
        boxId: Long,
        isActive: Boolean
    ) = wrapRetrofitExceptions {
        val updateBoxRequestEntity = UpdateBoxRequestEntity(isActive)
        boxesApi.setIsActive(boxId, updateBoxRequestEntity)
    }

}
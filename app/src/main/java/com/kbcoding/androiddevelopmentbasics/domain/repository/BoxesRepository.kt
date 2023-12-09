package com.kbcoding.androiddevelopmentbasics.domain.repository

import com.kbcoding.androiddevelopmentbasics.model.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.model.boxes.entities.BoxAndSettings
import kotlinx.coroutines.flow.Flow

interface BoxesRepository {

    /**
     * Get the list of boxes.
     * @param onlyActive if set to `true` then only active boxes are emitted.
     */
    suspend fun getBoxesAndSettings(onlyActive: Boolean = false): Flow<List<BoxAndSettings>>

    /**
     * Mark the specified box as active. Only active boxes are displayed in dashboard screen.
     * @throws StorageException
     */
    suspend fun activateBox(box: Box)

    /**
     * Mark the specified box as inactive. Inactive boxes are not displayed in dashboard screen.
     * @throws StorageException
     */
    suspend fun deactivateBox(box: Box)

}
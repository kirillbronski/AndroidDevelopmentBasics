package com.kbcoding.androiddevelopmentbasics.app.domain.boxes

import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxesFilter

interface BoxesSource {

    /**
     * Get the list of all boxes for the current logged-in user.
     * @throws BackendException
     * @throws ConnectionException
     * @throws ParseBackendResponseException
     */
    suspend fun getBoxes(boxesFilter: BoxesFilter): List<BoxAndSettings>

    /**
     * Set isActive flag for the specified box.
     * @throws BackendException
     * @throws ConnectionException
     * @throws ParseBackendResponseException
     */
    suspend fun setIsActive(boxId: Long, isActive: Boolean)

}
package com.kbcoding.androiddevelopmentbasics.app.model.boxes

import com.kbcoding.androiddevelopmentbasics.app.model.Empty
import com.kbcoding.androiddevelopmentbasics.app.model.Success
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.model.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.model.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.model.wrapBackendExceptions
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyFlowSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

class BoxesRepository(
    private val accountsRepository: AccountsRepository,
    private val boxesSource: BoxesSource
) {

    private var accountResponseResult: com.kbcoding.androiddevelopmentbasics.app.model.ResponseResult<Account> = Empty()

    private val boxesLazyFlowSubject = LazyFlowSubject<BoxesFilter, List<BoxAndSettings>> { filter ->
        wrapBackendExceptions { boxesSource.getBoxes(filter) }
    }

    /**
     * Get the list of boxes.
     * @return infinite flow, always success; errors are wrapped to [Result]
     */
    fun getBoxesAndSettings(filter: BoxesFilter): Flow<com.kbcoding.androiddevelopmentbasics.app.model.ResponseResult<List<BoxAndSettings>>> {
        return accountsRepository.getAccount()
            .onEach {
                accountResponseResult = it
            }
            .flatMapLatest { result ->
                if (result is Success) {
                    // has new account data -> reload boxes
                    boxesLazyFlowSubject.listen(filter)
                } else {
                    flowOf(result.map())
                }
            }
    }

    /**
     * Reload the list of boxes.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    fun reload(filter: BoxesFilter) {
        if (accountResponseResult is Error) {
            // failed to load account -> try it again;
            // after loading account, boxes will be loaded automatically
            accountsRepository.reloadAccount()
        } else {
            boxesLazyFlowSubject.reloadArgument(filter)
        }
    }

    /**
     * Mark the specified box as active. Only active boxes are displayed in dashboard screen.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    suspend fun activateBox(box: Box) = wrapBackendExceptions {
        boxesSource.setIsActive(box.id, true)
        boxesLazyFlowSubject.reloadAll(silentMode = true)
    }

    /**
     * Mark the specified box as inactive. Inactive boxes are not displayed in dashboard screen.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    suspend fun deactivateBox(box: Box) = wrapBackendExceptions {
        boxesSource.setIsActive(box.id, false)
        boxesLazyFlowSubject.reloadAll(silentMode = true)
    }

}
package com.kbcoding.androiddevelopmentbasics.app.domain.boxes

import com.kbcoding.androiddevelopmentbasics.app.domain.Empty
import com.kbcoding.androiddevelopmentbasics.app.domain.ResponseResult
import com.kbcoding.androiddevelopmentbasics.app.domain.Success
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.AccountsRepository
import com.kbcoding.androiddevelopmentbasics.app.domain.accounts.entities.Account
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.Box
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxAndSettings
import com.kbcoding.androiddevelopmentbasics.app.domain.boxes.entities.BoxesFilter
import com.kbcoding.androiddevelopmentbasics.app.domain.wrapBackendExceptions
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyFlowFactory
import com.kbcoding.androiddevelopmentbasics.app.utils.async.LazyFlowSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoxesRepository @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val boxesSource: BoxesSource,
    lazyFlowFactory: LazyFlowFactory
) {

    private var accountResult: ResponseResult<Account> = Empty()

    private val boxesLazyFlowSubject: LazyFlowSubject<BoxesFilter, List<BoxAndSettings>> =
        lazyFlowFactory.createLazyFlowSubject { filter ->
            wrapBackendExceptions { boxesSource.getBoxes(filter) }
        }

    /**
     * Get the list of boxes.
     * @return infinite flow, always success; errors are wrapped to [Result]
     */
    fun getBoxesAndSettings(filter: BoxesFilter): Flow<ResponseResult<List<BoxAndSettings>>> {
        return accountsRepository.getAccount()
            .onEach {
                accountResult = it
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
        if (accountResult is Error) {
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
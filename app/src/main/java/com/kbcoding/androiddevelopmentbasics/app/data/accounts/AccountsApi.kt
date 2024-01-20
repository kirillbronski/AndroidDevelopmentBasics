package com.kbcoding.androiddevelopmentbasics.app.data.accounts

import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.GetAccountResponseEntity
import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.SignInRequestEntity
import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.SignInResponseEntity
import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.SignUpRequestEntity
import com.kbcoding.androiddevelopmentbasics.app.data.accounts.entities.UpdateUsernameRequestEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AccountsApi {

    @POST("sign-in")
    suspend fun signIn(@Body body: SignInRequestEntity): SignInResponseEntity

    @POST("sign-up")
    suspend fun signUp(@Body body: SignUpRequestEntity)

    @GET("me")
    suspend fun getAccount(): GetAccountResponseEntity

    @PUT("me")
    suspend fun setUsername(@Body body: UpdateUsernameRequestEntity)

}
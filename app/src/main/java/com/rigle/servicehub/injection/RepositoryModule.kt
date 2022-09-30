package com.rigle.servicehub.injection

import com.rigle.servicehub.data.api.ApiService
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.data.repository.BaseRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun providesMainRepo(apiHelper: BaseRepoImpl): BaseRepo =apiHelper

    @Singleton
    @Provides
    fun providesApiRepo(apiServiceHelper: ApiService,sharedPrefManager: SharedPrefManager): BaseRepoImpl{
        return BaseRepoImpl(apiServiceHelper, sharedPrefManager)
    }
}
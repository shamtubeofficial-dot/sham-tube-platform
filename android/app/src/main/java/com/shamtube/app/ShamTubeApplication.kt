package com.shamtube.app

import android.app.Application
import com.shamtube.app.data.LocalAuthRepository
import com.shamtube.app.data.LocalShamTubeRepository
import com.shamtube.app.data.ShamTubeRepository
import com.shamtube.app.data.local.ShamTubeDatabase

class ShamTubeApplication : Application() {
    lateinit var repository: ShamTubeRepository
        private set

    lateinit var authRepository: LocalAuthRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = LocalShamTubeRepository(ShamTubeDatabase.create(this))
        authRepository = LocalAuthRepository()
    }
}
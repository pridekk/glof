package com.pridekk.getlandonfoot

import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.data.remote.GlofApi
import com.pridekk.getlandonfoot.data.remote.SpecApi
import com.pridekk.getlandonfoot.repository.GlofRepository
import com.pridekk.getlandonfoot.repository.SpecRepository
import com.pridekk.getlandonfoot.utils.Constants.GLOF_BASE_URL
import com.pridekk.getlandonfoot.utils.Constants.SPEC_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSpecRepository(
        api: SpecApi
    ) = SpecRepository(api)

    @Singleton
    @Provides
    fun provideSpecApi(): SpecApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SPEC_BASE_URL)
            .build()
            .create(SpecApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGlofRepository(
        api: GlofApi
    ) = GlofRepository(api)

    @Singleton
    @Provides
    fun provideGlofApi(): GlofApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GLOF_BASE_URL)
            .build()
            .create(GlofApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() =  FirebaseAuth.getInstance()
}
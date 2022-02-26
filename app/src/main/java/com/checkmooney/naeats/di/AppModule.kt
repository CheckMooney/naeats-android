package com.checkmooney.naeats.di

import android.content.Context
import com.checkmooney.naeats.data.MenuDataSource
import com.checkmooney.naeats.data.MenuFakeDataSource
import com.checkmooney.naeats.data.MenuRepository
import com.checkmooney.naeats.data.LoginLocalDataSource
import com.checkmooney.naeats.service.LoginApiService
import com.checkmooney.naeats.service.GoogleService
import com.checkmooney.naeats.service.MainApiService
import com.checkmooney.naeats.service.SharedPrefService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Interceptor.Companion.invoke
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserService(@ApplicationContext context: Context): GoogleService {
        return GoogleService(context)
    }

    @Provides
    @Singleton
    fun provideSharedPrefService(@ApplicationContext context: Context): SharedPrefService {
        return SharedPrefService(context)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoginInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = "http://13.209.72.236:4000/api/"

    @LoginInterceptorOkHttpClient
    @Provides
    fun provideLoginInterceptorOkHttpClient(): OkHttpClient {
        val interceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @MainInterceptorOkHttpClient
    @Provides
    fun provideMainInterceptorOkHttpClient(loginLocalDataSource: LoginLocalDataSource): OkHttpClient {
        val interceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .addInterceptor(invoke { chain ->
                val builder = chain.request().newBuilder()
                if (loginLocalDataSource.accessToken.isNotEmpty()) {
                    builder.addHeader(
                        "Authorization", "Bearer ${loginLocalDataSource.accessToken}"
                    )
                }
                chain.proceed(builder.build())
            })
            .build()
    }

}




@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideLoginApiService(
        @BaseUrl baseUrl: String,
        @LoginInterceptorOkHttpClient okHttpClient: OkHttpClient
    ): LoginApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginApiService::class.java)
    }

    @Provides
    fun provideMainApiService(
        @BaseUrl baseUrl: String,
        @MainInterceptorOkHttpClient okHttpClient: OkHttpClient
    ): MainApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MainApiService::class.java)
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class FakeMenuDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteMenuDataSource


    @Provides
    @FakeMenuDataSource
    fun provideFakeMenuDataSource(): MenuDataSource = MenuFakeDataSource

    @Provides
    fun provideMenuRepository(
        @FakeMenuDataSource fakeMenuDataSource: MenuDataSource
    ): MenuRepository {
        return MenuRepository(fakeMenuDataSource)
    }
}

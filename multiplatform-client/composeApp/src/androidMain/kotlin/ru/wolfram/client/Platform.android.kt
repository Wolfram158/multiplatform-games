package ru.wolfram.client

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.wolfram.client.data.common.DATA_STORE_FILE_NAME
import ru.wolfram.client.data.common.createDataStore

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val baseHttpUrl: String
    get() = "http://10.0.2.2:8080/api/v1"

actual val wsHost: String
    get() = "10.0.2.2"

actual class HttpClientEngineFactory {
    actual fun getHttpClientEngine(): HttpClientEngine {
        return OkHttp.create()
    }
}

actual class DispatcherIO actual constructor() {
    actual fun getDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}

actual val wsPort: Int
    get() = 8080

actual class Logger actual constructor() {
    actual fun log(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}

actual val dataStoreModule: Module
    get() = module { single { createDataStore(androidContext()) } }

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
)
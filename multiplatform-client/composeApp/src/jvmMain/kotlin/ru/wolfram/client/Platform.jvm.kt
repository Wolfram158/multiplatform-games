package ru.wolfram.client

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.wolfram.client.data.common.ClipboardManager
import ru.wolfram.client.data.common.DATA_STORE_FILE_NAME
import ru.wolfram.client.data.common.createDataStore
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val baseHttpUrl: String
    get() = "http://127.0.0.1:8080/api/v1"

actual val wsHost: String
    get() = "127.0.0.1"

actual class HttpClientEngineFactory {
    actual fun getHttpClientEngine(): HttpClientEngine {
        return CIO.create()
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
        println("$tag: $msg")
    }
}

actual val dataStoreModule: Module
    get() = module { single { createDataStore() } }

fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val file = File(System.getProperty("java.io.tmpdir"), DATA_STORE_FILE_NAME)
        file.absolutePath
    }
)

@Composable
actual fun BackHandle(onBackHandle: () -> Unit) {
}

actual val clipboardModule: Module
    get() = module {
        single {
            object : ClipboardManager {
                override fun copyToClipboard(text: String) {
                    val stringSelection = StringSelection(text)
                    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                    clipboard.setContents(stringSelection, null)
                }
            }
        }
    }
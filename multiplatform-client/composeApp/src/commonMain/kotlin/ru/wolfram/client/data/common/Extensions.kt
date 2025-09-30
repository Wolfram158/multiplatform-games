package ru.wolfram.client.data.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

const val DATA_STORE_FILE_NAME = "storage.preferences_pb"
const val NAME_KEY = "name"
const val KEY_KEY = "key"
const val PATH_KEY = "path"
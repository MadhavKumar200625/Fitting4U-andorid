package com.fitting4u.fitting4u.Data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object AuthPreferenceKeys {
    val PHONE = stringPreferencesKey("user_phone")
    val IS_BOUTIQUE = booleanPreferencesKey("is_boutique")
}
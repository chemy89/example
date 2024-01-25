package kr.jeongsejong.core.persistence.sharedpreferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kr.jeongsejong.core.persistence.spec.Persistence

class EncryptedSharedPreferencesPersistence constructor(
    context: Context,
    name: String
) : Persistence by Persistence.create(
    name,
    EncryptedSharedPreferences.create(
        name,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
)

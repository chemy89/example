package kr.jeongsejong.core.persistence.sharedpreferences

import android.content.Context
import kr.jeongsejong.core.persistence.spec.Persistence

class SharedPreferencesPersistence constructor(
    context: Context,
    name: String
) : Persistence by Persistence.create(
    name,
    context.getSharedPreferences(name, Context.MODE_PRIVATE)
)

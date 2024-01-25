package kr.jeongsejong.core.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class OffsetDateTimeAdapter : JsonDeserializer<OffsetDateTime> {

    private val dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): OffsetDateTime {
        return OffsetDateTime.parse(json?.asString, DateTimeFormatter.ofPattern(dateTimePattern))
    }
}

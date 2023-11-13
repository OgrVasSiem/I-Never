package com.foresko.gamenever.core.apollo.adapters

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DateTimeAdapter : Adapter<OffsetDateTime> {

    override fun fromJson(
        reader: JsonReader,
        customScalarAdapters: CustomScalarAdapters
    ): OffsetDateTime {
        return OffsetDateTime.parse(reader.nextString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    override fun toJson(
        writer: JsonWriter,
        customScalarAdapters: CustomScalarAdapters,
        value: OffsetDateTime
    ) {
        value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME).let(writer::value)
    }

}
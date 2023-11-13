package com.foresko.gamenever.core.apollo.adapters

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import java.math.BigDecimal

object DecimalAdapter : Adapter<BigDecimal> {

    override fun fromJson(
        reader: JsonReader,
        customScalarAdapters: CustomScalarAdapters
    ): BigDecimal {
        return BigDecimal(reader.nextString())
    }

    override fun toJson(
        writer: JsonWriter,
        customScalarAdapters: CustomScalarAdapters,
        value: BigDecimal
    ) {
        writer.value(value.toPlainString())
    }
}
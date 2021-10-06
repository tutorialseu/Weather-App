package eu.tutorials.weatherapp

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

//Todo 5 create a Serializer class
object WeatherResponseSerializer:Serializer<WeatherUpdate> {
    override val defaultValue: WeatherUpdate
        get() = WeatherUpdate.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): WeatherUpdate{
        try {
            return WeatherUpdate.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: WeatherUpdate, output: OutputStream) {
        t.writeTo(output)
    }
}
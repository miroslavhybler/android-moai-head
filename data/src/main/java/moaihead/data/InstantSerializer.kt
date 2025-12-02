package moaihead.data

import kotlinx.serialization.Serializable
import java.time.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * @author Miroslav Hýbler <br>
 * created on 02.12.2025
 */
object InstantSerializer : KSerializer<Instant> {

    override val descriptor = PrimitiveSerialDescriptor(
        serialName = Instant::class.qualifiedName!!,
        kind = PrimitiveKind.LONG,
    )

    override fun serialize(encoder: Encoder, value: Instant) {
        return encoder.encodeLong(value = value.toEpochMilli())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.ofEpochMilli(decoder.decodeLong())
    }
}

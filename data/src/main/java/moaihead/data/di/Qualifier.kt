// In :data module, e.g., in a file named "DiQualifiers.kt"
package moaihead.data.di

import javax.inject.Qualifier

/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Qualifier
@Retention(value = AnnotationRetention.BINARY)
annotation class LocalSource

/**
 * @author Miroslav Hýbler <br>
 * created on 04.12.2025
 */
@Qualifier
@Retention(value = AnnotationRetention.BINARY)
annotation class RemoteSource

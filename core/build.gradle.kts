import org.beatonma.commons.buildsrc.kts.extensions.coroutines

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
}

dependencies {
    val implementations = arrayOf(
        Dependencies.AndroidX.CORE_KTX,
        *coroutines
    )

    implementations.forEach(::implementation)
}

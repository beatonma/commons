import org.beatonma.commons.buildsrc.kts.extensions.buildConfigStrings
import org.beatonma.commons.buildsrc.kts.extensions.coroutines
import org.beatonma.commons.buildsrc.kts.extensions.main
import org.beatonma.commons.buildsrc.local.LocalConfig

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
}

android {
    defaultConfig {
        buildConfigStrings(
            "COMMONS_API_KEY" to LocalConfig.Api.Commons.API_KEY
        )
    }
}

dependencies {
    main {
        implementations(
            Dependencies.AndroidX.CORE_KTX,

            *coroutines,

            Dependencies.Retrofit.RETROFIT,
            Dependencies.Retrofit.Converter.MOSHI,
            Dependencies.Retrofit.Converter.TEXT,

            project(":core"),
            project(":network-core")
        )
    }
}

object Plugins {
    /**
     * [org.beatonma.commons.buildsrc.kts.plugin.CommonsApplicationModule]
     * Apply standard plugins, android configuration block, repositories and basic dependencies.
     */
    const val COMMONS_APPLICATION_CONFIG = "commons-application-module"

    /**
     * [org.beatonma.commons.buildsrc.kts.plugin.CommonsLibraryModule]
     * Apply standard plugins, android configuration block, repositories and basic dependencies.
     */
    const val COMMONS_LIBRARY_CONFIG = "commons-library-module"

    /**
     * [org.beatonma.commons.buildsrc.kts.plugin.CommonsHiltModule]
     * Apply required Dagger/Hilt plugins and dependencies.
     */
    const val COMMONS_HILT_MODULE = "commons-hilt-module"

    /**
     * [org.beatonma.commons.buildsrc.kts.plugin.CommonsRoomModule]
     * Apply required Room dependencies.
     */
    const val COMMONS_ROOM_MODULE = "commons-room-module"
}

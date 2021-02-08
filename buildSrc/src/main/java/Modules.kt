import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

/**
 * Local modules
 */
enum class Modules(private val moduleName: String) {
    App("app"),
    BuildSrc("buildSrc"),
    Compose("compose"),
    Core("core"),
    NetworkCore("network-core"),
    Persistence("persistence"),
    Repository("repo"),
    SampleData("sampledata"),
    Snommoc("snommoc"),
    Svg("svg"),
    Test("test"),
    TestCompose("testcompose"),
    TestHilt("testhilt"),
    Theme("theme"),
    ThemePreview("themepreview"),
    UkParliament("ukparliament"),
    ;

    override fun toString(): String = ":$moduleName"
}

fun DependencyHandler.project(module: Modules) = project(module.toString())

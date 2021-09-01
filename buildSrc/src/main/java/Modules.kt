/**
 * Local modules
 */
enum class Modules(private val moduleName: String) {
    App("app"),
    BuildSrc("buildSrc"),
    ComposeCore("compose-core"),
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
    AppTheme("app-theme"),
    ThemeCore("theme-core"),
    ThemePreview("themepreview"),
    UkParliament("ukparliament"),
    ;

    override fun toString(): String = ":$moduleName"
}

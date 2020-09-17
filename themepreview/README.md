An sample @Composable that lets you see the effects of your MaterialTheme.

![Example from Commons](https://beatonma.org/static/composethemepreview.webm)

To use:

    @Composable
    @Preview
    fun MyThemePreview() {
        ThemePreview(theme = { isDark, content -> MyTheme(isDark, content) })
    }

Then run MyThemePreview via context menu to show the preview on your device.

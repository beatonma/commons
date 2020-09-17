An sample @Composable that lets you see the effects of your MaterialTheme.

<img title="Typography" src="https://beatonma.org/static/upload/composethemepreview/typography.png" width="20%"/> <img title="Colors" src="https://beatonma.org/static/upload/composethemepreview/color.png" width="20%"/> <img title="Widgets" src="https://beatonma.org/static/upload/composethemepreview/widget.png" width="20%"/> <img title="Dark" src="https://beatonma.org/static/upload/composethemepreview/widget-dark.png" width="20%"/>

[Video demo](https://beatonma.org/static/upload/composethemepreview/demo.webm)

To use:

    @Composable
    @Preview
    fun MyThemePreview() {
        ThemePreview(theme = { isDark, content -> MyTheme(isDark, content) })
    }

Then run MyThemePreview via context menu to show the preview on your device.

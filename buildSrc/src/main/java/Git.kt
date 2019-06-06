import org.gradle.api.Project
import java.io.File

private fun String?.letIfEmpty(fallback: String): String {
    return if (this == null || isEmpty()) {
        fallback
    } else {
        this
    }
}

private fun String?.execute(workingDir: File, fallback: String): String {
    Runtime.getRuntime().exec(this, null, workingDir).let { process ->
        process.waitFor()
        return try {
            process.inputStream.reader().readText().trim().letIfEmpty(fallback)
        } catch (e: Exception) {
            fallback
        }
    }
}

object Git {
    fun sha(project: Project): String {
        // query git for the SHA, Tag and commit count. Use these to automate versioning.
        return "git rev-parse --short HEAD".execute(project.rootDir, "none")
    }

    fun tag(project: Project): String {
        return "git describe --tags".execute(project.rootDir, "dev")
    }

    fun commitCount(project: Project): Int {
        return "git rev-list --count HEAD".execute(project.rootDir, "0").toInt()
    }
}

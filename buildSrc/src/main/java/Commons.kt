object Commons {
    const val APPLICATION_ID = "org.beatonma.commons"

    object Sdk {
        const val MIN = 26
        const val TARGET = 30
        const val COMPILE = 30
    }

    object Social {
        const val MAX_COMMENT_LENGTH = 240
    }

    object Account {
        object Username {
            const val MIN_LENGTH = 4
            const val MAX_LENGTH = 16
            const val ALLOWED_CHARACTERS =
                "-_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        }
    }
}

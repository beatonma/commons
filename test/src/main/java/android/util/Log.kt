package android.util

class Log {
    companion object {
        @JvmStatic
        fun d(tag: String, message: String): Int {
            println("d/$tag: $message")
            return 0
        }

        @JvmStatic
        fun e(tag: String, message: String): Int {
            println("e/$tag: $message")
            return 0
        }

        @JvmStatic
        fun i(tag: String, message: String): Int {
            println("i/$tag: $message")
            return 0
        }

        @JvmStatic
        fun v(tag: String, message: String): Int {
            println("v/$tag: $message")
            return 0
        }

        @JvmStatic
        fun w(tag: String, message: String): Int {
            println("w/$tag: $message")
            return 0
        }
    }
}

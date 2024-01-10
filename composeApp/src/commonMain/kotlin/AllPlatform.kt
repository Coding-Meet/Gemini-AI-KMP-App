import utils.TYPE

class AllPlatform {
    private val platform = getPlatform()

    fun type(): TYPE {
        return platform.type
    }
}
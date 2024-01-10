import utils.TYPE

class JVMPlatform: Platform {
    override val type = TYPE.DESKTOP
}

actual fun getPlatform(): Platform = JVMPlatform()
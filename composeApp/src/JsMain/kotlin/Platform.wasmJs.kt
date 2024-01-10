import utils.TYPE

class WasmPlatform: Platform {
    override val type = TYPE.WEB
}

actual fun getPlatform(): Platform = WasmPlatform()
import android.os.Build
import utils.TYPE

class AndroidPlatform : Platform {
    override val type = TYPE.MOBILE
}

actual fun getPlatform(): Platform = AndroidPlatform()
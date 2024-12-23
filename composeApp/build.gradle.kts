import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelight)
}

kotlin {
    js(IR) {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    androidTarget {
        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.sqldelight.android.driver)

            implementation(libs.ktor.client.okhttp)

            // koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            // kstore
            implementation(libs.kstore.file)

            // Splash API
            implementation(libs.androidx.core.splashscreen)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Viewmodel
            implementation(libs.androidx.viewmodel.compose)

            // date time
            implementation(libs.kotlinx.datetime)

            // local storage like share preference
            implementation(libs.multiplatform.settings.no.arg)

            // SQLDelight
            implementation(libs.sqldelight.coroutine.ext)
            implementation(libs.sqldelight.primitive.adapters)

            //Kermit  for logging
            implementation(libs.kermit)

            //Coroutines
            api(libs.kotlinx.coroutines)

            // koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            //Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)

            // markdown
            implementation(libs.multiplatform.markdown.renderer)

            // kstore
            implementation(libs.kstore)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)

            implementation(libs.sqlite.driver)

            implementation(libs.ktor.client.java)

            // kstore
            implementation(libs.kstore.file)
        }
        iosMain.dependencies {
            // sqlite
            implementation(libs.sqldelight.native.driver)

            // ktor
            implementation(libs.ktor.client.darwin)

            // file picker
            implementation(libs.mpfilepicker)

            // kstore
            implementation(libs.kstore.file)
        }

        jsMain.dependencies {
            implementation(compose.html.core)

            implementation(libs.ktor.client.js)
            implementation(libs.web.worker.driver)
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.1"))
            implementation(npm("sql.js", "1.8.0"))

            // kstore
            implementation(libs.kstore.storage)
        }
    }
}
sqldelight {
    databases {
        create("GeminiApiChatDB") {
            packageName.set("com.coding.meet.gaminiaikmp")
            generateAsync.set(true)
        }
    }
    linkSqlite = true
}
android {
    namespace = "com.coding.meet.gaminiaikmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.coding.meet.gaminiaikmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.coding.meet.gaminiaikmp"
            packageVersion = "1.0.0"

            macOS {
                iconFile.set(project.file("gemini_logo.icns"))
            }
            windows {
                iconFile.set(project.file("gemini_logo.ico"))
            }
            linux {
                iconFile.set(project.file("gemini_logo.png"))
            }
        }
    }
}
buildkonfig {
    packageName = "com.coding.meet.gaminiaikmp"

    val localProperties =
        Properties().apply {
            val propsFile = rootProject.file("local.properties")
            if (propsFile.exists()) {
                load(propsFile.inputStream())
            }
        }

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "GEMINI_API_KEY",
            localProperties["GEMINI_API_KEY"]?.toString() ?: "",
        )
        buildConfigField(
            FieldSpec.Type.BOOLEAN,
            "isDebug", "false"
        )
    }
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.WARNING // NONE | FAIL
    rootProject.the<YarnRootExtension>().reportNewYarnLock = false // true
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = false // true
}
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.buildkonfig)
    kotlin("plugin.serialization") version "1.9.21"
    id("app.cash.sqldelight") version "2.0.1"
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
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            api(libs.spotlight.android)
            implementation(libs.sqldelight.android.driver)

            implementation(libs.ktor.client.okhttp)

            // koin
            implementation(libs.koin.android)

            // file picker
            implementation(libs.mpfilepicker)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            @OptIn(ExperimentalComposeLibrary::class) implementation(compose.components.resources)


            // precompose
            api(libs.precompose)
            api(libs.precompose.viewmodel)
            api(libs.precompose.koin)

            // date time
            implementation(libs.kotlinx.datetime)

            // markdown text show
            implementation(libs.multiplatform.markdown.renderer)

            // local storage like share preference
            implementation(libs.multiplatform.settings.no.arg)

            // SQLDelight
            implementation(libs.spotlight)
            implementation(libs.sqldelight.coroutine.ext)
            implementation(libs.sqldelight.primitive.adapters)

            //Kermit  for logging
            implementation(libs.kermit)

            //Coroutines
            api(libs.kotlinx.coroutines)

            // koin
            implementation(libs.koin.core)

            //Ktor
            with(libs.ktor.client) {
                implementation(core)
                implementation(content.negotiation)
                implementation(serialization)
                api(logging)
            }


        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)

            implementation(libs.sqlite.driver)

            implementation(libs.ktor.client.java)

            // file picker
            implementation(libs.mpfilepicker)
        }

        jsMain.dependencies {
            implementation(compose.html.core)

            implementation(libs.ktor.client.js)
            implementation(libs.web.worker.driver)
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.1"))
            implementation(npm("sql.js", "1.8.0"))

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.coding.meet.gaminiaikmp"
            packageVersion = "1.0.0"
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
    }
}
compose.experimental {
    web.application {}
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.WARNING // NONE | FAIL
    rootProject.the<YarnRootExtension>().reportNewYarnLock = false // true
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = false // true
}
// Workaround yarn concurrency issue - https://youtrack.jetbrains.com/issue/KT-43320
//tasks.withType(org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask::class.java).configureEach {
//    args.addAll(listOf("--mutex", "file:${file("../build/.yarn-mutex")}"))
//}
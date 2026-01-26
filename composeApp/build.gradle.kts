import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.composeHotReload)
}

kotlin {

    androidTarget {
        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
    }

    jvm("desktop")

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // Required when using NativeSQLiteDriver
            linkerOpts.add("-lsqlite3")
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
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
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material.icons.extended)
            implementation(libs.material3)

            // Viewmodel
            implementation(libs.androidx.viewmodel.compose)

            // date time
            implementation(libs.kotlinx.datetime)

            // local storage like share preference
            implementation(libs.multiplatform.settings.no.arg)

            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)

            // Kermit  for logging
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

            implementation(libs.sqldelight.sqlite.driver)

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

        @OptIn(ExperimentalWasmDsl::class)
        wasmJsMain.dependencies {
            implementation(compose.html.core)

            implementation(libs.ktor.client.js)
            implementation(libs.sqldelight.web.driver)
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.1.0"))
            implementation(npm("sql.js", libs.versions.sqlJs.get()))
            implementation(devNpm("copy-webpack-plugin", libs.versions.webPackPlugin.get()))
            // kstore
            implementation(libs.kstore.storage)
        }
    }
}
dependencies {
    debugImplementation(libs.ui.tooling)
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
        mainClass = "com.coding.meet.gaminiaikmp.MainKt"

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
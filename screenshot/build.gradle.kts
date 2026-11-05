plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))

    // https://github.com/Coding-Meet/CMPDevicePreview
    implementation(project(":cmpdevicepreview"))

    // this official libary but i have some issue screenshort frame and dialog not work show i change code
    // https://github.com/Apadmi-Engineering/CMPDevicePreview
    // implementation("com.apadmi:cmpdevicepreview:0.1.1")

    implementation(compose.desktop.currentOs)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.compose.components.resources)
    implementation(libs.kotlinx.coroutinesSwing)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.compose)

    implementation(libs.compose.uiToolingPreview)
}

tasks.register<JavaExec>("exportDeviceScreenshots") {
    group = "documentation"
    description = "Exports realistic framed device screenshots of Gemini AI KMP App."
    mainClass.set("com.coding.meet.gaminiaikmp.screenshot.ExportScreenshotsKt")
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = rootProject.projectDir
    val outDir = project.findProperty("outputDir")?.toString() ?: "${rootProject.projectDir}/image"
    args = listOf(outDir)
}


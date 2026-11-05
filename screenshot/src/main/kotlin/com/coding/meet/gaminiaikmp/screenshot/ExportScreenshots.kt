package com.coding.meet.gaminiaikmp.screenshot

import com.apadmi.cmpdevicepreview.devices.DevicePresets
import java.io.File

/**
 * Entry point for exporting framed screenshots across all platforms in both Dark and Light themes:
 * - Android (Pixel 9 Realistic Hardware Frame)
 * - iOS (iPhone 16 Pro Realistic Frame & Dynamic Island)
 * - macOS (Large Desktop Window Two-Pane Layout)
 * - Web (Wasm Chrome Browser Two-Pane Layout)
 */
fun main(args: Array<String>) {
    val baseDir = if (args.isNotEmpty()) File(args[0]) else File("image")
    val androidDir = File(baseDir, "android").apply { mkdirs() }
    val iosDir = File(baseDir, "ios").apply { mkdirs() }
    val macDir = File(baseDir, "mac").apply { mkdirs() }
    val webDir = File(baseDir, "web").apply { mkdirs() }

    val androidDarkDir = File(androidDir, "dark").apply { mkdirs() }
    val androidLightDir = File(androidDir, "light").apply { mkdirs() }
    val iosDarkDir = File(iosDir, "dark").apply { mkdirs() }
    val iosLightDir = File(iosDir, "light").apply { mkdirs() }
    val macDarkDir = File(macDir, "dark").apply { mkdirs() }
    val macLightDir = File(macDir, "light").apply { mkdirs() }
    val webDarkDir = File(webDir, "dark").apply { mkdirs() }
    val webLightDir = File(webDir, "light").apply { mkdirs() }

    // Clear previous images to ensure clean numbering
    clearDirectory(androidDarkDir)
    clearDirectory(androidLightDir)
    clearDirectory(iosDarkDir)
    clearDirectory(iosLightDir)
    clearDirectory(macDarkDir)
    clearDirectory(macLightDir)
    clearDirectory(webDarkDir)
    clearDirectory(webLightDir)

    // Clean flat legacy files in platform root folders
    cleanFlatFiles(androidDir)
    cleanFlatFiles(iosDir)
    cleanFlatFiles(macDir)
    cleanFlatFiles(webDir)

    val promoImageFile = File("image/Gemini-AI-KMP-App.png")
    val sampleImageBytes = if (promoImageFile.exists()) promoImageFile.readBytes() else ByteArray(0)

    println("=== Exporting All Screen States with Descriptive Numbered Prefixes (Dark & Light) ===")

    // 1. Android (Google Pixel 9 Realistic Frame)
    val pixelPreset = DevicePresets.pixel9
    println("\n[1/4] Generating Android screen states (${pixelPreset.name})...")
    println("  -> Dark Mode...")
    exportMobileStates(
        preset = pixelPreset,
        outputDir = androidDarkDir,
        sampleImageBytes = sampleImageBytes,
        isDark = true
    )
    println("  -> Light Mode...")
    exportMobileStates(
        preset = pixelPreset,
        outputDir = androidLightDir,
        sampleImageBytes = sampleImageBytes,
        isDark = false
    )

    // 2. iOS (Apple iPhone 16 Pro Realistic Frame)
    val iPhonePreset = DevicePresets.iPhone16Pro
    println("\n[2/4] Generating iOS screen states (${iPhonePreset.name})...")
    println("  -> Dark Mode...")
    exportMobileStates(
        preset = iPhonePreset,
        outputDir = iosDarkDir,
        sampleImageBytes = sampleImageBytes,
        isDark = true
    )
    println("  -> Light Mode...")
    exportMobileStates(
        preset = iPhonePreset,
        outputDir = iosLightDir,
        sampleImageBytes = sampleImageBytes,
        isDark = false
    )

    // 3. macOS (Desktop Two-Pane Window Frame)
    val macPreset = DevicePresets.largeDesktopWindow
    println("\n[3/4] Generating macOS desktop two-pane screen states (${macPreset.name})...")
    println("  -> Dark Mode...")
    exportDesktopStates(
        preset = macPreset,
        outputDir = macDarkDir,
        sampleImageBytes = sampleImageBytes,
        isDark = true
    )
    println("  -> Light Mode...")
    exportDesktopStates(
        preset = macPreset,
        outputDir = macLightDir,
        sampleImageBytes = sampleImageBytes,
        isDark = false
    )

    // 4. Web (Chrome Browser Two-Pane Frame)
    println("\n[4/4] Generating Web (Wasm) two-pane screen states...")
    println("  -> Dark Mode...")
    exportWebStates(
        outputDir = webDarkDir,
        sampleImageBytes = sampleImageBytes,
        isDark = true
    )
    println("  -> Light Mode...")
    exportWebStates(
        outputDir = webLightDir,
        sampleImageBytes = sampleImageBytes,
        isDark = false
    )

    println("\n=== All Screen States Exported Successfully! ===")
}

private fun clearDirectory(dir: File) {
    dir.listFiles()?.filter { it.isFile }?.forEach { it.delete() }
}

private fun cleanFlatFiles(dir: File) {
    dir.listFiles()?.filter { it.isFile && it.name.endsWith(".png") }?.forEach { it.delete() }
}

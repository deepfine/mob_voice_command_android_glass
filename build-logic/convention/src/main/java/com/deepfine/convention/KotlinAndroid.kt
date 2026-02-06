package com.deepfine.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal fun Project.configureKotlinAndroid(
  extension: LibraryExtension,
) {
  val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

  extension.apply {
    compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()

    defaultConfig {
      minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
    }

    kotlinExtension.jvmToolchain(17)

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
      buildConfig = false
    }
  }
}

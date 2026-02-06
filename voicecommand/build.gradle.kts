import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
  alias(libs.plugins.deepfine.android)
  alias(libs.plugins.maven.publish)
  alias(libs.plugins.compose.compiler)
}

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("release") {
        groupId = "com.github.deepfine"
        artifactId = "voicecommand-android-glass"
        version = libs.versions.versionName.get()

        afterEvaluate {
          from(components["release"])
        }
      }
    }
  }
}

android {
  namespace = "com.deepfine.voicecommand"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
  }

  publishing {
    singleVariant("release")
  }

  kotlinExtension.jvmToolchain(17)

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.compose)
}

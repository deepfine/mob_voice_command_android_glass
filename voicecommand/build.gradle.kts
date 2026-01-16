plugins {
  alias(libs.plugins.deepfine.presentation)
  alias(libs.plugins.maven.publish)
}

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("release") {
        from(components["release"])
        groupId = "com.github.deepfine"
        artifactId = "voicecommand-android-glass"
        version = libs.versions.versionName.get()
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

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

dependencies {
}

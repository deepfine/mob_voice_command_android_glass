plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.compose.gradlePlugin)
  compileOnly(libs.spotless.composeRuleset)
}

gradlePlugin {
  plugins {
    register("AndroidPlugin") {
      id = "com.deepfine.android.plugin"
      implementationClass = "AndroidConventionPlugin"
    }

    register("ComposePlugin") {
      id = "com.deepfine.compose.plugin"
      implementationClass = "ComposeConventionPlugin"
    }
  }
}

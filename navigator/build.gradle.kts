plugins {
  alias(libs.plugins.deepfine.android)
  alias(libs.plugins.deepfine.compose)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.deepfine.navigator"
}

dependencies {
  implementation(projects.dataApi)

  implementation(libs.androidx.compose.navigation3.runtime)
  implementation(libs.androidx.compose.navigation3.ui)
  implementation(libs.kotlinx.serialization)
}

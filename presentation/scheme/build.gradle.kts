plugins {
  alias(libs.plugins.deepfine.presentation)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.deepfine.scheme"
}

dependencies {
  implementation(projects.presentation)
  implementation(projects.presentation.main.api)

  implementation(libs.splashScreen)
  implementation(libs.kotlinx.serialization)
  implementation(libs.androidx.compose.navigation3.viewModel)
}

plugins {
  alias(libs.plugins.deepfine.android)
  alias(libs.plugins.deepfine.hilt)
}

android {
  namespace = "com.deepfine.data.network"
}

dependencies {
  compileOnly(projects.buildconfigStub)
  implementation(projects.dataApi.networkApi)

  implementation(libs.kotlinx.serialization)
  implementation(libs.bundles.ktor)
}

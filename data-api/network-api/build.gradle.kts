plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  implementation(libs.kotlinx.serialization)
  implementation(libs.kotlinx.coroutine.core)
  implementation(libs.sandwich)
}

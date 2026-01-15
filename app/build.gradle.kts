plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.ksp)
}

android {
  namespace = libs.versions.applicationId.get()
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = libs.versions.applicationId.get()
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = libs.versions.versionCode.get().toInt()
    versionName = libs.versions.versionName.get()

    resValue("string", "app_name", libs.versions.applicationName.get())
  }

  buildTypes {
    debug {
      isDebuggable = true
      isMinifyEnabled = false
      isShrinkResources = false
      splits.abi.isEnable = false
      aaptOptions.cruncherEnabled = false
    }

    release {
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//            signingConfig = signingConfigs.getByName("release")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.multidex)
  implementation(libs.splashScreen)
}

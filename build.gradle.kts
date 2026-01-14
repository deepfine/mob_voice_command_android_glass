plugins {
  alias(libs.plugins.spotless)
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.compose.compiler) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
}

subprojects {
  apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
  configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
      target("**/*.kt")
      targetExclude("${layout.buildDirectory}/**/*.kt")
      ktlint().editorConfigOverride(
        mapOf(
          "indent_size" to "2",
          "continuation_indent_size" to "2",
          "ij_kotlin_allow_trailing_comma" to "true",
          "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
          "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
          "ktlint_standard_no-unused-imports" to "enabled"
        )
      )
      trimTrailingWhitespace()
      endWithNewline()
    }
    format("kts") {
      target("**/*.kts")
      targetExclude("${layout.buildDirectory}/**/*.kts")
      trimTrailingWhitespace()
      endWithNewline()
    }
  }
}

val addPreCommitGitHookOnBuild by tasks.registering {
  doLast {
    println("⚈ ⚈ ⚈ Running Add Pre Commit Git Hook Script on Build ⚈ ⚈ ⚈")

    if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
      providers.exec {
        commandLine("cmd", "/c", "copy", "/Y", "scripts\\pre-commit", ".git\\hooks\\pre-commit")
      }.result.get()
    } else {
      providers.exec {
        commandLine("cp", "scripts/pre-commit", ".git/hooks")
      }.result.get()
      providers.exec {
        commandLine("chmod", "755", ".git/hooks/pre-commit")
      }.result.get()
    }

    if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
      providers.exec {
        commandLine("cmd", "/c", "copy", "/Y", "scripts\\prepare-commit-msg", ".git\\hooks\\prepare-commit-msg")
      }.result.get()
    } else {
      providers.exec {
        commandLine("cp", "scripts/prepare-commit-msg", ".git/hooks")
      }.result.get()
      providers.exec {
        commandLine("chmod", "755", ".git/hooks/prepare-commit-msg")
      }.result.get()
    }


    println("✅ Added Pre Commit Git Hook Script.")
  }
}
tasks.named("prepareKotlinBuildScriptModel") {
  dependsOn(addPreCommitGitHookOnBuild)
}
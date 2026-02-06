plugins {
  alias(libs.plugins.spotless)
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.compose.compiler) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.maven.publish)
}

subprojects {
  apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
  configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
      target("**/src/**/*.kt", "**/src/**/*.kts")
      targetExclude("**/build/**", "**/generated/**")
      ktlint()
        .customRuleSets(
          listOf(libs.spotless.composeRuleset.get().toString()),
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

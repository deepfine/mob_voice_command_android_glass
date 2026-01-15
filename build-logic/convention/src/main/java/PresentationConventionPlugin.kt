import org.gradle.api.Project

class PresentationConventionPlugin : AndroidConvention, ComposeConvention {
  override fun apply(target: Project) {
    super<AndroidConvention>.apply(target)
    super<ComposeConvention>.apply(target)
  }
}

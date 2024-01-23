pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidDevelopmentBasics"
include(":app")
include(":myAndroidLibrary")
include(":myKotlinLibrary")
include(":uiAlerts")
include(":uiAlertsImpl")

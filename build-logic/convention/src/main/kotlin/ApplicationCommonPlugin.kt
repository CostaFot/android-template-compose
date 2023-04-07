import com.android.build.api.dsl.ApplicationExtension
import com.feelsokayman.template.configureFlavors
import com.feelsokayman.template.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationCommonPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 33
                configureFlavors(this)
            }
        }
    }

}
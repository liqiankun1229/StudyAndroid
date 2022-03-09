import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.tasks.register("my") {
            doLast {
                println("groovy Last")
            }
        }
    }
}
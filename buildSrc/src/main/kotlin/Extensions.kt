import org.gradle.api.Project

/**
 * @author: AMAL
 * Created On : 26/02/20
 */


fun Project.stringProperty(name: String): String {
    return property(name) as String
}

fun Project.intProperty(name: String): Int {
    return stringProperty(name).toInt()
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_1_8.name
        targetCompatibility = JavaVersion.VERSION_1_8.name
    }

}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

plugins {
    "kotlin-dsl"
    id("groovy")
//    id("kotlin")
    id("java-gradle-plugin")
}

sourceSets {
    main {
        groovy {
            srcDir("src/main/groovy")
        }
        java {
            srcDir("src/main/java")
        }
    }
}

//buildscript {
//    repositories {
//        gradlePluginPortal()
//    }
//    dependencies {
//        classpath(kotlin("gradle-plugin", "1.7.10"))
//    }
//}


dependencies {
    implementation(gradleKotlinDsl())
    implementation(localGroovy())
    implementation(gradleApi())
}
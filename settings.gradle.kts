//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        jcenter()
//        maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
//        maven { url "https://jitpack.io" }
//        mavenCentral()
//        google()
//    }
//}

rootProject.name = "StudyAndroid"
rootProject.buildFileName = "build.gradle.kts"

include(":app")
include(":ijkplayer-java")
include(":network")
include(":web")
include(":common")
include(":activity")
include(":download")
include(":mvp")
include(":butter")
include(":annotations")
include(":butterknife")
include(":compiler")
include(":rxjava")
include(":lib")
include(":compose")
include(":lib_plugin")
include(":navi")
//include ":library")
include(":lib_mvi")
include(":ndk")
include(":lib_opencv")
include(":lib_logger")
include(":refresh-layout-kernel")
include(":refresh-drawable-paint")

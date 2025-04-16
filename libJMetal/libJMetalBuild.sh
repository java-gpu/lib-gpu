#!/bin/zsh

export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export javaPackagePath="tech/gpu/lib/metal"
export srcFolder="src/main/java/${javaPackagePath}"
export headerOutputFolder="build/native"
export lombokGradleCacheFolder="${HOME}/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/${lombokVersion}"

listJavaFile=(
    "${srcFolder}/MetalRenderer.java"
    "${srcFolder}/util/MetalNativeLoader.java"
)

generateJNIHeaders() {
    echo "Lombok Version [${lombokVersion}], Cache folder [$lombokGradleCacheFolder]"
    export lombokJarFile=$(find "${lombokGradleCacheFolder}" | grep "$lombokVersion.jar")
    echo "Lombok Jar File [${lombokJarFile}]"
    set -x
    javac -h "${headerOutputFolder}" "${listJavaFile[@]}" -d "${BUILD_FOLDER}/classes/java/main/${javaPackagePath}" -cp "${lombokJarFile}"
}

generateCommonNativeBuild() {
  set -x
  pushd $(pwd)
  cd "src/main/native/common"
  cmake -B"${BUILD_FOLDER}/native/common" .
  popd
}

generateNativeBuild() {
  set -x
  pushd $(pwd)
  cd "src/main/native/metal"
  cmake -B"${BUILD_FOLDER}/native/metal" .
  popd
}

compileCommonNative() {
  set -x
  pushd $(pwd)
  cd "${BUILD_FOLDER}/native/common"
  cmake --build .
  popd
}


compileNative() {
  set -x
  pushd $(pwd)
  cd "${BUILD_FOLDER}/native/metal"
  cmake --build .
  popd
}

echo "Executing command ${COMMAND}"
if [[ "${COMMAND}" = "generateJNIHeaders" ]]; then
  generateJNIHeaders
elif [[ "${COMMAND}" = "generateNativeBuild" ]]; then
  generateCommonNativeBuild
  generateNativeBuild
elif [[ "${COMMAND}" = "compileNative" ]]; then
  compileCommonNative
  compileNative
fi
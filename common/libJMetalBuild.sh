#!/bin/zsh

export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export javaPackagePath="tech/gpu/lib"
export srcFolder="src/main/java/${javaPackagePath}"
export headerOutputFolder="build/native"
export lombokGradleCacheFolder="${HOME}/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/${lombokVersion}"

listJavaFile=(
    "${srcFolder}/common/GpuInfo.java"
    "${srcFolder}/util/CommonNativeLoader.java"
    "${srcFolder}/util/Disposable.java"
    "${srcFolder}/graphics/Texture.java"
    "${srcFolder}/graphics/PixelFormat.java"
    "${srcFolder}/graphics/PixelFormatConverter.java"
    "${srcFolder}/ex/PixelFormatNotSupportedException.java"
    "${srcFolder}/ex/GpuRuntimeException.java"
    "${srcFolder}/Application.java"
    "${srcFolder}/ApplicationListener.java"
    "${srcFolder}/ApplicationLogger.java"
    "${srcFolder}/Audio.java"
    "${srcFolder}/Game.java"
    "${srcFolder}/Graphics.java"
    "${srcFolder}/Input.java"
    "${srcFolder}/InputProcessor.java"
    "${srcFolder}/InputEventQueue.java"
    "${srcFolder}/LifecycleListener.java"
    "${srcFolder}/Preferences.java"
    "${srcFolder}/Screen.java"
    "${srcFolder}/audio/AudioDevice.java"
    "${srcFolder}/audio/AudioRecorder.java"
    "${srcFolder}/audio/Music.java"
    "${srcFolder}/audio/Sound.java"
    "${srcFolder}/input/GestureDetector.java"
    "${srcFolder}/input/NativeInputConfiguration.java"
    "${srcFolder}/input/TextInputWrapper.java"
)

generateJNIHeaders() {
    echo "Lombok Version [${lombokVersion}], Cache folder [$lombokGradleCacheFolder]"
    export lombokJarFile=$(find "${lombokGradleCacheFolder}" | grep "$lombokVersion.jar")
    echo "Lombok Jar File [${lombokJarFile}]"
    set -x
    javac -h "${headerOutputFolder}" "${listJavaFile[@]}" -d "${BUILD_FOLDER}/classes/java/main/${javaPackagePath}" -cp "${lombokJarFile}"
}

generateNativeBuild() {
  set -x
  pushd $(pwd)
  cd "src/main/native"
  cmake -B"${BUILD_FOLDER}/native" .
  popd
}

compileNative() {
  set -x
  pushd $(pwd)
  cd "${BUILD_FOLDER}/native"
  cmake --build .
  popd
}

echo "Executing command ${COMMAND}"
if [[ "${COMMAND}" = "generateJNIHeaders" ]]; then
  generateJNIHeaders
elif [[ "${COMMAND}" = "generateNativeBuild" ]]; then
  generateNativeBuild
elif [[ "${COMMAND}" = "compileNative" ]]; then
  compileNative
fi
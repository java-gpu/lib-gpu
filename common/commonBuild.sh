export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export javaPackagePath="tech/gpu/lib"
export srcFolder="src/main/java/${javaPackagePath}"
export headerOutputFolder="build/native"

export listJavaFile=(
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

includeScriptPath=../support-scripts/generateJniHeader.sh
chmod +x "${includeScriptPath}"
. "${includeScriptPath}" "${BUILD_FOLDER}" "${COMMAND}" "${lombokVersion}"

echo "Executing command ${COMMAND}"
if [[ "${COMMAND}" = "generateJNIHeaders" ]]; then
  generateJNIHeaders
fi
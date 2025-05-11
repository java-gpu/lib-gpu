export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export jnaVersion="$4"
export javaPackagePath="tech/lib/bgfx"
export srcFolder="src/main/java/${javaPackagePath}"
export headerOutputFolder="build/native"

# Detect platform
unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     PLATFORM=linux;;
    Darwin*)    PLATFORM=osx;;
    CYGWIN*|MINGW*|MSYS*)    platform=windows;;
    *)          PLATFORM="unknown"
esac

# Detect architecture
archOut="$(uname -m)"
case "${archOut}" in
    arm64|aarch64) ARCH=arm64;;
    x86_64|amd64)  ARCH=x86_64;;
    *)             ARCH="unknown"
esac

echo "Platform: ${PLATFORM}"
echo "Architecture: ${ARCH}"

export listJavaFile=(
    "${srcFolder}/util/PlatformInfo.java"
    "src/main/java/com/sun/jna/MyJFramePointer.java"
    "src/main/java/tech/lib/ui/event/UiEvent.java"
    "src/main/java/tech/lib/ui/enu/UiEventType.java"
)

includeScriptPath=../support-scripts/generateJniHeader.sh
chmod +x "${includeScriptPath}"
. "${includeScriptPath}" "${BUILD_FOLDER}" "${COMMAND}" "${lombokVersion}" "${jnaVersion}"

echo "Executing command ${COMMAND}"
if [[ "${COMMAND}" = "generateJNIHeaders" ]]; then
  generateJNIHeaders "${srcFolder}/jni"
  if [[ "${PLATFORM}" = "linux" ]]; then
    mkdir -p build/native
    cp -v build/generated/sources/headers/java/main/*.h build/native/
  fi
elif [[ "${COMMAND}" = "generateNativeBuild" ]]; then
  set -x
  generateNativeBuild "../native" "${BUILD_FOLDER}/native/build"
elif [[ "${COMMAND}" = "compileNative" ]]; then
  compileNative "${BUILD_FOLDER}/native/build"
  if [[ "${PLATFORM}" == "osx" ]]; then
    install_name_tool -change ../../${PLATFORM}-${ARCH}/bin/libbgfx-shared-libRelease.dylib @rpath/libbgfx-shared-libRelease.dylib ../external/bgfx/.build/${PLATFORM}-${ARCH}/bin/libbgfx_jni.dylib
    install_name_tool -change ../../${PLATFORM}-${ARCH}/bin/libbgfx-shared-libDebug.dylib @rpath/libbgfx-shared-libDebug.dylib ../external/bgfx/.build/${PLATFORM}-${ARCH}/bin/libbgfx_jni.dylib
  fi
fi
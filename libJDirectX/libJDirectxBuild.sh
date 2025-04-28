#!/bin/zsh

export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export javaPackagePath="tech/gpu/lib/directx"
export srcFolder="src/main/java/${javaPackagePath}"
export headerOutputFolder="build/native"

listJavaFile=(
    "${srcFolder}/util/DirectxNativeLoader.java"
)

source ../support-scripts/generateJniHeader.sh "${BUILD_FOLDER}" "${COMMAND}" "${lombokVersion}"

echo "Executing command ${COMMAND}"
if [[ "${COMMAND}" = "generateJNIHeaders" ]]; then
  generateJNIHeaders "src/main/java/tech/gpu/lib/directx/jni"
fi
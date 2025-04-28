#!/bin/zsh

export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export javaPackagePath="tech/gpu/lib/vulkan"
export srcFolder="src/main/java/${javaPackagePath}"
export headerOutputFolder="build/native"

export listJavaFile=(
    "${srcFolder}/util/VulkanNativeLoader.java"
)

source ../support-scripts/generateJniHeader.sh "${BUILD_FOLDER}" "${COMMAND}" "${lombokVersion}"

echo "Executing command ${COMMAND}"
if [[ "${COMMAND}" = "generateJNIHeaders" ]]; then
  generateJNIHeaders "src/main/java/tech/gpu/lib/vulkan/jni"
elif [[ "${COMMAND}" = "generateNativeBuild" ]]; then
  set -x
  # generateNativeBuild "src/main/native/common" "${BUILD_FOLDER}/native/common"
  # generateNativeBuild "src/main/native/vulkan" "${BUILD_FOLDER}/native/metal"
elif [[ "${COMMAND}" = "compileNative" ]]; then
  # compileNative "${BUILD_FOLDER}/native/common"
  # compileNative "${BUILD_FOLDER}/native/metal"
fi
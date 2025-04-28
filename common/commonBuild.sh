export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export javaPackagePath="tech/gpu/lib"
export srcFolder="src/main/java/${javaPackagePath}"
export headerOutputFolder="build/native"

export listJavaFile=(
    "${srcFolder}/utils/CommonNativeLoader.java"
    "${srcFolder}/graphics/TextureInfo.java"
)

includeScriptPath=../support-scripts/generateJniHeader.sh
chmod +x "${includeScriptPath}"
. "${includeScriptPath}" "${BUILD_FOLDER}" "${COMMAND}" "${lombokVersion}"

echo "Executing command ${COMMAND}"
if [[ "${COMMAND}" = "generateJNIHeaders" ]]; then
  generateJNIHeaders "src/main/java/tech/gpu/lib/jni"
fi
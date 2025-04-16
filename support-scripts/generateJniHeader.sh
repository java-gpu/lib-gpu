export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export headerOutputFolder="build/native"
export lombokGradleCacheFolder="${HOME}/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/${lombokVersion}"

export generateJNIHeaders() {
    echo "Lombok Version [${lombokVersion}], Cache folder [$lombokGradleCacheFolder]"
    export lombokJarFile=$(find "${lombokGradleCacheFolder}" | grep "$lombokVersion.jar")
    echo "Lombok Jar File [${lombokJarFile}]"
    set -x
    javac -h "${headerOutputFolder}" "${listJavaFile[@]}" -d "${BUILD_FOLDER}/classes/java/main/${javaPackagePath}" -cp "${lombokJarFile}"
}

export generateNativeBuild() {
  nativeSrcFolder="$1"
  nativeOutputFolder="$2"
  if [[ -d "${nativeOutputFolder}" ]]; then
    rm -rfv "${nativeOutputFolder}"
  fi
  set -x
  pushd $(pwd)
  cd "${nativeSrcFolder}"
  cmake -B"${nativeOutputFolder}" .
  popd
}

export compileNative() {
  generatedFolder="$1"
  set -x
  pushd $(pwd)
  cd "${generatedFolder}"
  cmake --build .
  popd
}


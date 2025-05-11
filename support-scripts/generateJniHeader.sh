export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export jnaVersion="$4"
export headerOutputFolder="build/native"
export lombokGradleCacheFolder="${HOME}/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/${lombokVersion}"
export jnaGradleCacheFolder="${HOME}/.gradle/caches/modules-2/files-2.1/net.java.dev.jna/jna/${jnaVersion}"

generateJNIHeaders() {
#    javaSrcFolder="$1"
    # Use find with -print0 and read null-delimited lines
#    find "${javaSrcFolder}" -name '*.java' -print0 | while IFS= read -r -d '' file; do
#      listJavaFile+=("$file")
#    done
    # Loop through all input directories
    for javaSrcFolder in "$@"; do
      if [ -d "$javaSrcFolder" ]; then
        while IFS= read -r -d '' file; do
          listJavaFile+=("$file")
        done < <(find "$javaSrcFolder" -name '*.java' -print0)
      fi
    done

    echo "Lombok Version [${lombokVersion}], Cache folder [$lombokGradleCacheFolder]"
    export lombokJarFile=$(find "${lombokGradleCacheFolder}" | grep "$lombokVersion.jar")
    echo "Lombok Jar File [${lombokJarFile}]"
    echo "JNA Version [${jnaVersion}], Cache folder [${jnaGradleCacheFolder}]"
    export jnaJarFile=$(find "${jnaGradleCacheFolder}" | grep "${jnaVersion}.jar")
    echo "JNA Jar File [${jnaJarFile}]"

    set -x
    javac -h "${headerOutputFolder}" "${listJavaFile[@]}" -d "${BUILD_FOLDER}/tmp_javac/" -cp "${lombokJarFile}:${jnaJarFile}" --processor-path "${lombokJarFile}"
    buildResult=$?
    set +x
    if [[ $buildResult -ne 0 ]]; then
      echo "Error when call generate JNI headers ${buildResult}"
      exit ${buildResult}
    else
      echo "Cleaning folder [${BUILD_FOLDER}/tmp_javac/]"
      rm -rfv "${BUILD_FOLDER}/tmp_javac/"
    fi
}

export generateJNIHeaders

generateNativeBuild() {
  nativeSrcFolder="$1"
  nativeOutputFolder="$2"
  set -x
  pushd $(pwd)
  cd "${nativeSrcFolder}"
  if [[ -d "${nativeOutputFolder}" ]]; then
      echo "Cleaning folder [${nativeOutputFolder}]..."
      rm -rf "${nativeOutputFolder}"
  fi
  cmake -B"${nativeOutputFolder}" .
  buildResult=$?
  popd

  set +x
  if [[ $buildResult -ne 0 ]]; then
    echo "Error when call generate native build folder ${buildResult}"
    exit ${buildResult}
  fi
}

export generateNativeBuild

compileNative() {
  generatedFolder="$1"
  set -x
  pushd $(pwd)
  cd "${generatedFolder}"
  cmake --build .
  buildResult=$?
  popd

  set +x
  if [[ $buildResult -ne 0 ]]; then
    echo "Error when call compile native ${buildResult}"
    exit ${buildResult}
  fi
}
export compileNative

export BUILD_FOLDER="$1"
export COMMAND="$2"
export lombokVersion="$3"
export jnaVersion="$4"
export slf4jVersion="$5"
export headerOutputFolder="build/native"
export lombokGradleCacheFolder="${HOME}/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/${lombokVersion}"
export jnaGradleCacheFolder="${HOME}/.gradle/caches/modules-2/files-2.1/net.java.dev.jna/jna/${jnaVersion}"
export logGradleCacheFolder="${HOME}/.gradle/caches/modules-2/files-2.1/org.slf4j/slf4j-api/${slf4jVersion}"

generateJNIHeaders() {
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
    echo "Log Version [${slf4jVersion}], Cache folder [${logGradleCacheFolder}]"
    export logJarFile=$(find "${logGradleCacheFolder}" | grep "${slf4jVersion}.jar")
    echo "Log Jar File [${logJarFile}]"

    set -x
    javac -h "${headerOutputFolder}" "${listJavaFile[@]}" -d "${BUILD_FOLDER}/tmp_javac/" -cp "${lombokJarFile}:${jnaJarFile}:${logJarFile}" --processor-path "${lombokJarFile}"
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

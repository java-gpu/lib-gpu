export COMMAND="$1"
export OS_TYPE=$(uname)

executeCommandInDir() {
  cmdToExc="$1"
  directory="$2"
  pushd $(pwd)
  cd "${directory}"
  ls -ail
  eval ${cmdToExc}
  popd
}
export executeCommandInDir

echo "Running command ${COMMAND}....."
if [[ "${COMMAND}" = "executeCommandInDir" ]]; then
  export exec="$2"
  export directory="$3"
  executeCommandInDir "${exec}" "${directory}"
elif [[ "${COMMAND}" = "buildBgfx" ]]; then
  if [[ "$OS_TYPE" == "Darwin" ]]; then
      echo "This is macOS"
      # executeCommandInDir "make osx-arm64-release" "../external/bgfx"
      executeCommandInDir "make osx-arm64-debug" "../external/bgfx"
  elif [[ "$OS_TYPE" == "Linux" ]]; then
      echo "This is Linux"
      # executeCommandInDir "make linux-gcc-release64" "../external/bgfx"
      executeCommandInDir "make linux-gcc-debug64" "../external/bgfx"
  else
      echo "Unknown OS: $OS_TYPE"
  fi
elif [[ "${COMMAND}" = "cleanBgfx" ]]; then
  executeCommandInDir "make clean" "../external/bgfx"
fi

export COMMAND="$1"

export OS_TYPE=$(uname)

export executeCommandInDir() {
  cmdToExc="$1"
  directory="$2"
  pushd $(pwd)
  cd "${directory}"
  ls -ail
  eval ${cmdToExc}
  popd
}

echo "Running command ${COMMAND}....."
if [[ "${COMMAND}" = "buildGenie" ]]; then
  executeCommandInDir "make" "../external/genie"
elif [[ "${COMMAND}" = "cleanGenie" ]]; then
  executeCommandInDir "make clean" "../external/genie"
elif [[ "${COMMAND}" = "buildBgfx" ]]; then
  if [[ "$OS_TYPE" == "Darwin" ]]; then
      echo "This is macOS"
      executeCommandInDir "make osx-arm64-release" "../external/bgfx"
      # executeCommandInDir "make osx-arm64-debug" "../external/bgfx"
  elif [[ "$OS_TYPE" == "Linux" ]]; then
      echo "This is Linux"
      executeCommandInDir "make linux-gcc-release64" "../external/bgfx"
      # executeCommandInDir "make linux-clang-release64" "../external/bgfx"
  else
      echo "Unknown OS: $OS_TYPE"
  fi
elif [[ "${COMMAND}" = "cleanBgfx" ]]; then
  executeCommandInDir "make clean" "../external/bgfx"
fi

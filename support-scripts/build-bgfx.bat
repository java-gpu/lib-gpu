@echo off
set COMMAND=%1

echo Executing command %COMMAND%
if "%COMMAND%" == "buildGenie" (
  call :executeCommandInDir "make" "..\external\genie"
  goto :eof
) else if "%COMMAND%" == "cleanGenie" (
  call :executeCommandInDir "make clean" "..\external\genie"
  goto :eof
) else if "%COMMAND%" == "buildBgfx" (
  call :executeCommandInDir "make vs2022-release64" "..\external\bgfx"
  goto :eof
) else if "%COMMAND%" == "cleanBgfx" (
  call :executeCommandInDir "make clean" "..\external\bgfx"
  goto :eof
)

goto :eof

:executeCommandInDir
  set commandToExec=%1
  set directory=%2

  pushd %cd%
  cd %directory%
  echo Now in: %cd%
  call %commandToExec%
  popd
  echo Back to: %cd%
  goto :eof

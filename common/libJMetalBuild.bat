echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set javaPackagePath=tech\gpu\lib\common
set srcFolder=src\main\java\%javaPackagePath%
set headerOutputFolder=build\native

setlocal enabledelayedexpansion

:: Simulate an array
set listJavaFile=%srcFolder%\GpuInfo.java
set "listJavaFile=%listJavaFile% %srcFolder%\util\CommonNativeLoader.java"

echo "Executing command %COMMAND%"
if "%COMMAND%" == "generateJNIHeaders" (
  call :generateJNIHeaders
  goto :eof
) else (
  echo "skip"
  goto :eof
)

:generateJNIHeaders
javac -h "%headerOutputFolder%" %listJavaFile% -d "%BUILD_FOLDER%\classes\java\main\%javaPackagePath%"
goto :eof

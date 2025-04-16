echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set lombokVersion=%3
set javaPackagePath=tech\gpu\java\lib\metal
set srcFolder=src\main\java\%javaPackagePath%
set headerOutputFolder=build\native

setlocal enabledelayedexpansion

:: Simulate an array
set listJavaFile=%srcFolder%\MetalRenderer.java
set "listJavaFile=%listJavaFile% %srcFolder%\util\MetalNativeLoader.java"

echo "Executing command %COMMAND%"
if "%COMMAND%" == "generateJNIHeaders" (
  call ..\support-scripts\generateJniHeader.bat %BUILD_FOLDER% %COMMAND% %lombokVersion%
  goto :eof
) else (
  echo "skip"
  goto :eof
)

echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set lombokVersion=%3
set javaPackagePath=tech\gpu\lib\vulkan
set srcFolder=src\main\java\%javaPackagePath%
set headerOutputFolder=build\native

setlocal enabledelayedexpansion

:: Simulate an array
set "listJavaFile=%srcFolder%\util\VulkanNativeLoader.java"

echo "Executing command %COMMAND%"
if "%COMMAND%" == "generateJNIHeaders" (
  call ..\support-scripts\generateJniHeader.bat %BUILD_FOLDER% %COMMAND% %lombokVersion% "src\main\java\tech\gpu\lib\vulkan\jni"
  if errorlevel 1 (
    echo generateJniHeader.bat failed with errorlevel %errorlevel%
    exit /b %errorlevel%
  )
  goto :eof
) else (
  echo "skip"
  goto :eof
)

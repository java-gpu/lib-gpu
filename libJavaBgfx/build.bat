@echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set lombokVersion=%3
set jnaVersion=%4
set slf4jVersion=%5

set javaPackagePath=tech\lib\bgfx
set srcFolder=src\main\java\%javaPackagePath%
set headerOutputFolder=build\native

setlocal enabledelayedexpansion

:: Simulate an array
set "listJavaFile=%srcFolder%\util\PlatformInfo.java"
set "listJavaFile=%listJavaFile% src\main\java\com\sun\jna\MyJFramePointer.java"
set "listJavaFile=%listJavaFile% src\main\java\tech\lib\ui\event\AppEvent.java"
set "listJavaFile=%listJavaFile% src\main\java\tech\lib\ui\enu\AppEventType.java"
set "listJavaFile=%listJavaFile% src\main\java\tech\lib\ui\enu\SuspendState.java"
set "listJavaFile=%listJavaFile% src\main\java\tech\lib\bgfx\enu\BgfxResetFlag.java"
set "listJavaFile=%listJavaFile% src\main\java\tech\lib\ui\enu\GamepadAxis.java"
set "listJavaFile=%listJavaFile% src\main\java\tech\lib\ui\event\SuspendEvent.java"

echo "Executing command %COMMAND%"
if "%COMMAND%" == "generateJNIHeaders" (
  call ..\support-scripts\generateJniHeader.bat %BUILD_FOLDER% %COMMAND% %lombokVersion% %jnaVersion% %slf4jVersion% "%srcFolder%\jni" "src\main\java\tech\lib\ui\jni"

  if errorlevel 1 (
    echo generateJniHeader.bat failed with errorlevel %errorlevel%
    exit /b %errorlevel%
  )
  goto :eof
) else if "%COMMAND%" == "generateNativeBuild" (
  echo Generating for common JNI...
  call ..\support-scripts\generateJniHeader.bat "%BUILD_FOLDER%" %COMMAND% %lombokVersion% %jnaVersion% %slf4jVersion% ..\native %BUILD_FOLDER%\native\build
  if errorlevel 1 (
    echo generateJniHeader.bat failed with command [%COMMAND%] [..\native] [%BUILD_FOLDER%\native] == errorlevel %errorlevel%
    exit /b %errorlevel%
  )

) else if "%COMMAND%" == "compileNative" (
  echo Building common JNI...
  call ..\support-scripts\generateJniHeader.bat "%BUILD_FOLDER%" %COMMAND% %lombokVersion% %jnaVersion% %slf4jVersion% "%BUILD_FOLDER%\native\build"
  if errorlevel 1 (
    echo generateJniHeader.bat failed with errorlevel %errorlevel%
    exit /b %errorlevel%
  )

) else (
  echo "Skip"
  goto :eof
)

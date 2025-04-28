@echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set lombokVersion=%3
set javaPackagePath=tech\gpu\lib\directx
set srcFolder=src\main\java\%javaPackagePath%
set headerOutputFolder=build\native
set lombokGradleCacheFolder=%USERPROFILE%\.gradle\caches\modules-2\files-2.1\org.projectlombok\lombok\%lombokVersion%"

setlocal enabledelayedexpansion

:: Simulate an array
set "listJavaFile=%srcFolder%\util\DirectxNativeLoader.java"

if "%COMMAND%" == "generateJNIHeaders" (
  call ..\support-scripts\generateJniHeader.bat "%BUILD_FOLDER%" %COMMAND% %lombokVersion% "src\main\java\tech\gpu\lib\directx\jni"
  if errorlevel 1 (
    echo generateJniHeader.bat failed with errorlevel %errorlevel%
    exit /b %errorlevel%
  )
) else if "%COMMAND%" == "generateNativeBuild" (
  echo Generating for common JNI...
  call ..\support-scripts\generateJniHeader.bat "%BUILD_FOLDER%" %COMMAND% %lombokVersion% src\main\native\common %BUILD_FOLDER%\native\common
  if errorlevel 1 (
    echo generateJniHeader.bat failed with command [%COMMAND%] [src\main\native\common] [%BUILD_FOLDER%\native\common] == errorlevel %errorlevel%
    exit /b %errorlevel%
  )
  echo Generating for directx JNI...
  call ..\support-scripts\generateJniHeader.bat "%BUILD_FOLDER%" %COMMAND% %lombokVersion% src\main\native\directx %BUILD_FOLDER%\native\directx
  if errorlevel 1 (
    echo generateJniHeader.bat failed with command [%COMMAND%] [src\main\native\directx] [%BUILD_FOLDER%\native\directx] == errorlevel %errorlevel%
    exit /b %errorlevel%
  )
) else if "%COMMAND%" == "compileNative" (
  echo Building common JNI...
  call ..\support-scripts\generateJniHeader.bat "%BUILD_FOLDER%" %COMMAND% %lombokVersion% "%BUILD_FOLDER%/native/common"
  if errorlevel 1 (
    echo generateJniHeader.bat failed with errorlevel %errorlevel%
    exit /b %errorlevel%
  )
  echo Building directx JNI...
  call ..\support-scripts\generateJniHeader.bat "%BUILD_FOLDER%" %COMMAND% %lombokVersion% "%BUILD_FOLDER%/native/directx"
  if errorlevel 1 (
    echo generateJniHeader.bat failed with errorlevel %errorlevel%
    exit /b %errorlevel%
  )
) else (
  echo "Skip"
  goto :eof
)

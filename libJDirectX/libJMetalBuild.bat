echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set lombokVersion=%3
set javaPackagePath=tech\gpu\java\lib\directx
set srcFolder=src\main\java\%javaPackagePath%
set headerOutputFolder=build\native
set lombokGradleCacheFolder=%USERPROFILE%\.gradle\caches\modules-2\files-2.1\org.projectlombok\lombok\%lombokVersion%"

setlocal enabledelayedexpansion

:: Simulate an array
set listJavaFile=%srcFolder%\MetalRenderer.java
set "listJavaFile=%listJavaFile% %srcFolder%\util\MetalNativeLoader.java"

echo "Executing command %COMMAND%"
if "%COMMAND%" == "generateJNIHeaders" (
  call :findLombok
  goto :eof
) else (
  echo "skip"
  goto :eof
)

:findLombok
for /R "%lombokGradleCacheFolder%" %%f in (*%lombokVersion%.jar) do (
    set "lombokJarFile=%%f"
    goto :foundLombok
)

:foundLombok
javac -h "%headerOutputFolder%" %listJavaFile% -d "%BUILD_FOLDER%\classes\java\main\%javaPackagePath%" -cp %lombokJarFile%
goto :eof

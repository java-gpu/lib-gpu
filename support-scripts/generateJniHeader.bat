@echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set lombokVersion=%3
set headerOutputFolder=build\native

IF DEFINED GRADLE_USER_HOME (
    echo The GRADLE_USER_HOME variable is set to: %GRADLE_USER_HOME%
) ELSE (
    echo The GRADLE_USER_HOME variable is NOT set.
    set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"
)
set lombokGradleCacheFolder=%GRADLE_USER_HOME%\caches\modules-2\files-2.1\org.projectlombok\lombok\%lombokVersion%

setlocal enabledelayedexpansion

echo "Executing command %COMMAND%"
if "%COMMAND%" == "generateJNIHeaders" (
  call :findLombok
  goto :eof
) else (
  echo "skip"
  goto :eof
)

:findLombok
REM Search for the first matching JAR file
for /R "%lombokGradleCacheFolder%" %%f in (*) do (
    echo %%~nxf | findstr /C:"%lombokVersion%.jar" >nul
    if not errorlevel 1 (
        set "lombokJarFile=%%f"
        goto :foundLombok
    )
)

:foundLombok
echo Lombok Jar %lombokJarFile%
echo on
javac -h "%headerOutputFolder%" %listJavaFile% -d "%BUILD_FOLDER%\classes\java\main\%javaPackagePath%" -cp %lombokJarFile%
goto :eof

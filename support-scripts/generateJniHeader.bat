@echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set lombokVersion=%3
set jnaVersion=%4
set slf4jVersion=%5
set headerOutputFolder=build\native

REM Check processor architecture
echo Processor architecture: %PROCESSOR_ARCHITECTURE%
set "vsScript="

echo Running on %PROCESSOR_ARCHITECTURE% architecture
if "%PROCESSOR_ARCHITECTURE%" == "AMD64" (
   set "vsScript=C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat"
) else if "%PROCESSOR_ARCHITECTURE%" == "ARM64" (
    set "vsScript=C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvarsarm64.bat"
) else (
    echo Unknown architecture: %PROCESSOR_ARCHITECTURE%
    exit /b
)

setlocal enabledelayedexpansion

echo Executing command %COMMAND%
if "%COMMAND%" == "generateJNIHeaders" (
  call :findLombok %6 %7
  goto :eof
) else if "%COMMAND%" == "generateNativeBuild" (
  call :generateNativeBuild "%6" "%7"
  goto :eof
) else if "%COMMAND%" == "compileNative" (
  call :compileNative "%6"
  goto :eof
)
goto :eof

:findLombok
set sourceJniFolder=%1
set sourceJniFolder2=%2
IF DEFINED GRADLE_USER_HOME (
    echo The GRADLE_USER_HOME variable is set to: %GRADLE_USER_HOME%
) ELSE (
    echo The GRADLE_USER_HOME variable is NOT set.
    set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"
)
set lombokGradleCacheFolder=%GRADLE_USER_HOME%\caches\modules-2\files-2.1\org.projectlombok\lombok\%lombokVersion%
REM Search for the first matching JAR file
for /R "%lombokGradleCacheFolder%" %%f in (*) do (
    echo %%~nxf | findstr /C:"%lombokVersion%.jar" >nul
    if not errorlevel 1 (
        set "lombokJarFile=%%f"
        goto :findLogApi %sourceJniFolder% %sourceJniFolder2%
    )
)
goto :eof

:findLogApi
echo Lombok Jar %lombokJarFile%
set sourceJniFolder=%1
set sourceJniFolder2=%2
set logGradleCacheFolder=%GRADLE_USER_HOME%\caches\modules-2\files-2.1\org.slf4j\slf4j-api\%slf4jVersion%
REM Search for the first matching JAR file
for /R "%logGradleCacheFolder%" %%f in (*) do (
    echo %%~nxf | findstr /C:"%slf4jVersion%.jar" >nul
    if not errorlevel 1 (
        set "logJarFile=%%f"
        goto :findJna %sourceJniFolder% %sourceJniFolder2%
    )
)
goto :eof

:findJna
echo Log Jar %logJarFile%
set sourceJniFolder=%1
set sourceJniFolder2=%2
set lombokGradleCacheFolder=%GRADLE_USER_HOME%\caches\modules-2\files-2.1\net.java.dev.jna\jna\%jnaVersion%
REM Search for the first matching JAR file
for /R "%lombokGradleCacheFolder%" %%f in (*) do (
    echo %%~nxf | findstr /C:"%jnaVersion%.jar" >nul
    if not errorlevel 1 (
        set "jnaJarFile=%%f"
        goto :foundJna %sourceJniFolder% %sourceJniFolder2%
    )
)
goto :eof

:foundJna
echo JNA Jar %jnaJarFile%

echo off
rem Loop through all input arguments (directories)
set sourceJniFolder=%1
if exist %sourceJniFolder% (
    for /R %sourceJniFolder% %%f in (*.java) do (
        set listJavaFile=!listJavaFile! "%%f"
    )
)

set sourceJniFolder2=%2
if exist %sourceJniFolder2% (
    for /R %sourceJniFolder2% %%f in (*.java) do (
        set listJavaFile=!listJavaFile! "%%f"
    )
)

echo on
javac -h "%headerOutputFolder%" %listJavaFile% -d %BUILD_FOLDER%\tmp_javac\ -cp "%lombokJarFile%;%jnaJarFile%;%logJarFile%" --processor-path "%lombokJarFile%"
echo off
if exist %BUILD_FOLDER%\tmp_javac\ (
    for /R %BUILD_FOLDER%\tmp_javac %%F in (*) do (
        echo Deleting: %%F
        del /f /q "%%F"
    )
    rmdir /s /q %BUILD_FOLDER%\tmp_javac\
)
goto :eof

:generateNativeBuild
  set nativeSrcFolder=%1
  set nativeOutputFolder=%2
  echo Src folder %nativeSrcFolder%
  echo Output folder %nativeOutputFolder%

  if exist "%nativeOutputFolder%" (
      echo Cleaning native output folder: %nativeOutputFolder%
      rmdir /s /q "%nativeOutputFolder%"
  )

  echo Setting up build environment...
  call "%vsScript%"
  if errorlevel 1 (
     echo [%vsScript%] failed with errorlevel %errorlevel%
     exit /b %errorlevel%
  )

  REM Now generating data
  echo JAVA_HOME=%JAVA_HOME%
  echo Starting generate build folder...
  mkdir "%nativeOutputFolder%"
  cmake -B%nativeOutputFolder% %nativeSrcFolder%
  goto :eof

:compileNative
  set generatedFolder=%1

  echo Setting up build environment...
  call "%vsScript%"
  if errorlevel 1 (
    echo [%vsScript%] failed with errorlevel %errorlevel%
    exit /b %errorlevel%
  )

  cd %generatedFolder%
  cmake --build . --config Debug
  goto :eof

@echo off
set COMMAND=%1
set commandToExec=%2
set directory=%3

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

echo Setting up build environment...
call "%vsScript%"
if errorlevel 1 (
    echo [%vsScript%] failed with errorlevel %errorlevel%
    exit /b %errorlevel%
)

echo Executing command %COMMAND%
if "%COMMAND%" == "executeCommandInDir" (
  call :executeCommandInDir %commandToExec% %directory%
  goto :eof
)else if "%COMMAND%" == "buildBgfx" (
  setlocal enabledelayedexpansion
      pushd %cd%
      cd ..\external\bx

      set "BX_DIR=!cd!"
      echo Set BX_DIR as [!BX_DIR!]
      set "GENIE=!BX_DIR!\tools\bin\windows\genie"
      echo Set GENIE as [!GENIE!]

      popd

      pushd %cd%
      cd "..\external\bgfx"
      echo Genie: !GENIE!
      call !GENIE! --with-tools --with-combined-examples --with-shared-lib vs2022
      call devenv .build/projects/vs2022/bgfx.sln /Build "Debug|x64"
      call devenv .build/projects/vs2022/bgfx.sln /Build "Release|x64"
      popd
  endlocal

  goto :eof
) else if "%COMMAND%" == "cleanBgfx" (
    pushd %cd%
    cd ..\external\bgfx

    rd /s /q ".build"
    mkdir ".build"
    popd
    echo Clean build folder completed!
    goto :eof
)

goto :eof

:executeCommandInDir
  set commandToExec=%~1
  set directory=%2
  echo Command %1 in dir %2
  echo Moving to directory %directory%

  pushd %cd%
  cd %directory%
  echo Now in: %cd%
  echo Calling command [%commandToExec%]
  call %commandToExec%
  popd
  echo Back to: %cd%
  goto :eof

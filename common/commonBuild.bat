@echo off

set BUILD_FOLDER=%1
set COMMAND=%2
set lombokVersion=%3
set javaPackagePath=tech\gpu\lib
set srcFolder=src\main\java\%javaPackagePath%
set headerOutputFolder=build\native

setlocal enabledelayedexpansion

:: Simulate an array
set listJavaFile=%srcFolder%\GpuInfo.java
set listJavaFile=%srcFolder%\GpuManager.java
set "listJavaFile=%listJavaFile% %srcFolder%\util\CommonNativeLoader.java"
set "listJavaFile=%listJavaFile% %srcFolder%\util\Disposable.java"
set "listJavaFile=%listJavaFile% %srcFolder%\graphics\Texture.java"
set "listJavaFile=%listJavaFile% %srcFolder%\graphics\PixelFormat.java"
set "listJavaFile=%listJavaFile% %srcFolder%\graphics\PixelFormatConverter.java"
set "listJavaFile=%listJavaFile% %srcFolder%\ex\PixelFormatNotSupportedException.java"
set "listJavaFile=%listJavaFile% %srcFolder%\ex\GpuRuntimeException.java"
set "listJavaFile=%listJavaFile% %srcFolder%\Application.java"
set "listJavaFile=%listJavaFile% %srcFolder%\ApplicationListener.java"
set "listJavaFile=%listJavaFile% %srcFolder%\ApplicationLogger.java"
set "listJavaFile=%listJavaFile% %srcFolder%\Audio.java"
set "listJavaFile=%listJavaFile% %srcFolder%\Game.java"
set "listJavaFile=%listJavaFile% %srcFolder%\Graphics.java"
set "listJavaFile=%listJavaFile% %srcFolder%\Input.java"
set "listJavaFile=%listJavaFile% %srcFolder%\InputProcessor.java"
set "listJavaFile=%listJavaFile% %srcFolder%\InputEventQueue.java"
set "listJavaFile=%listJavaFile% %srcFolder%\LifecycleListener.java"
set "listJavaFile=%listJavaFile% %srcFolder%\Preferences.java"
set "listJavaFile=%listJavaFile% %srcFolder%\Screen.java"
set "listJavaFile=%listJavaFile% %srcFolder%\audio\AudioDevice.java"
set "listJavaFile=%listJavaFile% %srcFolder%\audio\AudioRecorder.java"
set "listJavaFile=%listJavaFile% %srcFolder%\audio\Music.java"
set "listJavaFile=%listJavaFile% %srcFolder%\audio\Sound.java"
set "listJavaFile=%listJavaFile% %srcFolder%\input\GestureDetector.java"
set "listJavaFile=%listJavaFile% %srcFolder%\input\NativeInputConfiguration.java"
set "listJavaFile=%listJavaFile% %srcFolder%\input\TextInputWrapper.java"

echo "Executing command %COMMAND%"
if "%COMMAND%" == "generateJNIHeaders" (
  call ..\support-scripts\generateJniHeader.bat %BUILD_FOLDER% %COMMAND% %lombokVersion%
  goto :eof
) else (
  echo "skip"
  goto :eof
)

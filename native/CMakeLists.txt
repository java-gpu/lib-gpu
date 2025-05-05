cmake_minimum_required(VERSION 3.15)
project(bgfx_java_wrapper)

# Set output to shared lib
set(CMAKE_POSITION_INDEPENDENT_CODE ON)
set(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)

# Tự động phát hiện platform
if (APPLE)
    set(PLATFORM "osx")
    find_library(COCOA_LIBRARY Cocoa)
elseif (WIN32)
    set(PLATFORM "windows")
elseif (UNIX)
    set(PLATFORM "linux")
endif()

# Tự động phát hiện kiến trúc
if (CMAKE_SYSTEM_PROCESSOR MATCHES "arm64" OR CMAKE_OSX_ARCHITECTURES STREQUAL "arm64")
    set(ARCH "arm64")
else()
    set(ARCH "x64")
endif()

if(PLATFORM STREQUAL "osx")
    set(BGFX_BIN_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../external/bgfx/.build/${PLATFORM}-${ARCH}/bin)
elseif(PLATFORM STREQUAL "windows")
    set(BGFX_BIN_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../external/bgfx/.build/win64_vs2022/bin)
elseif (PLATFORM STREQUAL "linux")
    set(BGFX_BIN_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../external/bgfx/.build/linux64_gcc/bin)
endif()

# Add bgfx as submodule
set(BGFX_DIR ${CMAKE_SOURCE_DIR}/../external/bgfx)
set(BX_DIR ${CMAKE_SOURCE_DIR}/../external/bx)
set(BIMG_DIR ${CMAKE_SOURCE_DIR}/../external/bimg)

# JNI include paths
find_package(Java REQUIRED)
find_package(JNI REQUIRED)

include_directories(
    ${JNI_INCLUDE_DIRS}
    ${BGFX_DIR}/include
    ${BX_DIR}/include
    ${BIMG_DIR}/include
    ${CMAKE_CURRENT_SOURCE_DIR}
    ../libJavaBgfx/build/native
)

message(STATUS "Using bgfx binary dir: ${BGFX_BIN_DIR}")
link_directories(${BGFX_BIN_DIR})

set(SOURCES
    bgfx_jni.cpp
    jni_utils.cpp
    GpuManager.cpp
)

if(APPLE)
   list(APPEND SOURCES
       macos/macos_view.mm
       macos/GpuManager.mm
   )
endif()

if (PLATFORM STREQUAL "linux")
    list(APPEND SOURCES
         linux/linux_view.cpp
         linux/gpu_manager.cpp
    )
endif()

if (PLATFORM STREQUAL "windows")
    list(APPEND SOURCES
         windows/gpu_manager.cpp
    )
endif()

add_library(bgfx_jni SHARED ${SOURCES})

target_link_directories(bgfx_jni
    PRIVATE
    ${BGFX_BIN_DIR}
    ${JAVA_AWT_LIBRARY_DIRECTORIES}
)

if(PLATFORM STREQUAL "windows")
    target_compile_options(bgfx_jni PRIVATE /MTd)
    # Remove default /MDd
    string(REGEX REPLACE "/MDd" "" CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG}")
    string(REGEX REPLACE "/MDd" "" CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG}")

    target_link_libraries(bgfx_jni
        PRIVATE
        jawt
        ${BGFX_BIN_DIR}/bgfx-shared-libDebug.lib
        ${BGFX_BIN_DIR}/bxDebug.lib
        ${BGFX_BIN_DIR}/bimgDebug.lib
        ${BGFX_BIN_DIR}/bgfxDebug.lib
        # bgfx-shared-libRelease
        # bxRelease
        #bxDebug
        #bimgRelease
        #bimgDebug
    )
else()
    target_link_libraries(bgfx_jni
        PRIVATE
        jawt
        bgfx-shared-libDebug
        # bgfx-shared-libRelease
        # bxRelease
        bxDebug
        #bimgRelease
        bimgDebug
    )
endif()

target_include_directories(bgfx_jni PRIVATE
    ${BGFX_DIR}/include
    ${BX_DIR}/include
    ${BIMG_DIR}/include
)

if(PLATFORM STREQUAL "osx")
    set_target_properties(bgfx_jni PROPERTIES
        OUTPUT_NAME "bgfx_jni"
        # Set library output directory (relative to CMake's invocation path)
        LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/${PLATFORM}-${ARCH}/bin

        INSTALL_RPATH "@loader_path"
        # INSTALL_RPATH "@loader_path/../../external/bgfx/.build/${PLATFORM}-${ARCH}/bin"
    )
elseif(PLATFORM STREQUAL "windows")
    set_target_properties(bgfx_jni PROPERTIES
        OUTPUT_NAME "bgfx_jni"
        # Set library output directory (relative to CMake's invocation path)
        ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/win64_vs2022/bin
        RUNTIME_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/win64_vs2022/bin
        ARCHIVE_OUTPUT_DIRECTORY_DEBUG ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/win64_vs2022/bin
        RUNTIME_OUTPUT_DIRECTORY_DEBUG ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/win64_vs2022/bin
        ARCHIVE_OUTPUT_DIRECTORY_RELEASE ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/win64_vs2022/bin
        RUNTIME_OUTPUT_DIRECTORY_RELEASE ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/win64_vs2022/bin

        INSTALL_RPATH "@loader_path"
    )
elseif (PLATFORM STREQUAL "linux")
    set_target_properties(bgfx_jni PROPERTIES
        OUTPUT_NAME "bgfx_jni"
        # Set library output directory (relative to CMake's invocation path)
        LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/linux64_gcc/bin

        INSTALL_RPATH "@loader_path"
    )
endif()

if(APPLE)
   target_link_libraries(bgfx_jni PRIVATE
       ${COCOA_LIBRARY}
       "-framework Foundation"
       "-framework Metal"
       "-framework AppKit"
   )
endif()
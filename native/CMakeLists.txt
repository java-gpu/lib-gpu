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
set(BGFX_BIN_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../external/bgfx/.build/${PLATFORM}-${ARCH}/bin)
# set(BGFX_BIN_DIR ${CMAKE_CURRENT_SOURCE_DIR}/build)

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
#link_directories(${BGFX_BIN_DIR})

set(SOURCES
    bgfx_jni.cpp
    jni_utils.cpp
)

if(APPLE)
   list(APPEND SOURCES macos/macos_view.mm)
endif()

add_library(bgfx_jni SHARED ${SOURCES})

target_link_directories(bgfx_jni
    PRIVATE
    ${BGFX_BIN_DIR}
    ${JAVA_AWT_LIBRARY_DIRECTORIES}
)
target_link_libraries(bgfx_jni
    PRIVATE
    bgfx-shared-libDebug
    # bgfx-shared-libRelease
    # bxRelease
    bxDebug
    #bimgRelease
    bimgDebug
    jawt
)

set_target_properties(bgfx_jni PROPERTIES
    OUTPUT_NAME "bgfx_jni"
    # Set library output directory (relative to CMake's invocation path)
    LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../external/bgfx/.build/${PLATFORM}-${ARCH}/bin
    # LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/build/jni-output
    INSTALL_RPATH "@loader_path"
    # INSTALL_RPATH "@loader_path/../../external/bgfx/.build/${PLATFORM}-${ARCH}/bin"
)

if(APPLE)
   target_link_libraries(bgfx_jni PRIVATE ${COCOA_LIBRARY})
endif()
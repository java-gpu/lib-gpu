cmake_minimum_required(VERSION 3.10)
project(GpuCommon)

set(CMAKE_CXX_STANDARD 17)
find_package(Java REQUIRED)
find_package (JNI REQUIRED)
include (UseJava)

if(CMAKE_SYSTEM_PROCESSOR MATCHES "ARM64")
    message(STATUS "Targeting ARM64")
    set(DIRECTXTK_LIB_PATH "C:/DirectXTK/Bin/Desktop_2022/ARM64")
elseif(CMAKE_SYSTEM_PROCESSOR MATCHES "AMD64")
    message(STATUS "Targeting x64")
    set(DIRECTXTK_LIB_PATH "C:/DirectXTK/Bin/Desktop_2022/x64")
endif()

# Set library output directory (relative to CMake's invocation path)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/../../../build/libs/native")

include_directories(
    ${JNI_INCLUDE_DIRS}
    ../../../../../common/build/native/
    C:/DirectXTK/inc
)

link_directories(
)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")

add_library(GpuCommon SHARED
    GpuManager.cpp
    Texture.cpp
    ../NativeJniUtil.cpp
)

link_libraries(${JNI_LIBRARIES})

# target_link_directories(GpuCommon PRIVATE ${DIRECTXTK_LIB_PATH})

target_link_libraries(GpuCommon
    d3d11
    dxgi
    windowscodecs
    "${JNI_LIBRARIES}"
    optimized "${DIRECTXTK_LIB_PATH}/Release/DirectXTK.lib"
    debug     "${DIRECTXTK_LIB_PATH}/Debug/DirectXTK.lib"
)

set_target_properties(GpuCommon PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY_DEBUG "${CMAKE_BINARY_DIR}/../../../build/libs/native/debug"
    RUNTIME_OUTPUT_DIRECTORY_RELEASE "${CMAKE_BINARY_DIR}/../../../build/libs/native/release"
)

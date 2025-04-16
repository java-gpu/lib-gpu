cmake_minimum_required(VERSION 3.15)
project(GpuCommon)

set(CMAKE_CXX_STANDARD 17)

# Use JAVA_HOME from environment
if(DEFINED ENV{JAVA_HOME})
    set(JAVA_HOME $ENV{JAVA_HOME})
else()
    message(FATAL_ERROR "JAVA_HOME environment variable is not set.")
endif()

message(STATUS "JAVA_HOME is: ${JAVA_HOME}")

# Set library output directory (relative to CMake's invocation path)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/../../../build/libs/native")

include_directories(
    ${JAVA_HOME}/include
    ${JAVA_HOME}/include/darwin
    ${CMAKE_BINARY_DIR}/../../../../common/build/native/
    include/
)

add_library(GpuCommon SHARED
    jni/GpuInfo.cpp
    jni/Texture.cpp
    object_c_pp/GpuInfo.m
    object_c_pp/Texture.m
)

target_link_libraries(GpuCommon
    "-framework Foundation"
    "-framework Metal"
    "-framework AppKit"
)
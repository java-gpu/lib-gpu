cmake_minimum_required(VERSION 3.15)
project(BgfxShaders)

set(BGFX_DIR "${CMAKE_CURRENT_SOURCE_DIR}/../../../external/bgfx")
set(SHADER_DIR "${CMAKE_CURRENT_SOURCE_DIR}")
set(BUILD_DIR "${BGFX_DIR}/.build/shader")

# arch
if (CMAKE_SYSTEM_PROCESSOR MATCHES "arm64" OR CMAKE_OSX_ARCHITECTURES STREQUAL "arm64")
    set(ARCH "arm64")
else()
    set(ARCH "x64")
endif()

# PLATFORM
if (APPLE)
    set(PLATFORM "darwin")
    set(BGFX_BIN_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../external/bgfx/.build/${PLATFORM}-${ARCH}/bin)
elseif (WIN32)
    set(PLATFORM "windows")
    set(BGFX_BIN_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../external/bgfx/.build/win64_vs2022/bin)
elseif (UNIX)
    set(PLATFORM "linux")
    set(BGFX_BIN_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../external/bgfx/.build/linux64_gcc/bin)
endif()

# Define where your compiled shaders will go
file(MAKE_DIRECTORY ${BUILD_DIR})

# Find all shader source files (for example, *.sc or *.glsl)
file(GLOB SHADER_SOURCES "${CMAKE_CURRENT_SOURCE_DIR}/*.sc")

# A function or macro to compile each shader via bgfx shader compiler
foreach(SHADER ${SHADER_SOURCES})
    get_filename_component(SHADER_NAME ${SHADER} NAME)
    set(COMPILED_SHADER "${BUILD_DIR}/${SHADER_NAME}.bin")

    add_custom_command(
        COMMENT "Compiling shader ${SHADER_NAME}"
        OUTPUT ${COMPILED_SHADER}
        COMMAND ${BGFX_BIN_DIR}/tools/shadercDebug -f ${SHADER} -o ${COMPILED_SHADER} --platform ${PLATFORM} --type vertex
        DEPENDS ${SHADER}
    )

    list(APPEND COMPILED_SHADERS ${COMPILED_SHADER})
endforeach()

add_custom_target(shaders ALL DEPENDS ${COMPILED_SHADERS})

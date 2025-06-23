cmake_minimum_required(VERSION 3.15)
project(bgfx_java_wrapper)

# Set output to shared lib
set(CMAKE_POSITION_INDEPENDENT_CODE ON)
set(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# PLATFORM
if (APPLE)
    set(PLATFORM "osx")
    find_library(COCOA_LIBRARY Cocoa)
    find_library(IOKIT_LIBRARY IOKit)
    find_library(COREFOUNDATION_LIBRARY CoreFoundation)
elseif (WIN32)
    set(PLATFORM "windows")
    if(CMAKE_SYSTEM_PROCESSOR MATCHES "ARM64")
        message(STATUS "Targeting ARM64")
        set(DIRECTXTK_LIB_PATH "C:/DirectXTK/Bin/Desktop_2022/ARM64")
    elseif(CMAKE_SYSTEM_PROCESSOR MATCHES "AMD64")
        message(STATUS "Targeting x64")
        set(DIRECTXTK_LIB_PATH "C:/DirectXTK/Bin/Desktop_2022/x64")
    endif()
elseif (UNIX)
    set(PLATFORM "linux")
    # Find Vulkan
    find_package(Vulkan REQUIRED)
    find_package(PkgConfig REQUIRED)
    pkg_check_modules(DBUS REQUIRED dbus-1)
    pkg_check_modules(LIBEVDEV REQUIRED libevdev)
endif()

# arch
message(STATUS "CMAKE_SYSTEM_PROCESSOR: ${CMAKE_SYSTEM_PROCESSOR} and CMAKE_OSX_ARCHITECTURES: ${CMAKE_OSX_ARCHITECTURES}")
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
    ${CMAKE_CURRENT_SOURCE_DIR}/include
    ../libJavaBgfx/build/native
    # ../libJavaBgfx/build/generated/sources/headers/java/main
    if(PLATFORM STREQUAL "windows")
        C:/DirectXTK/inc
    endif()
    # if(PLATFORM STREQUAL "osx")
    #     /opt/homebrew/include/
    #     /usr/include
    # endif()
    if(PLATFORM STREQUAL "linux")
        ${DBUS_INCLUDE_DIRS}
        ${LIBEVDEV_INCLUDE_DIRS}
    endif()
)

message(STATUS "Using bgfx binary dir: ${BGFX_BIN_DIR}")
link_directories(${BGFX_BIN_DIR})

set(SOURCES
    bgfx_jni.cpp
    jni_utils.cpp
    GpuManager.cpp
    bx_timer.cpp
    event_manager_jni.cpp
    game_pad_event.cpp
    memory_handle_jni.cpp
    vertex_layout.cpp
)

if(APPLE)
   list(APPEND SOURCES
       macos/macos_view.mm
       macos/GpuManager.mm
       macos/event_manager.mm
       macos/event_manager.cpp
   )
endif()

if (PLATFORM STREQUAL "linux")
    list(APPEND SOURCES
         linux/linux_view.cpp
         linux/gpu_manager.cpp
         linux/event_manager.cpp
    )
endif()

if (PLATFORM STREQUAL "windows")
    list(APPEND SOURCES
         windows/gpu_manager.cpp
         windows/event_manager.cpp
    )
endif()

add_library(bgfx_jni SHARED ${SOURCES})

target_link_directories(bgfx_jni
    PRIVATE
    ${BGFX_BIN_DIR}
    ${JAVA_AWT_LIBRARY_DIRECTORIES}
)

if(PLATFORM STREQUAL "windows")
    target_compile_options(bgfx_jni PRIVATE /MTd
        "$<$<CXX_COMPILER_ID:MSVC>:/Zc:__cplusplus>"
        "$<$<CXX_COMPILER_ID:MSVC>:/Zc:preprocessor>"
    )
    # Remove default /MDd
    string(REGEX REPLACE "/MDd" "" CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG}")
    string(REGEX REPLACE "/MDd" "" CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG}")
    add_definitions(-DNOMINMAX)

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
        # optimized "${DIRECTXTK_LIB_PATH}/Release/DirectXTK.lib"
        # debug     "${DIRECTXTK_LIB_PATH}/Debug/DirectXTK.lib"
        dxgi.lib   # Links to the DXGI library, which contains CreateDXGIFactory1
        d3d11.lib  # Links to the D3D11 library, in case you need DirectX 11 support
    )
elseif(PLATFORM STREQUAL "linux")
    target_link_libraries(bgfx_jni
        PRIVATE
        jawt
        bgfx-shared-libDebug
        # bgfx-shared-libRelease
        # bxRelease
        bxDebug
        #bimgRelease
        bimgDebug
        ${Vulkan_LIBRARIES}
        ${DBUS_LIBRARIES}
        ${LIBEVDEV_LIBRARIES}
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
        ${IOKIT_LIBRARY}
        ${COREFOUNDATION_LIBRARY}
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

target_compile_definitions(bgfx_jni PRIVATE
    $<$<CONFIG:Debug>:BX_CONFIG_DEBUG=1>
    $<$<NOT:$<CONFIG:Debug>>:BX_CONFIG_DEBUG=0>
)

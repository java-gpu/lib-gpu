# Variables
BGFX_DIR=..\..\..\external\bgfx
BUILD_DIR=$(BGFX_DIR)\.build
SHADERS_DIR=.
SHADER_PATH=$(BUILD_DIR)\shaders\direct3d11
OS=windows

SHADERC=$(BUILD_DIR)\win64_vs2022\bin\shadercDebug.exe
ADDITIONAL_INCLUDES=

VS_FLAGS=--platform windows -p s_5_0 -O 3 -i $(SHADERS_DIR) $(ADDITIONAL_INCLUDES)
FS_FLAGS=--platform windows -p s_5_0 -O 3 -i $(SHADERS_DIR) $(ADDITIONAL_INCLUDES)
CS_FLAGS=--platform windows -p s_5_0 -O 1 -i $(SHADERS_DIR) $(ADDITIONAL_INCLUDES)


BUILD_OUTPUT_DIR=$(SHADER_PATH)
BUILD_INTERMEDIATE_DIR=$(SHADER_PATH)

# List shader source files manually
FS_SOURCES=fs_cubes.sc
VS_SOURCES=vs_cubes.sc
# CS_SOURCES=cs_basic.sc

!IFNDEF TARGET

all:
	@echo Usage: nmake TARGET=# [clean, all, rebuild]
	@echo.
	@echo   TARGET=0 (hlsl - d3d11 / Windows only!)
	@echo   TARGET=1 (hlsl - d3d11 / Windows only!)

build:
	@nmake /nologo /f nmakefile TARGET=0 all
	@nmake /nologo /f nmakefile TARGET=1 all

clean:
	@nmake /nologo /f nmakefile TARGET=0 clean
	@nmake /nologo /f nmakefile TARGET=1 clean

rebuild:
	@nmake /nologo /f nmakefile clean
	@nmake /nologo /f nmakefile build

!ELSE

all: dirs shaders
	@echo === Done building shaders for TARGET=$(TARGET) ===

clean:
	@if exist $(BUILD_INTERMEDIATE_DIR) rmdir /S /Q $(BUILD_INTERMEDIATE_DIR)

rebuild: clean all

dirs:
    @if not exist $(BUILD_INTERMEDIATE_DIR) mkdir $(BUILD_INTERMEDIATE_DIR)
    @if not exist $(BUILD_OUTPUT_DIR) mkdir $(BUILD_OUTPUT_DIR)

#shaders: showvars $(VS_SOURCES:.sc=.bin) $(FS_SOURCES:.sc=.bin) $(CS_SOURCES:.sc=.bin)
shaders: showvars $(FS_SOURCES:.sc=.bin) $(VS_SOURCES:.sc=.bin)

showvars:
    @echo SHADERC=$(SHADERC)
    @echo SHADERS_DIR=$(SHADERS_DIR)
    @echo BUILD_INTERMEDIATE_DIR=$(BUILD_INTERMEDIATE_DIR)
    @echo VS_FLAGS=$(VS_FLAGS)
    @echo BUILD_OUTPUT_DIR=$(BUILD_OUTPUT_DIR)

# Vertex shaders
fs_cubes.bin:
	@echo Compiling $(@B).sc as fragment shader

	$(SHADERC) -f $(SHADERS_DIR)\$(@B).sc -o $(BUILD_OUTPUT_DIR)\$@ --type fragment $(VS_FLAGS) --depends --disasm
# copy /Y $(BUILD_INTERMEDIATE_DIR)\$@ $(BUILD_OUTPUT_DIR)\$@

# Fragment shaders
vs_cubes.bin:
	@echo Compiling $(@B).sc as fragment shader
	$(SHADERC) -f $(SHADERS_DIR)\$(@B).sc -o $(BUILD_OUTPUT_DIR)\$@ --type vertex $(VS_FLAGS) --depends --disasm

# copy /Y $(BUILD_INTERMEDIATE_DIR)\$@ $(BUILD_OUTPUT_DIR)\$@

# Compute shaders
# cs_basic.bin:
# 	@echo Compiling $(@B).sc as compute shader
# 	$(SHADERC) $(CS_FLAGS) --type compute --depends -o $(BUILD_INTERMEDIATE_DIR)$@ -f $(SHADERS_DIR)$(@B).sc --disasm
# 	copy /Y $(BUILD_INTERMEDIATE_DIR)\$@ $(BUILD_OUTPUT_DIR)\$@

!ENDIF

$input v_color0

#include <bgfx_shader.sh>

// Metal requires explicit return type
#ifdef BGFX_SHADER_LANGUAGE_METAL
struct FragmentOutput {
    float4 color [[color(0)]];
};
#endif

void main()
{
    vec4 color = v_color0;

    // Handle platform-specific color channel ordering
    #if BGFX_SHADER_LANGUAGE_HLSL
    color = color.bgra;  // DX expects BGRA
    #endif

    // Platform-specific output
    #if BGFX_SHADER_LANGUAGE_METAL
        FragmentOutput out;
        out.color = color;
        return out;
    #elif BGFX_SHADER_LANGUAGE_HLSL
        SV_Target0 = color;
    #elif BGFX_SHADER_LANGUAGE_SPIRV
        gl_FragColor = color;
    #else // GLSL
        gl_FragColor = color;
    #endif
}
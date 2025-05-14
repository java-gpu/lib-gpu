package tech.lib.bgfx.enu;

public enum UniformType {
    /// <summary>
    /// Sampler.
    /// </summary>
    Sampler,

    /// <summary>
    /// Reserved, do not use.
    /// </summary>
    End,

    /// <summary>
    /// 4 floats vector.
    /// </summary>
    Vec4,

    /// <summary>
    /// 3x3 matrix.
    /// </summary>
    Mat3,

    /// <summary>
    /// 4x4 matrix.
    /// </summary>
    Mat4;

    public int toNative() {
        return ordinal();
    }
}

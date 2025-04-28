package tech.gpu.lib.utils;

public interface Poolable {
    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    void reset();
}

package com.sun.jna;

public class MyJFramePointer extends Pointer {
    public MyJFramePointer(Pointer p) {
        super(p.peer);
    }

    public long getPeer() {
        return peer;
    }
}

package tech.lib.ui.data;

import lombok.Data;

import java.util.Arrays;

@Data
public class RingBufferControl {
    private final byte[] buffer;
    private final int size;

    private int write;
    private int read;
    private int current;

    public RingBufferControl(int size) {
        this.size = size;
        this.buffer = new byte[size];
        Arrays.fill(buffer, (byte) 0);
        reset();
    }

    public void reset() {
        this.read = 0;
        this.write = 0;
        this.current = 0;
    }

    // Reserves space by advancing current pointer (non-committed write)
    public boolean reserve(int length) {
        if (availableToWrite() < length) {
            return false;
        }

        int tempCurrent = (current + length) % size;

        // Ensure we don't overtake the read pointer
        if ((write <= read && tempCurrent >= read) || (write > read && (tempCurrent >= read && tempCurrent < write))) {
            return false;
        }

        current = tempCurrent;
        return true;
    }

    // Commit reserved space (i.e., finalize the write)
    public void commit() {
        write = current;
    }

    // Reset current to write pointer (e.g., discard reserved space)
    public void cancel() {
        current = write;
    }

    // Write data immediately (skips reserve/commit logic)
    public int write(byte[] data) {
        int toWrite = Math.min(data.length, availableToWrite());
        for (int i = 0; i < toWrite; i++) {
            buffer[write] = data[i];
            write = (write + 1) % size;
            current = write;
        }
        return toWrite;
    }

    // Read data
    public int read(byte[] dst, int length) {
        int toRead = Math.min(length, availableToRead());
        for (int i = 0; i < toRead; i++) {
            dst[i] = buffer[read];
            read = (read + 1) % size;
        }
        return toRead;
    }

    // Available bytes to read
    public int availableToRead() {
        if (write >= read) {
            return write - read;
        } else {
            return size - (read - write);
        }
    }

    // Available bytes to write
    public int availableToWrite() {
        if (write >= read) {
            return size - (write - read) - 1;
        } else {
            return read - write - 1;
        }
    }
}

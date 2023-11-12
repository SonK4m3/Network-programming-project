package com.goreckia.game.plane;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class LoopingByteInputStream extends ByteArrayInputStream
{
    private boolean closed;

    public LoopingByteInputStream(final byte[] buffer) {
        super(buffer);
        this.closed = false;
    }

    @Override
    public int read(final byte[] buffer, final int offset, final int length) {
        if (this.closed) {
            return -1;
        }
        int totalBytesRead = 0;
        while (totalBytesRead < length) {
            final int numBytesRead = super.read(buffer, offset + totalBytesRead, length - totalBytesRead);
            if (numBytesRead > 0) {
                totalBytesRead += numBytesRead;
            }
            else {
                this.reset();
            }
        }
        return totalBytesRead;
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.closed = true;
    }
}


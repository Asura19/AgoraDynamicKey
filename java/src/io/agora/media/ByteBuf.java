package io.agora.media;

import java.nio.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Li on 10/1/2016.
 */
public class ByteBuf {
    ByteBuffer buffer = null;

    public ByteBuf() {
        buffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);
    }
    public ByteBuf(byte[] bytes) {
        buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }

    public byte[] asBytes() {
        byte[] out = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(out, 0, out.length);
        return out;
    }

    public ByteBuf put(short v) {
        buffer.putShort(v);
        return this;
    }

    public ByteBuf put(byte[] v) {
        put((short)v.length);
        buffer.put(v);
        return this;
    }

    public ByteBuf put(int v) {
        buffer.putInt(v);
        return this;
    }

    public ByteBuf put(String v) {
        return put(v.getBytes());
    }

    public ByteBuf put(TreeMap<Short, String> extra) {
        put((short)extra.size());

        for (Map.Entry<Short, String> pair : extra.entrySet()) {
            put(pair.getKey());
            put(pair.getValue());
        }

        return this;
    }

    public short readShort() {
        return buffer.getShort();
    }

    public int readInt() {
        return buffer.getInt();
    }

    public String readString() {
        short length = readShort();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

    public void readMap(TreeMap<Short, String> extra) {
        short length = readShort();
        for (short i = 0; i < length; i++) {
            short k = readShort();
            String v = readString();
            extra.put(k, v);
        }
    }
}

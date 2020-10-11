package com.paulmandal.atak.libcotshrink.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipHelper {
    private static final int BUFFER_SIZE = 32;

    public static byte[] compress(String input) throws IOException {
        return compress(input.getBytes());
    }

    public static byte[] compress(byte[] input) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(input.length);
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(input);
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return compressed;
    }

    public static String decompress(byte[] compressed) throws IOException {
        return new String(decompressBytes(compressed));
    }

    public static byte[] decompressBytes(byte[] compressed) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int bytesRead;
        byte[] data = new byte[BUFFER_SIZE];

        while ((bytesRead = gis.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        gis.close();
        is.close();
        buffer.flush();

        return buffer.toByteArray();
    }
}

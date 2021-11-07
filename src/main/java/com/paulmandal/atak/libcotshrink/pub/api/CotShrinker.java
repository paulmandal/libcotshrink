package com.paulmandal.atak.libcotshrink.pub.api;

import android.util.Log;

import androidx.annotation.Nullable;

import com.atakmap.coremap.cot.event.CotEvent;
import com.paulmandal.atak.libcotshrink.exi.ExiConverter;
import com.paulmandal.atak.libcotshrink.gzip.GzipHelper;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.CotEventProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.MappingNotFoundException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;

import java.io.IOException;

/**
 * CotShrinker exposes the libcotshrink API
 */
public class  CotShrinker {
    private static final String TAG = CotShrinker.class.getSimpleName();

    private ExiConverter mExiConverter;
    private CotEventProtobufConverter mCotEventProtobufConverter;

    public CotShrinker(ExiConverter exiConverter, CotEventProtobufConverter cotEventProtobufConverter) {
        mExiConverter = exiConverter;
        mCotEventProtobufConverter = cotEventProtobufConverter;
    }

    /**
     * Converts a {@link CotEvent} to a byte array without any data loss
     *
     * @param cotEvent The CotEvent to convert to a byte array
     * @return a byte array or null if there was an error
     */
    @Nullable
    public byte[] toByteArray(CotEvent cotEvent) {
        byte[] exiBytes = mExiConverter.xmlToExi(cotEvent.toString());

        byte[] compressedBytes = null;

        try {
            compressedBytes = GzipHelper.compress(exiBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (compressedBytes != null && compressedBytes.length < exiBytes.length){
            return compressedBytes;
        }

        return exiBytes;
    }

    /**
     * Converts a {@link CotEvent} to a byte array with some precision loss (documented in the libcotshrink README)
     *
     * @param cotEvent The CotEvent to convert to a byte array
     * @return a byte array or null if there was an error
     */
    @Nullable
    public byte[] toByteArrayLossy(CotEvent cotEvent) {
        byte[] cotEventBytes = null;

        try {
            cotEventBytes = mCotEventProtobufConverter.toByteArray(cotEvent);
        } catch (MappingNotFoundException | UnknownDetailFieldException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        if (cotEventBytes == null) {
            cotEventBytes = toByteArray(cotEvent);
        }

        byte[] compressedCotEventBytes = null;
        try {
            compressedCotEventBytes = GzipHelper.compress(cotEventBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (compressedCotEventBytes != null && compressedCotEventBytes.length < cotEventBytes.length) {
            return compressedCotEventBytes;
        }

        return cotEventBytes;
    }

    /**
     * Converts a byte array into a {@link CotEvent}. The byte array input can be from either a lossy or lossless conversion.
     *
     * @param cotEventBytes The byte array representing your CotEvent
     * @return A CotEvent or null if there was an error
     */
    @Nullable
    public CotEvent toCotEvent(byte[] cotEventBytes) {
        CotEvent cotEvent;

        try {
            cotEventBytes = GzipHelper.decompressBytes(cotEventBytes);
        } catch (IOException e) {
            // Do nothing, we don't always expect to get GZip
        }

        cotEvent = mCotEventProtobufConverter.toCotEvent(cotEventBytes);

        if (cotEvent == null) {
            String cotXml = mExiConverter.exiToXml(cotEventBytes);

            if (cotXml != null) {
                cotEvent = CotEvent.parse(cotXml);
            }
        }

        return cotEvent;
    }
}

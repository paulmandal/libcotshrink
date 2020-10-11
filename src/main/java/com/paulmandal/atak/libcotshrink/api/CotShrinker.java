package com.paulmandal.atak.libcotshrink.api;

import android.util.Log;

import androidx.annotation.Nullable;

import com.atakmap.coremap.cot.event.CotEvent;
import com.paulmandal.atak.libcotshrink.exi.ExiConverter;
import com.paulmandal.atak.libcotshrink.gzip.GzipHelper;
import com.paulmandal.atak.libcotshrink.protobuf.CotEventProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.MappingNotFoundException;
import com.paulmandal.atak.libcotshrink.protobuf.UnknownDetailFieldException;

import java.io.IOException;

public class CotShrinker {
    private static final String TAG = CotShrinker.class.getSimpleName();

    private ExiConverter mExiConverter;
    private CotEventProtobufConverter mCotEventProtobufConverter;

    public CotShrinker(ExiConverter exiConverter, CotEventProtobufConverter cotEventProtobufConverter) {
        mExiConverter = exiConverter;
        mCotEventProtobufConverter = cotEventProtobufConverter;
    }

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
            cotEvent = CotEvent.parse(cotXml);
        }

        return cotEvent;
    }
}

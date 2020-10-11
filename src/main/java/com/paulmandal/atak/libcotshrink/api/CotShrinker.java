package com.paulmandal.atak.libcotshrink.api;

import android.util.Log;

import androidx.annotation.Nullable;

import com.atakmap.coremap.cot.event.CotEvent;
import com.paulmandal.atak.libcotshrink.exi.ExiConverter;
import com.paulmandal.atak.libcotshrink.protobuf.CotEventProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.MappingNotFoundException;
import com.paulmandal.atak.libcotshrink.protobuf.UnknownDetailFieldException;

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
        return mExiConverter.xmlToExi(cotEvent.toString());
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
            return toByteArray(cotEvent);
        }

        return cotEventBytes;
    }

    @Nullable
    public CotEvent toCotEvent(byte[] cotEventBytes) {
        CotEvent cotEvent;
        cotEvent = mCotEventProtobufConverter.toCotEvent(cotEventBytes);

        if (cotEvent == null) {
            String cotXml = mExiConverter.exiToXml(cotEventBytes);
            cotEvent = CotEvent.parse(cotXml);
        }

        return cotEvent;
    }
}

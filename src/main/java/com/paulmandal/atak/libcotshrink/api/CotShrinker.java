package com.paulmandal.atak.libcotshrink.api;

import com.atakmap.coremap.cot.event.CotEvent;
import com.paulmandal.atak.libcotshrink.exi.ExiConverter;

public class CotShrinker {
    private ExiConverter mExiConverter;
    private com.paulmandal.atak.libcotshrink.protobuf.CotEventProtobufConverter mCotEventProtobufConverter;

    public CotShrinker(ExiConverter exiConverter) {
        mExiConverter = exiConverter;
    }

    public byte[] cotEventToBytes(CotEvent cotEvent) {
        // TODO: impl
        return null;
    }

    public CotEvent cotEventFromBytes(byte[] cotEventBytes) {
        // TODO: impl
        return null;
    }
}

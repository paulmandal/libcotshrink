package com.paulmandal.atak.libcotshrink.api;

import com.paulmandal.atak.libcotshrink.exi.ExiConverter;
import com.paulmandal.atak.libcotshrink.exi.ExiConverterFactory;
import com.paulmandal.atak.libcotshrink.protobuf.CotEventProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.CotEventProtobufConverterFactory;

public class CotShrinkerFactory {
    public CotShrinker createCotShrinker() {
        ExiConverterFactory exiConverterFactory = new ExiConverterFactory();
        ExiConverter exiConverter = exiConverterFactory.createExiConverter();

        CotEventProtobufConverterFactory cotEventProtobufConverterFactory = new CotEventProtobufConverterFactory();
        CotEventProtobufConverter cotEventProtobufConverter = cotEventProtobufConverterFactory.createCotEventProtobufConverter();

        return new CotShrinker(exiConverter, cotEventProtobufConverter);
    }
}

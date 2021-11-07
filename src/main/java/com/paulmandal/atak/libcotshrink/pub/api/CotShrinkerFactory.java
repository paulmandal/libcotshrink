package com.paulmandal.atak.libcotshrink.pub.api;

import com.paulmandal.atak.libcotshrink.exi.ExiConverter;
import com.paulmandal.atak.libcotshrink.exi.ExiConverterFactory;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.CotEventProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.CotEventProtobufConverterFactory;

/**
 * Factory for CotShrinker instances
 */
public class CotShrinkerFactory {
    /**
     * Create a CotShrinker instance
     *
     * @return A CotShrinker instance
     */
    public CotShrinker createCotShrinker() {
        ExiConverterFactory exiConverterFactory = new ExiConverterFactory();
        ExiConverter exiConverter = exiConverterFactory.createExiConverter();

        CotEventProtobufConverterFactory cotEventProtobufConverterFactory = new CotEventProtobufConverterFactory();
        CotEventProtobufConverter cotEventProtobufConverter = cotEventProtobufConverterFactory.createCotEventProtobufConverter();

        return new CotShrinker(exiConverter, cotEventProtobufConverter);
    }
}

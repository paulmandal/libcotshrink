package com.paulmandal.atak.libcotshrink.api;

import com.paulmandal.atak.libcotshrink.exi.ExiConverter;
import com.paulmandal.atak.libcotshrink.exi.ExiConverterFactory;

public class CotShrinkerFactory {
    public CotShrinker createCotShrinker() {
        ExiConverterFactory exiConverterFactory = new ExiConverterFactory();
        ExiConverter exiConverter = exiConverterFactory.createExiConverter();

        return new CotShrinker(exiConverter);
    }
}

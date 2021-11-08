package com.paulmandal.atak.libcotshrink.protobuf.utils;

public class PrecisionUtil {

    public int reducePrecision(String s, double factor) {
        return reducePrecision(Double.parseDouble(s), factor);
    }

    public int reducePrecision(double d, double factor) {
        return (int)(d * factor);
    }
}

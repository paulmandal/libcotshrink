package com.paulmandal.atak.libcotshrink.protobuf;

import com.atakmap.coremap.maps.time.CoordinatedTime;
import com.paulmandal.atak.libcotshrink.protobuf.utils.BitUtils;

/**
 * CustomBytes Format (fixed64):
 *
 * time   (25 bits, seconds since start of year)
 * stale  (23 bits, seconds after `time`)
 * hae    (16 bits, whole meters)
 */
public class CustomBytesConverter {
    private static final int LONG_INT_LENGTH = 64;

    private static final int CUSTOM_FIELD_TIME_LENGTH = 25;
    private static final int CUSTOM_FIELD_STALE_LENGTH = 23;
    private static final int CUSTOM_FIELD_HAE_LENGTH = 16;

    private static final double HAE_ALT_PRECISION_FACTOR_ALT = Constants.HAE_ALT_PRECISION_FACTOR_ALT;

    private static final int MAX_HAE_PACKED = 20945;
    private static final int MAX_HAE = 9999999;
    private static final int HAE_OFFSET = 900;

    public long packCustomBytes(CoordinatedTime time, CoordinatedTime stale, double hae, long startOfYearMs) {
        ShiftTracker shiftTracker = new ShiftTracker();
        long customBytes = 0;

        long timeSinceStartOfYear = (time.getMilliseconds() - startOfYearMs) / 1000L;
        customBytes = BitUtils.packBits(customBytes, LONG_INT_LENGTH, timeSinceStartOfYear, CUSTOM_FIELD_TIME_LENGTH, shiftTracker);

        long timeUntilStale = stale.getMilliseconds() / 1000L - time.getMilliseconds() / 1000L;
        customBytes = BitUtils.packBits(customBytes, LONG_INT_LENGTH, timeUntilStale, CUSTOM_FIELD_STALE_LENGTH, shiftTracker);

        if (hae == MAX_HAE) {
            hae = MAX_HAE_PACKED;
        }

        hae = hae + HAE_OFFSET;
        hae = hae * HAE_ALT_PRECISION_FACTOR_ALT;
        customBytes = BitUtils.packBits(customBytes, LONG_INT_LENGTH, (long)hae, CUSTOM_FIELD_HAE_LENGTH, shiftTracker);

        return customBytes;
    }

    public CustomBytesFields unpackCustomBytes(long customBytes, long startOfYearMs) {
        ShiftTracker shiftTracker = new ShiftTracker();

        long timeSinceStartOfYear = BitUtils.unpackBits(customBytes, LONG_INT_LENGTH, CUSTOM_FIELD_TIME_LENGTH, shiftTracker);
        CoordinatedTime time = new CoordinatedTime(startOfYearMs + timeSinceStartOfYear * 1000);

        long timeUntilStale = BitUtils.unpackBits(customBytes, LONG_INT_LENGTH, CUSTOM_FIELD_STALE_LENGTH, shiftTracker);
        CoordinatedTime stale = new CoordinatedTime(time.getMilliseconds() + timeUntilStale * 1000);

        long haeLong = BitUtils.unpackBits(customBytes, LONG_INT_LENGTH, CUSTOM_FIELD_HAE_LENGTH, shiftTracker);
        double hae = haeLong / HAE_ALT_PRECISION_FACTOR_ALT;
        hae = hae - HAE_OFFSET;

        if (hae == MAX_HAE_PACKED) {
            hae = MAX_HAE;
        }

        return new CustomBytesFields(time, stale, hae);
    }
}

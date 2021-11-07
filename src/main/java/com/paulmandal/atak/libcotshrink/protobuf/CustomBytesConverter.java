package com.paulmandal.atak.libcotshrink.protobuf;

import com.atakmap.coremap.maps.time.CoordinatedTime;
import com.paulmandal.atak.libcotshrink.protobuf.utils.BitUtils;

/**
 * CustomBytes Format (fixed64):
 *
 * time   (25 bits, seconds since start of year)
 * stale  (17 bits, seconds after `time`)
 * hae    (14 bits, whole meters)
 */
public class CustomBytesConverter {
    private static final int LONG_INT_LENGTH = 64;

    private static final int CUSTOM_FIELD_TIME_LENGTH = 25;
    private static final int CUSTOM_FIELD_STALE_LENGTH = 17;
    private static final int CUSTOM_FIELD_HAE_LENGTH = 16;

    private static final int MAX_HAE_PACKED = 65535;
    private static final int MAX_HAE = 9999999;

    public long packCustomBytes(CoordinatedTime time, CoordinatedTime stale, double hae, long startOfYearMs) {
        ShiftTracker shiftTracker = new ShiftTracker();
        long customBytes = 0;

        long timeSinceStartOfYear = (time.getMilliseconds() - startOfYearMs) / 1000L;
        customBytes = BitUtils.packBits(customBytes, LONG_INT_LENGTH, timeSinceStartOfYear, CUSTOM_FIELD_TIME_LENGTH, shiftTracker);

        long timeUntilStale = (stale.getMilliseconds() - time.getMilliseconds()) / 1000L;
        customBytes = BitUtils.packBits(customBytes, LONG_INT_LENGTH, timeUntilStale, CUSTOM_FIELD_STALE_LENGTH, shiftTracker);

        if (hae == MAX_HAE) {
            hae = MAX_HAE_PACKED;
        }

        customBytes = BitUtils.packBits(customBytes, LONG_INT_LENGTH, (long)hae, CUSTOM_FIELD_HAE_LENGTH, shiftTracker);

        return customBytes;
    }

    public CustomBytesFields unpackCustomBytes(long customBytes, long startOfYearMs) {
        ShiftTracker shiftTracker = new ShiftTracker();

        long timeSinceStartOfYear = BitUtils.unpackBits(customBytes, LONG_INT_LENGTH, CUSTOM_FIELD_TIME_LENGTH, shiftTracker);
        CoordinatedTime time = new CoordinatedTime(startOfYearMs + timeSinceStartOfYear * 1000);

        long timeUntilStale = BitUtils.unpackBits(customBytes, LONG_INT_LENGTH, CUSTOM_FIELD_STALE_LENGTH, shiftTracker);
        CoordinatedTime stale = new CoordinatedTime(time.getMilliseconds() + timeUntilStale * 1000);

        double hae = (double)BitUtils.unpackBits(customBytes, LONG_INT_LENGTH, CUSTOM_FIELD_HAE_LENGTH, shiftTracker);

        if (hae == MAX_HAE_PACKED) {
            hae = MAX_HAE;
        }

        return new CustomBytesFields(time, stale, hae);
    }
}

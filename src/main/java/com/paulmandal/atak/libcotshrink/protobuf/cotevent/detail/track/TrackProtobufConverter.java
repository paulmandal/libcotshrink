package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.track;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.Constants;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.PrecisionUtil;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufTrack;

public class TrackProtobufConverter {
    private static final String KEY_TRACK = "track";

    private static final String KEY_COURSE = "course";
    private static final String KEY_SPEED = "speed";

    private static final double COURSE_PRECISION_FACTOR = Constants.COURSE_PRECISION_FACTOR;
    private static final double SPEED_PRECISION_FACTOR = Constants.SPEED_PRECISION_FACTOR;

    private static final int NULL_MARKER = -1;

    private final PrecisionUtil mPrecisionUtil;

    public TrackProtobufConverter(PrecisionUtil precisionUtil) {
        mPrecisionUtil = precisionUtil;
    }

    public ProtobufTrack.Track toTrack(CotDetail cotDetail) throws UnknownDetailFieldException {
        ProtobufTrack.Track.Builder builder = ProtobufTrack.Track.newBuilder();
        builder.setCourse(NULL_MARKER);
        builder.setSpeed(NULL_MARKER);
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_COURSE:
                    builder.setCourse(mPrecisionUtil.reducePrecision(attribute.getValue(), COURSE_PRECISION_FACTOR));
                    break;
                case KEY_SPEED:
                    builder.setSpeed(mPrecisionUtil.reducePrecision(attribute.getValue(), SPEED_PRECISION_FACTOR));
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: track." + attribute.getName());
            }
        }
        return builder.build();
    }

    public void maybeAddTrack(CotDetail cotDetail, ProtobufTrack.Track track) {
        if (track == null || track == ProtobufTrack.Track.getDefaultInstance()) {
            return;
        }

        CotDetail trackDetail = new CotDetail(KEY_TRACK);

        if (track.getCourse() != NULL_MARKER) {
            trackDetail.setAttribute(KEY_COURSE, Double.toString(track.getCourse() / COURSE_PRECISION_FACTOR));
        }
        if (track.getSpeed() != NULL_MARKER) {
            trackDetail.setAttribute(KEY_SPEED, Double.toString(track.getSpeed() / SPEED_PRECISION_FACTOR));
        }
        cotDetail.addChild(trackDetail);
    }
}

package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.remarks;

import static com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues.UID_SUBSTITUTION_MARKER;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.maps.time.CoordinatedTime;
import com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.StringUtils;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufRemarks;

import java.text.ParseException;

public class RemarksProtobufConverter {
    private static final String KEY_REMARKS = "remarks";

    private static final String KEY_SOURCE = "source";
    private static final String KEY_TO = "to";
    private static final String KEY_TIME = "time";

    public ProtobufRemarks.Remarks toRemarks(CotDetail cotDetail, SubstitutionValues substitutionValues, long startOfYearMs) throws UnknownDetailFieldException, UnhandledInnerTextException, UnhandledChildException {
        if (cotDetail.getChildren() != null && cotDetail.getChildren().size() > 0) {
            throw new UnhandledChildException("Unhandled child: " + cotDetail.getChildren().get(0).getElementName());
        }

        ProtobufRemarks.Remarks.Builder builder = ProtobufRemarks.Remarks.newBuilder();
        String remarks = cotDetail.getInnerText();
        if (remarks != null) {
            builder.setRemarks(remarks);
        }
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_SOURCE:
                    String source = attribute.getValue();
                    if (source.contains(substitutionValues.uidFromGeoChat)) {
                        builder.setSource(source.replace(substitutionValues.uidFromGeoChat, UID_SUBSTITUTION_MARKER));
                    } else {
                        builder.setSource(source);
                    }
                    break;
                case KEY_TO:
                    builder.setTo(attribute.getValue());
                    break;
                case KEY_TIME:
                    try {
                        long time = CoordinatedTime.fromCot(attribute.getValue()).getMilliseconds();
                        long sinceStartOfYear = (time - startOfYearMs) / 1000;
                        builder.setTime((int) sinceStartOfYear);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: remarks." + attribute.getName());
            }
        }
        return builder.build();
    }

    public void maybeAddRemarks(CotDetail cotDetail, ProtobufRemarks.Remarks remarks, SubstitutionValues substitutionValues, long startOfYearMs) {
        if (remarks == null || remarks == ProtobufRemarks.Remarks.getDefaultInstance()) {
            return;
        }

        CotDetail remarksDetail = new CotDetail(KEY_REMARKS);

        if (!StringUtils.isNullOrEmpty(remarks.getRemarks())) {
            remarksDetail.setInnerText(remarks.getRemarks());
        }
        String source = remarks.getSource();
        if (!StringUtils.isNullOrEmpty(source)) {
            if (source.contains(UID_SUBSTITUTION_MARKER)) {
                source = source.replace(UID_SUBSTITUTION_MARKER, substitutionValues.uidFromGeoChat);
            }
            remarksDetail.setAttribute(KEY_SOURCE, source);
        }
        if (!StringUtils.isNullOrEmpty(remarks.getTo())) {
            remarksDetail.setAttribute(KEY_TO, remarks.getTo());
        }
        long timeOffset = remarks.getTime();
        if (timeOffset > 0) {
            long timeMs = startOfYearMs + (timeOffset * 1000);
            CoordinatedTime time = new CoordinatedTime(timeMs);
            remarksDetail.setAttribute(KEY_TIME, time.toString());
        }

        cotDetail.addChild(remarksDetail);
    }
}

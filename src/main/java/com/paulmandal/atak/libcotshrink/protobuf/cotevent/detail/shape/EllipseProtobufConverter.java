package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.Constants;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.PrecisionUtil;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufEllipse;

import java.util.List;

public class EllipseProtobufConverter {
    private static final String KEY_ELLIPSE = "ellipse";

    private static final String KEY_MAJOR = "major";
    private static final String KEY_MINOR = "minor";
    private static final String KEY_ANGLE = "angle";

    private static final double ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR = Constants.ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR;

    private final PrecisionUtil mPrecisionUtil;

    public EllipseProtobufConverter(PrecisionUtil precisionUtil) {
        mPrecisionUtil = precisionUtil;
    }

    public ProtobufEllipse.Ellipse toEllipse(CotDetail cotDetail) throws UnknownDetailFieldException {
        ProtobufEllipse.Ellipse.Builder builder = ProtobufEllipse.Ellipse.newBuilder();

        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_MAJOR:
                    builder.setMajor(mPrecisionUtil.reducePrecision(attribute.getValue(), ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR));
                    break;
                case KEY_MINOR:
                    builder.setMinor(mPrecisionUtil.reducePrecision(attribute.getValue(), ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR));
                    break;
                case KEY_ANGLE:
                    builder.setAngle(Integer.parseInt(attribute.getValue()));
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle child attribute: ellipse." + attribute.getName());
            }
        }

        List<CotDetail> children = cotDetail.getChildren();
        for (CotDetail child : children) {
            switch (child.getElementName()) {
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle child object: ellipse." + child.getElementName());
            }
        }

        return builder.build();
    }

    public void maybeAddEllipse(CotDetail cotDetail, ProtobufEllipse.Ellipse ellipse) {
        if (ellipse == null || ellipse == ProtobufEllipse.Ellipse.getDefaultInstance()) {
            return;
        }

        CotDetail ellipseDetail = new CotDetail(KEY_ELLIPSE);

        ellipseDetail.setAttribute(KEY_MAJOR, Double.toString(ellipse.getMajor() / ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR));
        ellipseDetail.setAttribute(KEY_MINOR, Double.toString(ellipse.getMinor() / ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR));
        ellipseDetail.setAttribute(KEY_ANGLE, Integer.toString(ellipse.getAngle()));

        cotDetail.addChild(ellipseDetail);
    }
}

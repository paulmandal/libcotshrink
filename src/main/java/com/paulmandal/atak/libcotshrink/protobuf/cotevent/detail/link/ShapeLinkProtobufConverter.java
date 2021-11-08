package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.link;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.Constants;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.PrecisionUtil;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufDrawnShape;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufShapeLink;

public class ShapeLinkProtobufConverter {
    private static final String KEY_LINK = "link";

    private static final String KEY_POINT = "point";

    private static final double LAT_LON_INT_CONVERSION_FACTOR = Constants.LAT_LON_INT_CONVERSION_FACTOR;

    private static final int NULL_VALUE = -1;

    private final PrecisionUtil mPrecisionUtil;

    public ShapeLinkProtobufConverter(PrecisionUtil precisionUtil) {
        mPrecisionUtil = precisionUtil;
    }

    public void toShapeLink(CotDetail cotDetail, ProtobufDrawnShape.DrawnShape.Builder shapeBuilder) throws UnknownDetailFieldException {
        ProtobufShapeLink.ShapeLink.Builder builder = ProtobufShapeLink.ShapeLink.newBuilder();
        CotAttribute[] attributes = cotDetail.getAttributes();

        builder.setLat(NULL_VALUE);
        builder.setLon(NULL_VALUE);
        builder.setHae(NULL_VALUE);

        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_POINT:
                    String[] splitPoint = attribute.getValue().split(",");
                    if (splitPoint.length > 0) {
                        builder.setLat(mPrecisionUtil.reducePrecision(splitPoint[0], LAT_LON_INT_CONVERSION_FACTOR));
                    }
                    if (splitPoint.length > 1) {
                        builder.setLon(mPrecisionUtil.reducePrecision(splitPoint[1], LAT_LON_INT_CONVERSION_FACTOR));
                    }
                    if (splitPoint.length > 2) {
                        builder.setHae(Double.parseDouble(splitPoint[2]));
                    }
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: link[shape]." + attribute.getName());
            }
        }

        shapeBuilder.addLink(builder);
    }

    public void maybeAddDrawnShape(CotDetail cotDetail, ProtobufDrawnShape.DrawnShape drawnShape) {
        if (drawnShape == null || drawnShape == ProtobufDrawnShape.DrawnShape.getDefaultInstance()
                || drawnShape.getLinkList() == ProtobufDrawnShape.DrawnShape.getDefaultInstance().getLinkList()) {
            return;
        }

        for (ProtobufShapeLink.ShapeLink link : drawnShape.getLinkList()) {
            CotDetail linkDetail = new CotDetail(KEY_LINK);

            String point = "";
            if (link.getLat() != NULL_VALUE) {
                point += Double.toString(link.getLat() / LAT_LON_INT_CONVERSION_FACTOR);
            }
            if (link.getLon() != NULL_VALUE) {
                point += ((point.length() > 0 ? "," : "") + link.getLon() / LAT_LON_INT_CONVERSION_FACTOR);
            }
            if (link.getHae() != NULL_VALUE) {
                point += ((point.length() > 0 ? "," : "") + link.getHae());
            }
            linkDetail.setAttribute(KEY_POINT, point);

            cotDetail.addChild(linkDetail);
        }
    }
}

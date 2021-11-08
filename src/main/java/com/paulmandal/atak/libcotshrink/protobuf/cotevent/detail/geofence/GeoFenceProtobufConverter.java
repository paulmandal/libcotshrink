package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.geofence;

import android.util.Log;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.Constants;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.PrecisionUtil;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufGeoFence;

import java.util.List;

public class GeoFenceProtobufConverter {
    private static final String TAG = GeoFenceProtobufConverter.class.getSimpleName();

    private static final String KEY_GEOFENCE = "__geofence";

    private static final String KEY_ELEVATION_MONITORED = "elevationMonitored";
    private static final String KEY_MIN_ELEVATION = "minElevation";
    private static final String KEY_MONITOR = "monitor";
    private static final String KEY_TRIGGER = "trigger";
    private static final String KEY_TRACKING = "tracking";
    private static final String KEY_MAX_ELEVATION = "maxElevation";
    private static final String KEY_BOUNDING_SPHERE = "boundingSphere";

    private static final double HAE_ALT_PRECISION_FACTOR = Constants.HAE_ALT_PRECISION_FACTOR;
    private static final double GEOFENCE_BOUNDING_SPHERE_PRECISION_FACTOR = Constants.GEOFENCE_BOUNDING_SPHERE_PRECISION_FACTOR;

    private final PrecisionUtil mPrecisionUtil;

    private static final String NAN_MARKER = "NaN";
    private static final int NULL_MARKER = -1;

    public GeoFenceProtobufConverter(PrecisionUtil precisionUtil) {
        mPrecisionUtil = precisionUtil;
    }

    public ProtobufGeoFence.GeoFence toGeoFence(CotDetail cotDetail) throws UnknownDetailFieldException, UnhandledInnerTextException, UnhandledChildException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        ProtobufGeoFence.GeoFence.Builder builder = ProtobufGeoFence.GeoFence.newBuilder();

        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_ELEVATION_MONITORED:
                    builder.setElevationMonitored(Boolean.parseBoolean(attribute.getValue()));
                    break;
                case KEY_MIN_ELEVATION:
                    String minElevationStr = attribute.getValue();
                    if (minElevationStr.equals(NAN_MARKER)) {
                        builder.setMinElevation(NULL_MARKER);
                    } else {
                        builder.setMinElevation(mPrecisionUtil.reducePrecision(minElevationStr, HAE_ALT_PRECISION_FACTOR));
                    }
                    break;
                case KEY_MONITOR:
                    builder.setMonitor(ProtobufGeoFence.GeoFence.Monitor.valueOf(attribute.getValue().toUpperCase()));
                    break;
                case KEY_TRIGGER:
                    builder.setTrigger(ProtobufGeoFence.GeoFence.Trigger.valueOf(attribute.getValue().toUpperCase()));
                    break;
                case KEY_TRACKING:
                    builder.setTracking(Boolean.parseBoolean(attribute.getValue()));
                    break;
                case KEY_MAX_ELEVATION:
                    String maxElevationStr = attribute.getValue();
                    if (maxElevationStr.equals(NAN_MARKER)) {
                        builder.setMaxElevation(NULL_MARKER);
                    } else {
                        builder.setMaxElevation(mPrecisionUtil.reducePrecision(maxElevationStr, HAE_ALT_PRECISION_FACTOR));
                    }
                    break;
                case KEY_BOUNDING_SPHERE:
                    builder.setBoundingSphere(mPrecisionUtil.reducePrecision(attribute.getValue(), GEOFENCE_BOUNDING_SPHERE_PRECISION_FACTOR));
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle child attribute: __geofence." + attribute.getName());
            }
        }

        List<CotDetail> children = cotDetail.getChildren();
        for (CotDetail child : children) {
            switch (child.getElementName()) {
                default:
                    throw new UnhandledChildException("Don't know how to handle child object: __geofence." + child.getElementName());
            }
        }

        return builder.build();
    }

    public void maybeAddGeoFence(CotDetail cotDetail, ProtobufGeoFence.GeoFence geoFence) {
        if (geoFence == null || geoFence == ProtobufGeoFence.GeoFence.getDefaultInstance()) {
            return;
        }

        CotDetail geoFenceDetail = new CotDetail(KEY_GEOFENCE);

        geoFenceDetail.setAttribute(KEY_ELEVATION_MONITORED, Boolean.toString(geoFence.getElevationMonitored()));
        if (geoFence.getMinElevation() == NULL_MARKER) {
            geoFenceDetail.setAttribute(KEY_MIN_ELEVATION, NAN_MARKER);
        } else {
            geoFenceDetail.setAttribute(KEY_MIN_ELEVATION, Double.toString(geoFence.getMinElevation() / HAE_ALT_PRECISION_FACTOR));
        }
        geoFenceDetail.setAttribute(KEY_MONITOR, xmlNameFromMonitor(geoFence.getMonitor()));
        geoFenceDetail.setAttribute(KEY_TRIGGER, xmlNameFromTrigger(geoFence.getTrigger()));
        geoFenceDetail.setAttribute(KEY_TRACKING, Boolean.toString(geoFence.getTracking()));
        if (geoFence.getMaxElevation() == NULL_MARKER) {
            geoFenceDetail.setAttribute(KEY_MAX_ELEVATION, NAN_MARKER);
        } else {
            geoFenceDetail.setAttribute(KEY_MAX_ELEVATION, Double.toString(geoFence.getMaxElevation() / HAE_ALT_PRECISION_FACTOR));
        }
        geoFenceDetail.setAttribute(KEY_BOUNDING_SPHERE, Double.toString(geoFence.getBoundingSphere() / GEOFENCE_BOUNDING_SPHERE_PRECISION_FACTOR));

        cotDetail.addChild(geoFenceDetail);
    }

    private String xmlNameFromMonitor(ProtobufGeoFence.GeoFence.Monitor monitor) {
        switch (monitor) {
            case TAKUSERS:
                return "TAKUsers";
            case FRIENDLY:
                return "Friendly";
            case HOSTILE:
                return "Hostile";
            case CUSTOM:
                return "Custom";
            case ALL:
                return "All";
            default:
                Log.e(TAG, "Unknown monitor type: " + monitor);
        }
        return "";
    }

    private String xmlNameFromTrigger(ProtobufGeoFence.GeoFence.Trigger trigger) {
        switch (trigger) {
            case ENTRY:
                return "Entry";
            case EXIT:
                return "Exit";
            case BOTH:
                return "Both";
            default:
                Log.e(TAG, "Unknown trigger type: " + trigger);
        }
        return "";
    }


}

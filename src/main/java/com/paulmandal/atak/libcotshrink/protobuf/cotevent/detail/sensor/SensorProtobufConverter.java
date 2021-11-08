package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.sensor;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.Constants;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.PrecisionUtil;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufSensor;

public class SensorProtobufConverter {
    private static final String KEY_SENSOR = "sensor";

    private static final String KEY_FOV_RED = "fovRed";
    private static final String KEY_FOV_BLUE = "fovBlue";
    private static final String KEY_FOV_GREEN = "fovGreen";
    private static final String KEY_RANGE = "range";
    private static final String KEY_AZIMUTH = "azimuth";
    private static final String KEY_DISPLAY_MAGNETIC_REFERENCE = "displayMagneticReference";
    private static final String KEY_FOV = "fov";
    private static final String KEY_HIDE_FOV = "hideFov";
    private static final String KEY_FOV_ALPHA = "fovAlpha";

    private static final double SENSOR_FOV_PRECISION_FACTOR = Constants.SENSOR_FOV_PRECISION_FACTOR;

    private final PrecisionUtil mPrecisionUtil;

    public SensorProtobufConverter(PrecisionUtil precisionUtil) {
        mPrecisionUtil = precisionUtil;
    }

    public ProtobufSensor.Sensor toSensor(CotDetail cotDetail) throws UnknownDetailFieldException {
        ProtobufSensor.Sensor.Builder builder = ProtobufSensor.Sensor.newBuilder();
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_FOV_RED:
                    builder.setFovRed(mPrecisionUtil.reducePrecision(attribute.getValue(), SENSOR_FOV_PRECISION_FACTOR));
                    break;
                case KEY_FOV_BLUE:
                    builder.setFovBlue(mPrecisionUtil.reducePrecision(attribute.getValue(), SENSOR_FOV_PRECISION_FACTOR));
                    break;
                case KEY_FOV_GREEN:
                    builder.setFovGreen(mPrecisionUtil.reducePrecision(attribute.getValue(), SENSOR_FOV_PRECISION_FACTOR));
                    break;
                case KEY_RANGE:
                    builder.setRange(Integer.parseInt(attribute.getValue()));
                    break;
                case KEY_AZIMUTH:
                    builder.setAzimuth(Integer.parseInt(attribute.getValue()));
                    break;
                case KEY_DISPLAY_MAGNETIC_REFERENCE:
                    builder.setDisplayMagneticReference(Integer.parseInt(attribute.getValue()));
                    break;
                case KEY_FOV:
                    builder.setFov(Integer.parseInt(attribute.getValue()));
                    break;
                case KEY_HIDE_FOV:
                    builder.setHideFov(Boolean.parseBoolean(attribute.getValue()));
                    break;
                case KEY_FOV_ALPHA:
                    builder.setFovAlpha(mPrecisionUtil.reducePrecision(attribute.getValue(), SENSOR_FOV_PRECISION_FACTOR));
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: sensor." + attribute.getName());
            }
        }

        return builder.build();
    }

    public void maybeAddSensor(CotDetail cotDetail, ProtobufSensor.Sensor sensor) {
        if (sensor == null || sensor == ProtobufSensor.Sensor.getDefaultInstance()) {
            return;
        }

        CotDetail sensorDetail = new CotDetail(KEY_SENSOR);

        sensorDetail.setAttribute(KEY_FOV_RED, Double.toString(sensor.getFovRed() / SENSOR_FOV_PRECISION_FACTOR));
        sensorDetail.setAttribute(KEY_FOV_BLUE, Double.toString(sensor.getFovBlue() / SENSOR_FOV_PRECISION_FACTOR));
        sensorDetail.setAttribute(KEY_FOV_GREEN, Double.toString(sensor.getFovGreen() / SENSOR_FOV_PRECISION_FACTOR));
        sensorDetail.setAttribute(KEY_FOV_ALPHA, Double.toString(sensor.getFovAlpha() / SENSOR_FOV_PRECISION_FACTOR));
        sensorDetail.setAttribute(KEY_RANGE, Integer.toString(sensor.getRange()));
        sensorDetail.setAttribute(KEY_AZIMUTH, Integer.toString(sensor.getAzimuth()));
        sensorDetail.setAttribute(KEY_DISPLAY_MAGNETIC_REFERENCE, Integer.toString(sensor.getDisplayMagneticReference()));
        sensorDetail.setAttribute(KEY_FOV, Integer.toString(sensor.getFov()));
        sensorDetail.setAttribute(KEY_HIDE_FOV, Boolean.toString(sensor.getHideFov()));

        cotDetail.addChild(sensorDetail);
    }
}

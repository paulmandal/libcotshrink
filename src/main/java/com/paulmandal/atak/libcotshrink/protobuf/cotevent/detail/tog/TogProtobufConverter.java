package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.tog;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.CustomBytesExtFields;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;

import java.util.List;

public class TogProtobufConverter {
    private static final String KEY_TOG = "tog";

    private static final String KEY_ENABLED = "enabled";

    public void maybeGetTogValue(CotDetail cotDetail, CustomBytesExtFields customBytesExtFields) {
        CotDetail togDetail = cotDetail.getFirstChildByName(0, KEY_TOG);
        if (togDetail != null) {
            String valueStr = togDetail.getAttribute(KEY_ENABLED);
            if (valueStr != null) {
                customBytesExtFields.tog = valueStr.equals("1");
            }
        }
    }

    public void toTog(CotDetail cotDetail) throws UnknownDetailFieldException, UnhandledInnerTextException, UnhandledChildException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_ENABLED:
                    // Do nothing, we are packing this into bits
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: tog." + attribute.getName());
            }
        }

        List<CotDetail> children = cotDetail.getChildren();
        for (CotDetail child : children) {
            switch (child.getElementName()) {
                case KEY_TOG:
                    break;
                default:
                    throw new UnhandledChildException("Don't know how to handle child object: __geofence." + child.getElementName());
            }
        }
    }

    public void maybeAddTog(CotDetail cotDetail, CustomBytesExtFields customBytesExtFields) {
        if (customBytesExtFields.tog == null) {
            return;
        }

        CotDetail togDetail = new CotDetail(KEY_TOG);

        togDetail.setAttribute(KEY_ENABLED, customBytesExtFields.tog ? "1" : "0");

        cotDetail.addChild(togDetail);
    }
}

package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.cehumaninput;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.CustomBytesExtFields;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;

public class CeHumanInputProtobufConverter {
    private static final String KEY_CE_HUMAN_INPUT = "ce_human_input";

    public void maybeGetCeHumanInputValues(CotDetail cotDetail, CustomBytesExtFields customBytesExtFields) {
        CotDetail ceHumanInputDetail = cotDetail.getFirstChildByName(0, KEY_CE_HUMAN_INPUT);
        if (ceHumanInputDetail != null) {
            String valueStr = ceHumanInputDetail.getInnerText();
            if (valueStr != null) {
                customBytesExtFields.ceHumanInput = Boolean.parseBoolean(valueStr);
            }
        }
    }

    public void toCeHumanInput(CotDetail cotDetail) throws UnknownDetailFieldException {
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: ce_human_input." + attribute.getName());
            }
        }
    }

    public void maybeAddCeHumanInput(CotDetail cotDetail, CustomBytesExtFields customBytesExtFields) {
        if (customBytesExtFields.ceHumanInput == null) {
            return;
        }

        CotDetail ceHumanInputDetail = new CotDetail(KEY_CE_HUMAN_INPUT);

        ceHumanInputDetail.setInnerText(Boolean.toString(customBytesExtFields.ceHumanInput));

        cotDetail.addChild(ceHumanInputDetail);
    }
}

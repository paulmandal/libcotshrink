package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.StringUtils;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufFlowTags;

public class FlowTagsProtobufConverter {
    private static final String KEY_FLOW_TAGS = "_flow-tags_";

    private static final String KEY_ANDROID_MEDICAL_LINE = "AndroidMedicalLine";
    private static final String KEY_OBSTACLES = "obstacles";

    public ProtobufFlowTags.FlowTags toFlowTags(CotDetail cotDetail) throws UnknownDetailFieldException, UnhandledChildException, UnhandledInnerTextException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        if (cotDetail.getChildren() != null && cotDetail.getChildren().size() > 0) {
            throw new UnhandledChildException("Unhandled child: " + cotDetail.getChildren().get(0).getElementName());
        }

        ProtobufFlowTags.FlowTags.Builder builder = ProtobufFlowTags.FlowTags.newBuilder();
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_ANDROID_MEDICAL_LINE:
                    builder.setAndroidMedicalLine(attribute.getValue());
                    break;
                case KEY_OBSTACLES:
                    builder.setObstacles(attribute.getValue());
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: _flow-tags_." + attribute.getName());
            }
        }

        return builder.build();
    }

    public void maybeAddFlowTags(CotDetail cotDetail, ProtobufFlowTags.FlowTags flowTags) {
        if (flowTags == null || flowTags == ProtobufFlowTags.FlowTags.getDefaultInstance()) {
            return;
        }

        CotDetail flowTagsDetail = new CotDetail(KEY_FLOW_TAGS);

        if (!StringUtils.isNullOrEmpty(flowTags.getAndroidMedicalLine())) {
            flowTagsDetail.setAttribute(KEY_ANDROID_MEDICAL_LINE, flowTags.getAndroidMedicalLine());
        }
        if (!StringUtils.isNullOrEmpty(flowTags.getObstacles())) {
            flowTagsDetail.setAttribute(KEY_OBSTACLES, flowTags.getObstacles());
        }

        cotDetail.addChild(flowTagsDetail);
    }
}

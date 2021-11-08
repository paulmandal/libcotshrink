package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.model;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.StringUtils;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufModel;

public class ModelProtobufConverter {
    private static final String KEY_MODEL = "model";

    private static final String KEY_NAME = "name";

    public ProtobufModel.Model toModel(CotDetail cotDetail) throws UnknownDetailFieldException, UnhandledChildException, UnhandledInnerTextException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        if (cotDetail.getChildren() != null && cotDetail.getChildren().size() > 0) {
            throw new UnhandledChildException("Unhandled child: " + cotDetail.getChildren().get(0).getElementName());
        }

        ProtobufModel.Model.Builder builder = ProtobufModel.Model.newBuilder();
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_NAME:
                    builder.setName(attribute.getValue());
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: model." + attribute.getName());
            }
        }
        return builder.build();
    }

    public void maybeAddModel(CotDetail cotDetail, ProtobufModel.Model model) {
        if (model == null || model == ProtobufModel.Model.getDefaultInstance()) {
            return;
        }

        CotDetail modelDetail = new CotDetail(KEY_MODEL);

        if (!StringUtils.isNullOrEmpty(model.getName())) {
            modelDetail.setAttribute(KEY_NAME, model.getName());
        }

        cotDetail.addChild(modelDetail);
    }
}

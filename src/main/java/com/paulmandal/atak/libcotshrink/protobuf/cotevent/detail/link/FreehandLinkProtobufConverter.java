package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.link;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.google.protobuf.ByteString;
import com.paulmandal.atak.libcotshrink.gzip.GzipHelper;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufFreehandLink;

import java.io.IOException;

public class FreehandLinkProtobufConverter {
    private static final String KEY_LINK = "link";

    private static final String KEY_LINE = "line";

    public ProtobufFreehandLink.FreehandLink toFreehandLink(CotDetail cotDetail) throws UnknownDetailFieldException, UnhandledInnerTextException, UnhandledChildException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        if (cotDetail.getChildren() != null && cotDetail.getChildren().size() > 0) {
            throw new UnhandledChildException("Unhandled child: " + cotDetail.getChildren().get(0).getElementName());
        }

        ProtobufFreehandLink.FreehandLink.Builder builder = ProtobufFreehandLink.FreehandLink.newBuilder();
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_LINE:
                    try {
                        builder.setLine(ByteString.copyFrom(GzipHelper.compress(attribute.getValue()))); // TODO: evaluate this using EXI instead
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: freehandlink." + attribute.getName());
            }
        }
        return builder.build();
    }

    public void maybeAddFreehandLink(CotDetail cotDetail, ProtobufFreehandLink.FreehandLink freehandLink) {
        if (freehandLink == null || freehandLink == ProtobufFreehandLink.FreehandLink.getDefaultInstance()) {
            return;
        }

        CotDetail freehandLinkDetail = new CotDetail(KEY_LINK);

        ByteString freehandLine = freehandLink.getLine();
        if (freehandLine != ProtobufFreehandLink.FreehandLink.getDefaultInstance().getLine()) {
            try {
                freehandLinkDetail.setAttribute(KEY_LINE, GzipHelper.decompress(freehandLine.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cotDetail.addChild(freehandLinkDetail);
    }
}

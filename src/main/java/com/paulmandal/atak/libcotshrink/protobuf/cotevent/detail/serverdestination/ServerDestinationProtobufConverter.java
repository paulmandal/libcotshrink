package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.serverdestination;

import static com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues.UID_SUBSTITUTION_MARKER;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.StringUtils;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufServerDestination;

public class ServerDestinationProtobufConverter {
    private static final String KEY_SERVER_DESTINATION = "__serverdestination";

    private static final String KEY_DESTINATIONS = "destinations";

    public ProtobufServerDestination.ServerDestination toServerDestination(CotDetail cotDetail, SubstitutionValues substitutionValues) throws UnknownDetailFieldException, UnhandledInnerTextException, UnhandledChildException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        if (cotDetail.getChildren() != null && cotDetail.getChildren().size() > 0) {
            throw new UnhandledChildException("Unhandled child: " + cotDetail.getChildren().get(0).getElementName());
        }

        ProtobufServerDestination.ServerDestination.Builder builder = ProtobufServerDestination.ServerDestination.newBuilder();
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_DESTINATIONS:
                    String destinations = attribute.getValue();
                    if (destinations.contains(substitutionValues.uidFromGeoChat)) {
                        destinations = destinations.replace(substitutionValues.uidFromGeoChat, UID_SUBSTITUTION_MARKER);
                    }
                    builder.setDestinations(destinations);
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle detail field: serverdestination." + attribute.getName());
            }
        }
        return builder.build();
    }

    public void maybeAddServerDestination(CotDetail cotDetail, ProtobufServerDestination.ServerDestination serverDestination, SubstitutionValues substitutionValues) {
        if (serverDestination == null || serverDestination == ProtobufServerDestination.ServerDestination.getDefaultInstance()) {
            return;
        }

        CotDetail serverDestinationDetail = new CotDetail(KEY_SERVER_DESTINATION);

        String destinations = serverDestination.getDestinations();
        if (!StringUtils.isNullOrEmpty(destinations)) {
            if (destinations.contains(UID_SUBSTITUTION_MARKER)) {
                destinations = destinations.replace(UID_SUBSTITUTION_MARKER, substitutionValues.uidFromGeoChat);
            }
            serverDestinationDetail.setAttribute(KEY_DESTINATIONS, destinations);
        }

        cotDetail.addChild(serverDestinationDetail);
    }
}


package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.chat.hierarchy;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.chat.hierarchy.group.GroupProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufHierarchy;

import java.util.List;

public class HierarchyProtobufConverter {
    private static final String KEY_HIERARCHY = "hierarchy";
    private static final String KEY_GROUP = "group";

    private final GroupProtobufConverter mGroupProtobufConverter;

    public HierarchyProtobufConverter(GroupProtobufConverter groupProtobufConverter) {
        mGroupProtobufConverter = groupProtobufConverter;
    }

    public ProtobufHierarchy.Hierarchy toHierarchy(CotDetail cotDetail, SubstitutionValues substitutionValues) throws UnknownDetailFieldException, UnhandledChildException, UnhandledInnerTextException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        ProtobufHierarchy.Hierarchy.Builder builder = ProtobufHierarchy.Hierarchy.newBuilder();
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle child attribute: chat.hierarchy." + attribute.getName());
            }
        }

        List<CotDetail> children = cotDetail.getChildren();
        for (CotDetail child : children) {
            switch (child.getElementName()) {
                case KEY_GROUP:
                    builder.setGroup(mGroupProtobufConverter.toGroup(child, substitutionValues));
                    break;
                default:
                    throw new UnhandledChildException("Don't know how to handle child object: chat.hierarchy." + child.getElementName());
            }
        }

        return builder.build();
    }

    public void maybeAddHierarchy(CotDetail cotDetail, ProtobufHierarchy.Hierarchy hierarchy, SubstitutionValues substitutionValues) {
        if (hierarchy == null || hierarchy == ProtobufHierarchy.Hierarchy.getDefaultInstance()) {
            return;
        }

        CotDetail hierarchyDetail = new CotDetail(KEY_HIERARCHY);

        mGroupProtobufConverter.maybeAddGroup(hierarchyDetail, hierarchy.getGroup(), substitutionValues);

        cotDetail.addChild(hierarchyDetail);
    }
}

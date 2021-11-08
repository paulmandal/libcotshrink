package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.chat;

import static com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues.ID_SUBSTITUTION_MARKER;
import static com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues.UID_SUBSTITUTION_MARKER;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.StringUtils;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufChatGroup;

import java.util.List;

public class ChatGroupProtobufConverter {
    private static final String KEY_CHAT_GROUP = "chatgrp";

    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";

    public ProtobufChatGroup.ChatGroup toChatGroup(CotDetail cotDetail, SubstitutionValues substitutionValues) throws UnknownDetailFieldException, UnhandledInnerTextException, UnhandledChildException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        if (cotDetail.getChildren() != null && cotDetail.getChildren().size() > 0) {
            throw new UnhandledChildException("Unhandled child: " + cotDetail.getChildren().get(0).getElementName());
        }

        ProtobufChatGroup.ChatGroup.Builder builder = ProtobufChatGroup.ChatGroup.newBuilder();
        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_ID:
                    String id = attribute.getValue();
                    if (id.equals(substitutionValues.idFromChat)) {
                        id = ID_SUBSTITUTION_MARKER;
                    }
                    builder.setId(id);
                    break;
                default:
                    if (attribute.getName().startsWith(KEY_UID)) {
                        String uid = attribute.getValue();
                        if (uid.equals(substitutionValues.uidFromGeoChat)) {
                            uid = UID_SUBSTITUTION_MARKER;
                        } else {
                            substitutionValues.uidsFromChatGroup.add(uid);
                        }
                        builder.addUid(uid);
                        break;
                    }
                    throw new UnknownDetailFieldException("Don't know how to handle child attribute: chat.chatgrp." + attribute.getName());
            }
        }
        return builder.build();
    }

    public void maybeAddChatGroup(CotDetail cotDetail, ProtobufChatGroup.ChatGroup chatGroup, SubstitutionValues substitutionValues) {
        if (chatGroup == null || chatGroup == ProtobufChatGroup.ChatGroup.getDefaultInstance()) {
            return;
        }

        CotDetail chatGroupDetail = new CotDetail(KEY_CHAT_GROUP);

        String id = chatGroup.getId();
        if (!StringUtils.isNullOrEmpty(id)) {
            if (id.equals(ID_SUBSTITUTION_MARKER)) {
                id = substitutionValues.idFromChat;
            }
            chatGroupDetail.setAttribute(KEY_ID, id);
        }
        List<String> uidList = chatGroup.getUidList();
        for (int i = 0; i < uidList.size(); i++) {
            String uid = uidList.get(i);
            if (uid.equals(UID_SUBSTITUTION_MARKER)) {
                uid = substitutionValues.uidFromGeoChat;
            } else {
                substitutionValues.uidsFromChatGroup.add(uid);
            }
            chatGroupDetail.setAttribute(KEY_UID + i, uid);
        }

        cotDetail.addChild(chatGroupDetail);
    }
}

package com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.video;

import com.atakmap.coremap.cot.event.CotAttribute;
import com.atakmap.coremap.cot.event.CotDetail;
import com.paulmandal.atak.libcotshrink.protobuf.SubstitutionValues;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.video.connectionentry.ConnectionEntryProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledChildException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnhandledInnerTextException;
import com.paulmandal.atak.libcotshrink.protobuf.exceptions.UnknownDetailFieldException;
import com.paulmandal.atak.libcotshrink.protobuf.utils.StringUtils;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufConnectionEntry;
import com.paulmandal.atak.libcotshrink.protobufs.ProtobufVideo;

import java.util.List;

public class VideoProtobufConverter {
    private static final String KEY_VIDEO = "__video";

    private static final String KEY_UID = "uid";
    private static final String KEY_URL = "url";

    private static final String KEY_CONNECTION_ENTRY = "ConnectionEntry";

    private ConnectionEntryProtobufConverter mConnectionEntryProtobufConverter;

    public VideoProtobufConverter(ConnectionEntryProtobufConverter connectionEntryProtobufConverter) {
        mConnectionEntryProtobufConverter = connectionEntryProtobufConverter;
    }

    public ProtobufVideo.Video toVideo(CotDetail cotDetail, SubstitutionValues substitutionValues) throws UnknownDetailFieldException, UnhandledInnerTextException, UnhandledChildException {
        if (cotDetail.getInnerText() != null && !cotDetail.getInnerText().isEmpty()) {
            throw new UnhandledInnerTextException("Unhandled inner text: " + cotDetail.getInnerText());
        }

        ProtobufVideo.Video.Builder builder = ProtobufVideo.Video.newBuilder();

        CotAttribute[] attributes = cotDetail.getAttributes();
        for (CotAttribute attribute : attributes) {
            switch (attribute.getName()) {
                case KEY_UID:
                    String uid = attribute.getValue();
                    substitutionValues.uidFromVideo = uid;
                    builder.setUid(uid);
                    break;
                case KEY_URL:
                    builder.setUrl(attribute.getValue());
                    break;
                default:
                    throw new UnknownDetailFieldException("Don't know how to handle child attribute: __video." + attribute.getName());
            }
        }

        List<CotDetail> children = cotDetail.getChildren();
        for (CotDetail child : children) {
            switch (child.getElementName()) {
                case KEY_CONNECTION_ENTRY:
                    builder.addConnectionEntry(mConnectionEntryProtobufConverter.toConnectionEntry(child, substitutionValues));
                    break;
                default:
                    throw new UnhandledChildException("Don't know how to handle child object: __video." + child.getElementName());
            }
        }

        return builder.build();
    }

    public void maybeAddVideo(CotDetail cotDetail, ProtobufVideo.Video video, SubstitutionValues substitutionValues) {
        if (video == null || video == ProtobufVideo.Video.getDefaultInstance()) {
            return;
        }

        CotDetail videoDetail = new CotDetail(KEY_VIDEO);

        String uid = video.getUid();
        if (!StringUtils.isNullOrEmpty(uid)) {
            substitutionValues.uidFromVideo = uid;
            videoDetail.setAttribute(KEY_UID, uid);
        }

        String url = video.getUrl();
        if (!StringUtils.isNullOrEmpty(url)) {
            videoDetail.setAttribute(KEY_URL, url);
        }

        for (ProtobufConnectionEntry.ConnectionEntry connectionEntry : video.getConnectionEntryList()) {
            mConnectionEntryProtobufConverter.maybeAddConnectionEntry(videoDetail, connectionEntry, substitutionValues);
        }

        cotDetail.addChild(videoDetail);
    }
}

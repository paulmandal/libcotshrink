package com.paulmandal.atak.libcotshrink.protobuf.cotevent;

import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.cehumaninput.CeHumanInputProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.chat.ChatGroupProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.link.ChatLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.chat.ChatProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.link.ComplexLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.video.connectionentry.ConnectionEntryProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.contact.ContactProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.CustomBytesConverter;
import com.paulmandal.atak.libcotshrink.protobuf.CustomBytesExtConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.colorstroke.DetailStyleProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.DroppedFieldConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.link.FreehandLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.chat.hierarchy.group.contact.GroupContactProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.chat.hierarchy.group.GroupProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.HeightAndHeightUnitProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.chat.hierarchy.HierarchyProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.labelson.LabelsOnProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.linkattr.LinkAttrProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.model.ModelProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.routeinfo.navcue.NavCueProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.routeinfo.navcue.navcues.NavCuesProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.precisionlocation.PrecisionLocationProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.remarks.RemarksProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.routeinfo.RouteProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.sensor.SensorProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.serverdestination.ServerDestinationProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.link.ShapeLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.status.StatusProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.takv.TakvProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.tog.TogProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.track.TrackProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.routeinfo.navcue.trigger.TriggerProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.underscoredgroup.UnderscoreGroupProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.video.VideoProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac.FlowTagsProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac.MedevacProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac.MistProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac.MistsMapProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.EllipseLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.EllipseProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.geofence.GeoFenceProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.LineStyleProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.PolyStyleProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.ShapeProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.StyleProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.utils.PrecisionUtil;

import java.util.Calendar;

public class CotEventProtobufConverterFactory {
    public CotEventProtobufConverter createCotEventProtobufConverter() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startOfYearMs = cal.getTime().getTime();

        PrecisionUtil precisionUtil = new PrecisionUtil();

        return new CotEventProtobufConverter(
                new TakvProtobufConverter(),
                new TrackProtobufConverter(precisionUtil),
                new ServerDestinationProtobufConverter(),
                new RemarksProtobufConverter(),
                new ContactProtobufConverter(),
                new UnderscoreGroupProtobufConverter(),
                new CustomBytesConverter(),
                new CustomBytesExtConverter(),
                new ChatProtobufConverter(
                        new ChatGroupProtobufConverter(),
                        new HierarchyProtobufConverter(new GroupProtobufConverter(new GroupContactProtobufConverter()))),
                new LabelsOnProtobufConverter(),
                new PrecisionLocationProtobufConverter(),
                new DroppedFieldConverter(),
                new StatusProtobufConverter(),
                new HeightAndHeightUnitProtobufConverter(precisionUtil),
                new ModelProtobufConverter(),
                new DetailStyleProtobufConverter(),
                new CeHumanInputProtobufConverter(),
                new FreehandLinkProtobufConverter(),
                new ChatLinkProtobufConverter(),
                new ComplexLinkProtobufConverter(),
                new ShapeLinkProtobufConverter(precisionUtil),
                new RouteProtobufConverter(
                        new NavCuesProtobufConverter(
                                new NavCueProtobufConverter(
                                        new TriggerProtobufConverter()
                                )
                        ),
                        precisionUtil
                ),
                new LinkAttrProtobufConverter(),
                new TogProtobufConverter(),
                new SensorProtobufConverter(precisionUtil),
                new VideoProtobufConverter(
                        new ConnectionEntryProtobufConverter()
                ),
                new FlowTagsProtobufConverter(),
                new MedevacProtobufConverter(
                        new MistsMapProtobufConverter(
                                new MistProtobufConverter()
                        )
                ),
                new GeoFenceProtobufConverter(),
                new ShapeProtobufConverter(
                        new EllipseProtobufConverter(precisionUtil),
                        new EllipseLinkProtobufConverter(
                                new StyleProtobufConverter(
                                        new LineStyleProtobufConverter(),
                                        new PolyStyleProtobufConverter()
                                )
                        )
                ),
                precisionUtil,
                startOfYearMs);
    }
}

package com.paulmandal.atak.libcotshrink.protobuf.cotevent;

import com.paulmandal.atak.libcotshrink.protobuf.CeHumanInputProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ChatGroupProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ChatLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ChatProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ComplexLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ConnectionEntryProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ContactProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.CustomBytesConverter;
import com.paulmandal.atak.libcotshrink.protobuf.CustomBytesExtConverter;
import com.paulmandal.atak.libcotshrink.protobuf.DetailStyleProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.DroppedFieldConverter;
import com.paulmandal.atak.libcotshrink.protobuf.FreehandLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.GroupContactProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.GroupProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.HeightAndHeightUnitProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.HierarchyProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.LabelsOnProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.LinkAttrProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ModelProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.NavCueProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.NavCuesProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.PrecisionLocationProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.RemarksProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.RouteProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.SensorProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ServerDestinationProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.ShapeLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.StatusProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.TakvProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.TogProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.TrackProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.TriggerProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.UnderscoreGroupProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.VideoProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac.FlowTagsProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac.MedevacProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac.MistProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.medevac.MistsMapProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.EllipseLinkProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.EllipseProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.GeoFenceProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.LineStyleProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.PolyStyleProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.ShapeProtobufConverter;
import com.paulmandal.atak.libcotshrink.protobuf.cotevent.detail.shape.StyleProtobufConverter;

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

        return new CotEventProtobufConverter(
                new TakvProtobufConverter(),
                new TrackProtobufConverter(),
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
                new HeightAndHeightUnitProtobufConverter(),
                new ModelProtobufConverter(),
                new DetailStyleProtobufConverter(),
                new CeHumanInputProtobufConverter(),
                new FreehandLinkProtobufConverter(),
                new ChatLinkProtobufConverter(),
                new ComplexLinkProtobufConverter(),
                new ShapeLinkProtobufConverter(),
                new RouteProtobufConverter(
                        new NavCuesProtobufConverter(
                                new NavCueProtobufConverter(
                                        new TriggerProtobufConverter()
                                )
                        )
                ),
                new LinkAttrProtobufConverter(),
                new TogProtobufConverter(),
                new SensorProtobufConverter(),
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
                        new EllipseProtobufConverter(),
                        new EllipseLinkProtobufConverter(
                                new StyleProtobufConverter(
                                        new LineStyleProtobufConverter(),
                                        new PolyStyleProtobufConverter()
                                )
                        )
                ),
                startOfYearMs);
    }
}

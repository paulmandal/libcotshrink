//package com.paulmandal.atak.libcotshrink;
//
//import com.atakmap.coremap.cot.event.CotDetail;
//import com.atakmap.coremap.cot.event.CotEvent;
//import com.atakmap.coremap.cot.event.CotPoint;
//import com.atakmap.coremap.maps.time.CoordinatedTime;
//import com.paulmandal.atak.libcotshrink.pub.api.CotShrinker;
//import com.paulmandal.atak.libcotshrink.pub.api.CotShrinkerFactory;
//import com.paulmandal.atak.libcotshrink.utils.XmlComparer;
//
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
///**
// * Basic pinning tests for now
// */
//public class CotShrinkerTest {
//    @Test
//    public void testLossyPli() {
//        validateLossy("PLI", 193, "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='ANDROID-53af0912586418dc' type='a-f-G-U-C' time='2020-08-29T21:14:00.406Z' start='2020-08-29T21:14:00.406Z' stale='2020-08-29T21:15:15.406Z' how='h-e'><point lat='39.71401955573084' lon='-104.99452709918448' hae='1586.245787738948' ce='9999999.0' le='9999999.0'/><detail><takv os='29' version='4.0.0.7 (a457ad0d).1597850931-CIV' device='GOOGLE PIXEL 4 XL' platform='ATAK-CIV'/><contact endpoint='192.168.1.159:4242:tcp' callsign='dasuberdog'/><uid Droid='dasuberdog'/><precisionlocation altsrc='DTED2' geopointsrc='USER'/><__group role='Team Lead' name='Orange'/><status battery='58'/><track course='327.66875837972367' speed='0.0'/></detail></event>");
//    }
//
//    @Test
//    public void testLosslessPli() {
//        validateLossless("PLI", 193, "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='ANDROID-53af0912586418dc' type='a-f-G-U-C' time='2020-08-29T21:14:00.406Z' start='2020-08-29T21:14:00.406Z' stale='2020-08-29T21:15:15.406Z' how='h-e'><point lat='39.71401955573084' lon='-104.99452709918448' hae='1586.245787738948' ce='9999999.0' le='9999999.0'/><detail><takv os='29' version='4.0.0.7 (a457ad0d).1597850931-CIV' device='GOOGLE PIXEL 4 XL' platform='ATAK-CIV'/><contact endpoint='192.168.1.159:4242:tcp' callsign='dasuberdog'/><uid Droid='dasuberdog'/><precisionlocation altsrc='DTED2' geopointsrc='USER'/><__group role='Team Lead' name='Orange'/><status battery='58'/><track course='327.66875837972367' speed='0.0'/></detail></event>");
//    }
//
//    public void validateLossless(String messageType, int maxSize, String testXml) {
//        CotEvent cotEvent = CotEvent.parse(testXml);
//
//        CotShrinkerFactory cotShrinkerFactory = new CotShrinkerFactory();
//        CotShrinker cotShrinker = cotShrinkerFactory.createCotShrinker();
//        byte[] cotEventAsBytes = cotShrinker.toByteArray(cotEvent);
//        CotEvent convertedCotEvent = cotShrinker.toCotEvent(cotEventAsBytes);
//
//        XmlComparer xmlComparer = new XmlComparer();
//        boolean matched = xmlComparer.compareXmls(messageType, cotEvent.toString(), convertedCotEvent.toString());
//        assertTrue("Pre and post conversion did not match!", matched);
//        int size = cotEventAsBytes.length;
//        assertTrue("", size <= maxSize);
//        assertFalse(messageType + " size decreased from " + maxSize + " to " + size + "! NICEWORK, Update to the new max size: " + size, size < maxSize);
//    }
//
//    public void validateLossy(String messageType, int maxSize, String testXml) {
//        CotEvent cotEvent = CotEvent.parse(testXml);
//
//        CotShrinkerFactory cotShrinkerFactory = new CotShrinkerFactory();
//        CotShrinker cotShrinker = cotShrinkerFactory.createCotShrinker();
//        byte[] cotEventAsBytes = cotShrinker.toByteArrayLossy(cotEvent);
//        CotEvent convertedCotEvent = cotShrinker.toCotEvent(cotEventAsBytes);
//
//        // Wipe out known fudged fields
//        CoordinatedTime placeholderTime = new CoordinatedTime();
//        CotPoint cotPoint = new CotPoint(0.0, 0.0, 0.0, 0.0, 0.0);
//
//        cotEvent.setTime(placeholderTime);
//        cotEvent.setStart(placeholderTime);
//        cotEvent.setStale(placeholderTime);
//        cotEvent.setPoint(cotPoint);
//
//        convertedCotEvent.setTime(placeholderTime);
//        convertedCotEvent.setStart(placeholderTime);
//        convertedCotEvent.setStale(placeholderTime);
//        convertedCotEvent.setPoint(cotPoint);
//
//        CotDetail remarksDetail = cotEvent.getDetail().getFirstChildByName(0, "remarks");
//        CotDetail convertedRemarksDetail = convertedCotEvent.getDetail().getFirstChildByName(0, "remarks");
//        if (remarksDetail != null) {
//            remarksDetail.setAttribute("time", placeholderTime.toString());
//            convertedRemarksDetail.setAttribute("time", placeholderTime.toString());
//        }
//
//        CotDetail linkDetail = cotEvent.getDetail().getFirstChildByName(0, "link");
//        CotDetail convertedLinkDetail = convertedCotEvent.getDetail().getFirstChildByName(0, "link");
//        if (linkDetail != null && linkDetail.getAttribute("production_time") != null) {
//            linkDetail.setAttribute("production_time", placeholderTime.toString());
//            convertedLinkDetail.setAttribute("production_time", placeholderTime.toString());
//        }
//
//        List<CotDetail> removeable = new ArrayList<>();
//        for (CotDetail child : cotEvent.getDetail().getChildren()) {
//            if (child.getElementName().equals("archive")) {
//                removeable.add(child);
//            }
//        }
//
//        for (CotDetail remove : removeable) {
//            cotEvent.getDetail().removeChild(remove);
//        }
//
//        for (CotDetail child : cotEvent.getDetail().getChildren()) {
//            if (child.getElementName().equals("link") && child.getAttribute("remarks") != null) {
//                String[] uidSplit = child.getAttribute("uid").split("-");
//                child.setAttribute("uid", uidSplit[uidSplit.length - 1]);
//            }
//        }
//
//        XmlComparer xmlComparer = new XmlComparer();
//        boolean matched = xmlComparer.compareXmls(messageType, cotEvent.toString(), convertedCotEvent.toString());
//        assertTrue("Pre and post conversion did not match!", matched);
//        int size = cotEventAsBytes.length;
//        assertTrue("", size <= maxSize);
//        assertFalse(messageType + " size decreased from " + maxSize + " to " + size + "! NICEWORK, Update to the new max size: " + size, size < maxSize);
//    }
//}

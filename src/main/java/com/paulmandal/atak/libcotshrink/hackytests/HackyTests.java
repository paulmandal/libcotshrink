package com.paulmandal.atak.libcotshrink.hackytests;

import android.util.Log;

import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;
import com.atakmap.coremap.cot.event.CotPoint;
import com.atakmap.coremap.maps.time.CoordinatedTime;
import com.paulmandal.atak.libcotshrink.protobuf.Constants;
import com.paulmandal.atak.libcotshrink.pub.api.CotShrinker;
import com.paulmandal.atak.libcotshrink.pub.api.CotShrinkerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

// Tests to validate our protobufs
public class HackyTests {
    private static final String TAG = HackyTests.class.getSimpleName();

    private static final double LAT_LON_INT_CONVERSION_FACTOR = Constants.LAT_LON_INT_CONVERSION_FACTOR;
    private static final double COURSE_PRECISION_FACTOR = Constants.COURSE_PRECISION_FACTOR;
    private static final double SPEED_PRECISION_FACTOR = Constants.SPEED_PRECISION_FACTOR;
    private static final double HEIGHT_PRECISION_FACTOR = Constants.HEIGHT_PRECISION_FACTOR;
    private static final double ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR = Constants.ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR;

    public void runAllTests() {
        testPli();
        testPliWithNegativeHae();
        testPliWithZeroEndpointAddr();
        testComplexShape();
        testDrawnShape();
        testFreehand();
        testGroupChat();
        testOp();
        testPeerToPeerChat();
        testRoute();
        testSensor();
        testSimpleShape();
        testUserIcon();
        testWaypoint();
        testCasevacAllFields();
        testCasevacNoFields();
        testCircle();
        testGeoFenceWithAltitude();
        testGeoFenceWithoutAltitude();
    }

    public void testPli() {
        String messageType = "PLI";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='ANDROID-53af0912586418dc' type='a-f-G-U-C' time='2021-08-29T21:14:00.406Z' start='2021-08-29T21:14:00.406Z' stale='2021-08-29T21:15:15.406Z' how='h-e'><point lat='39.71401955573084' lon='-104.99452709918448' hae='1586.245787738948' ce='9999999.0' le='9999999.0'/><detail><takv os='29' version='4.0.0.7 (a457ad0d).1597850931-CIV' device='GOOGLE PIXEL 4 XL' platform='ATAK-CIV'/><contact endpoint='192.168.1.159:4242:tcp' callsign='dasuberdog'/><uid Droid='dasuberdog'/><precisionlocation altsrc='DTED2' geopointsrc='USER'/><__group role='Team Lead' name='Orange'/><status battery='58'/><track course='327.66875837972367' speed='0.0'/></detail></event>";
        validateLossy(messageType, 171, testXml);
        validateLossless(messageType, 545, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testPliWithNegativeHae() {
        String messageType = "PLI with negative HAE";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='ANDROID-53af0912586418dc' type='a-f-G-U-C' time='2021-08-29T21:14:00.406Z' start='2021-08-29T21:14:00.406Z' stale='2021-08-29T21:15:15.406Z' how='h-e'><point lat='39.71401955573084' lon='-104.99452709918448' hae='-800.0' ce='9999999.0' le='9999999.0'/><detail><takv os='29' version='4.0.0.7 (a457ad0d).1597850931-CIV' device='GOOGLE PIXEL 4 XL' platform='ATAK-CIV'/><contact endpoint='192.168.1.159:4242:tcp' callsign='dasuberdog'/><uid Droid='dasuberdog'/><precisionlocation altsrc='DTED2' geopointsrc='USER'/><__group role='Team Lead' name='Orange'/><status battery='58'/><track course='327.66875837972367' speed='0.0'/></detail></event>";
        validateLossy(messageType, 171, testXml);
        validateLossless(messageType, 543, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testPliWithZeroEndpointAddr() {
        String messageType = "PLI w 0.0.0.0 endpoint";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='ANDROID-53af0912586418dc' type='a-f-G-U-C' time='2021-08-29T21:14:00.406Z' start='2021-08-29T21:14:00.406Z' stale='2021-08-29T21:15:15.406Z' how='h-e'><point lat='39.71401955573084' lon='-104.99452709918448' hae='1586.245787738948' ce='9999999.0' le='9999999.0'/><detail><takv os='29' version='4.0.0.7 (a457ad0d).1597850931-CIV' device='GOOGLE PIXEL 4 XL' platform='ATAK-CIV'/><contact endpoint='0.0.0.0:4242:tcp' callsign='dasuberdog'/><uid Droid='dasuberdog'/><precisionlocation altsrc='DTED2' geopointsrc='USER'/><__group role='Team Lead' name='Orange'/><status battery='58'/><track course='327.66875837972367' speed='0.0'/></detail></event>";
        validateLossy(messageType, 166, testXml);
        validateLossless(messageType, 539, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testComplexShape() {
        String messageType = "Complex Shape";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='1da89ca4-9a57-4bba-b082-3793ebbd3ddb' type='overhead_marker' time='2021-08-29T21:25:54.877Z' start='2021-08-29T21:25:54.877Z' stale='2021-08-30T21:25:54.877Z' how='h-e'><point lat='39.72750837570107' lon='-104.98061468169054' hae='1597.252688001634' ce='9999999.0' le='9999999.0'/><detail><model name='Squad Car'/><track course='0.0'/><contact callsign='Squad Car'/><remarks>Do p</remarks><archive/><link uid='ANDROID-53af0912586418dc' production_time='2021-08-29T21:25:36.669Z' type='a-f-G-U-C' parent_callsign='dasuberdog' relation='p-p'/><labels_on value='false'/><height_unit>1</height_unit><height unit='meters' value='0.0'>0.0</height><precisionlocation altsrc='DTED2'/><color value='-16744704'/></detail></event>";
        validateLossy(messageType, 222, testXml);
        validateLossless(messageType, 575, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testDrawnShape() {
        String messageType = "Drawn Shape";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='52ce8003-8a24-41fd-8119-7e603ac1e95e' type='u-d-r' time='2021-08-29T21:31:25.109Z' start='2021-08-29T21:31:25.109Z' stale='2021-08-30T21:31:25.109Z' how='h-e'><point lat='39.730614458351475' lon='-104.99506083807847' hae='1588.394668173431' ce='9999999.0' le='9999999.0'/><detail><link point='39.734374690892466,-105.000951756657,1583.000100853604'/><link point='39.72561220672946,-104.9910075127075,1591.5484945254186'/><link point='39.72685459182151,-104.98916936139345'/><link point='39.735616232147535,-104.99911502806049'/><contact callsign='Rectangle 1'/><remarks>Nice</remarks><archive/><strokeColor value='-16744704'/><strokeWeight value='4.0'/><fillColor value='-1778352384'/><labels_on value='true'/><height_unit>4</height_unit><height unit='feet' value='0.9144000000000001'>0.9144000000000001</height><precisionlocation altsrc='DTED2'/><tog enabled='1'/></detail></event>";
        validateLossy(messageType, 229, testXml);
        validateLossless(messageType, 687, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testFreehand() {
        String messageType = "Freehand";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='30408da0-5e20-4eb5-a818-14dfe25d0be2' type='u-d-f-m' time='2021-08-29T21:51:23.406Z' start='2021-08-29T21:51:23.406Z' stale='2021-08-30T21:51:23.406Z' how='h-e'><point lat='0.0' lon='0.0' hae='9999999.0' ce='9999999.0' le='9999999.0' /><detail><link line='&lt;?xml version=&apos;1.0&apos; encoding=&apos;UTF-8&apos; standalone=&apos;yes&apos;?&gt;&lt;event version=&apos;2.0&apos; uid=&apos;88cde5f4-f5c6-4941-bf1e-0eb43d63ad6b&apos; type=&apos;u-d-f&apos; time=&apos;2021-08-29T21:51:23.406Z&apos; start=&apos;2021-08-29T21:51:23.406Z&apos; stale=&apos;2021-08-30T21:51:23.406Z&apos; how=&apos;h-e&apos;&gt;&lt;point lat=&apos;39.52891154613381&apos; lon=&apos;-105.75749869258964&apos; hae=&apos;3101.27767599931&apos; ce=&apos;9999999.0&apos; le=&apos;9999999.0&apos; /&gt;&lt;detail&gt;&lt;link point=&apos;39.52906154607542,-105.75732260632482&apos;/&gt;&lt;link point=&apos;39.52896154611435,-105.75762059846528&apos;/&gt;&lt;link point=&apos;39.52892972794492,-105.75766123375718&apos;/&gt;&lt;link point=&apos;39.52887063705884,-105.75763414356258&apos;/&gt;&lt;link point=&apos;39.52885245524774,-105.7575799631734&apos;/&gt;&lt;link point=&apos;39.52884336434219,-105.75736775664913&apos;/&gt;&lt;link point=&apos;39.52886154615329,-105.75730454619509&apos;/&gt;&lt;labels_on value=&apos;true&apos;/&gt;&lt;archive/&gt;&lt;strokeColor value=&apos;-16776961&apos;/&gt;&lt;strokeWeight value=&apos;4.0&apos;/&gt;&lt;contact callsign=&apos;Freehand 2&apos;/&gt;&lt;/detail&gt;&lt;/event&gt;'/><labels_on value='true'/><archive/><color value='-16744704'/><remarks>But</remarks><strokeColor value='-1'/><strokeWeight value='4.0'/><height_unit>4</height_unit><height unit='feet' value='0.9144000000000001'>0.9144000000000001</height><contact callsign='Freehand 2'/></detail></event>";
        validateLossy(messageType, 600, testXml);
        validateLossless(messageType, 958, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testGroupChat() {
        String messageType = "Group Chat";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='GeoChat.ANDROID-53af0912586418dc.okm.35f1f729-4107-4c84-8eb5-c04f3eac7701' type='b-t-f' time='2021-08-29T21:29:33.912Z' start='2021-08-29T21:29:33.912Z' stale='2021-08-30T21:29:33.912Z' how='h-g-i-g-o'><point lat='39.71401955573084' lon='-104.99452709918448' hae='1586.245787738948' ce='9999999.0' le='9999999.0'/><detail><__chat parent='UserGroups' groupOwner='true' chatroom='okm' id='b1367178-c68f-4f86-9753-4fd9a283f1c9' senderCallsign='dasuberdog'><chatgrp uid0='ANDROID-53af0912586418dc' uid1='ANDROID-355499060918435' id='b1367178-c68f-4f86-9753-4fd9a283f1c9'/><hierarchy><group uid='UserGroups' name='Groups'><group uid='b1367178-c68f-4f86-9753-4fd9a283f1c9' name='okm'><contact uid='ANDROID-53af0912586418dc' name='dasuberdog'/><contact uid='ANDROID-355499060918435' name='maya'/></group></group></hierarchy></__chat><link uid='ANDROID-53af0912586418dc' type='a-f-G-U-C' relation='p-p'/><remarks source='BAO.F.ATAK.ANDROID-53af0912586418dc' time='2021-08-29T21:29:33.912Z'>at VDO</remarks><__serverdestination destinations='192.168.1.159:4242:tcp:ANDROID-53af0912586418dc'/></detail></event>";
        validateLossy(messageType, 346, testXml);
        validateLossless(messageType, 723, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testOp() {
        String messageType = "OP";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='701e1268-3cee-44f9-b45b-8c7e000ef85d' type='b-m-p-s-p-op' time='2021-08-29T21:24:46.588Z' start='2021-08-29T21:24:46.588Z' stale='2021-08-29T21:29:46.588Z' how='h-g-i-g-o'><point lat='39.7265559318419' lon='-104.98673259921553' hae='1588.5983739602902' ce='198.5' le='9999999.0'/><detail><status readiness='true'/><archive/><contact callsign='dasuberdog.29.152430'/><remarks/><archive/><link uid='ANDROID-53af0912586418dc' production_time='2021-08-29T21:24:30.471Z' type='a-f-G-U-C' parent_callsign='dasuberdog' relation='p-p'/><precisionlocation altsrc='DTED2' geopointsrc='USER'/><ce_human_input>true</ce_human_input><color argb='-1'/></detail></event>";
        validateLossy(messageType, 194, testXml);
        validateLossless(messageType, 546, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testPeerToPeerChat() {
        String messageType = "P2P Chat";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='GeoChat.ANDROID-53af0912586418dc.maya.af7f7c7b-0577-403e-83c3-2eaf6e700d2e' type='b-t-f' time='2021-08-29T21:27:12.460Z' start='2021-08-29T21:27:12.460Z' stale='2021-08-30T21:27:12.460Z' how='h-g-i-g-o'><point lat='39.71401955573084' lon='-104.99452709918448' hae='1586.245787738948' ce='9999999.0' le='9999999.0'/><detail><__chat parent='RootContactGroup' groupOwner='false' chatroom='maya' id='ANDROID-355499060918435' senderCallsign='dasuberdog'><chatgrp uid0='ANDROID-53af0912586418dc' uid1='ANDROID-355499060918435' id='ANDROID-355499060918435'/></__chat><link uid='ANDROID-53af0912586418dc' type='a-f-G-U-C' relation='p-p'/><remarks source='BAO.F.ATAK.ANDROID-53af0912586418dc' to='ANDROID-355499060918435' time='2021-08-29T21:27:12.460Z'>you are a dog</remarks><__serverdestination destinations='192.168.1.159:4242:tcp:ANDROID-53af0912586418dc'/></detail></event>";
        validateLossy(messageType, 303, testXml);
        validateLossless(messageType, 642, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testRoute() {
        String messageType = "Route";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='3eb5f892-e44f-4605-9693-288815722690' type='b-m-r' time='2021-08-29T21:38:00.716Z' start='2021-08-29T21:38:00.716Z' stale='2021-08-30T21:38:00.716Z' how='h-e'><point lat='0.0' lon='0.0' hae='9999999.0' ce='9999999.0' le='9999999.0'/><detail><link uid='93798514-6c72-4e14-88ce-2288ad85370e' callsign='Route 7 SP' type='b-m-p-w' point='39.530711638779984,-105.76095833293527,3229.2090310525723' remarks='' relation='c'/><link uid='e0b082c8-a522-4661-9960-211b2d0f61cb' callsign='' type='b-m-p-c' point='39.53015542202335,-105.76000133390068,3199.4863308287886' remarks='' relation='c'/><link uid='ae39bb53-4bae-449a-b6c3-80dde5bce84d' callsign='' type='b-m-p-c' point='39.53091028762164,-105.75927125216293,3221.245650987822' remarks='' relation='c'/><link uid='23a5e2db-a00a-47b3-addd-450ff2982360' callsign='CP1' type='b-m-p-w' point='39.529834273062676,-105.75886674741635,3175.9541488988066' remarks='' relation='c'/><link uid='1d9bd771-708e-4793-a6d9-7e7d5dac9d18' callsign='' type='b-m-p-c' point='39.53053285482249,-105.75811364508328,3197.533593376215' remarks='' relation='c'/><link uid='7ac29e4e-a9df-463d-8aa3-3ae18aac0953' callsign='' type='b-m-p-c' point='39.53113873378954,-105.7575973260165,3206.9957389306155' remarks='' relation='c'/><link uid='bda0fba4-8e9d-46ca-8f95-a7bcdad2a7b6' callsign='CP2' type='b-m-p-w' point='39.53023488156001,-105.75715664604867,3178.162436818524' remarks='' relation='c'/><link uid='0ff20d3a-dcef-43a3-a4c5-3ef11ca0440d' callsign='' type='b-m-p-c' point='39.530456706099855,-105.75633777058607,3171.74040652843' remarks='' relation='c'/><link uid='8d633813-f589-4a5a-8cd1-0d61806b0c71' callsign='' type='b-m-p-c' point='39.531188395999955,-105.75618978104464,3183.9218981426966' remarks='' relation='c'/><link uid='2e81f497-651f-4fba-8b45-85d102c9d642' callsign='' type='b-m-p-c' point='39.53076461180442,-105.75506834918622,3140.0895940184528' remarks='' relation='c'/><link uid='37b4e06d-d8ab-4e7c-b6e1-5a689a8b04ff' callsign='TGT' type='b-m-p-w' point='39.52982102980656,-105.75470659697383,3097.669497337116' remarks='' relation='c'/><link_attr planningmethod='Infil' color='-16776961' method='Walking' prefix='CP' type='On Foot' stroke='3' direction='Infil' routetype='Primary' order='Ascending Check Points'/><labels_on value='false'/><__routeinfo><__navcues><__navcue voice='Speed Up' id='bda0fba4-8e9d-46ca-8f95-a7bcdad2a7b6' text='Speed Up'><trigger mode='d' value='70'/></__navcue><__navcue voice='Stop' id='23a5e2db-a00a-47b3-addd-450ff2982360' text='Stop'><trigger mode='d' value='70'/></__navcue></__navcues></__routeinfo><archive/><color value='-16776961'/><remarks>Butts</remarks><strokeColor value='-16776961'/><strokeWeight value='3.0'/><contact callsign='Route 7'/></detail></event>";
        validateLossy(messageType, 608, testXml);
        validateLossless(messageType, 1692, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testSensor() {
        String messageType = "Sensor";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='53dc0c04-d84a-473b-9128-f241350693ef' type='b-m-p-s-p-loc' time='2021-08-29T21:22:06.524Z' start='2021-08-29T21:22:06.524Z' stale='2021-09-15T22:02:35.452Z' how='h-g-i-g-o'><point lat='39.72268266' lon='-105.002122' hae='1580.88835768' ce='9999999.0' le='9999999.0'/><detail><status readiness='true'/><archive/><contact callsign='Sensor Bale'/><remarks>Hi</remarks><archive/><link uid='ANDROID-355499060918435' production_time='2021-08-29T21:18:50.381Z' type='a-f-G-U-C' parent_callsign='maya' relation='p-p'/><precisionlocation altsrc='DTED2'/><sensor fovGreen='0.0' fovBlue='0.0' fovRed='1.0' range='699' azimuth='78' displayMagneticReference='0' fov='130' hideFov='true' fovAlpha='0.44'/><color argb='-1'/><__video uid='efd8f175-9732-4a2a-a7dc-fc7f2c803ed2' url=':6636'><ConnectionEntry networkTimeout='13000' uid='efd8f175-9732-4a2a-a7dc-fc7f2c803ed2' path='' protocol='udp' bufferTime='3000' address='' port='6636' roverPort='-1' rtspReliable='0' ignoreEmbeddedKLV='false' alias='about name'/></__video></detail></event>";
        validateLossy(messageType, 285, testXml);
        validateLossless(messageType, 853, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testSimpleShape() {
        String messageType = "Simple Shape";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='10a20222-0678-49b8-b0cf-793d3340528b' type='a-h-G' time='2021-08-29T21:17:31.377Z' start='2021-08-29T21:17:31.377Z' stale='2021-08-29T21:22:31.377Z' how='h-g-i-g-o'><point lat='39.72871028603934' lon='-104.99686216622815' hae='1588.0681247063874' ce='30.0' le='9999999.0'/><detail><status readiness='true'/><archive/><contact callsign='Red Market'/><remarks>Remarks about these bitches</remarks><archive/><link uid='ANDROID-53af0912586418dc' production_time='2021-08-29T21:16:36.870Z' type='a-f-G-U-C' parent_callsign='dasuberdog' relation='p-p'/><height_unit>1</height_unit><height unit='meters' value='6.0'>6.0</height><precisionlocation altsrc='DTED2' geopointsrc='USER'/><ce_human_input>true</ce_human_input><color argb='-1'/><usericon iconsetpath='COT_MAPPING_2525B/a-h/a-h-G'/></detail></event>";
        validateLossy(messageType, 240, testXml);
        validateLossless(messageType, 656, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testUserIcon() {
        String messageType = "User Icon";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='eaa09ae4-50e0-4052-909a-9a5e3cbe905c' type='a-u-G-E-W' time='2021-08-29T21:26:41.476Z' start='2021-08-29T21:26:41.476Z' stale='2021-08-29T21:31:41.476Z' how='h-g-i-g-o'><point lat='39.73047153452518' lon='-104.97315124278549' hae='1607.5676218634023' ce='9999999.0' le='9999999.0'/><detail><status readiness='true'/><archive/><contact callsign='bombing 1'/><remarks>Lol lol</remarks><archive/><link uid='ANDROID-53af0912586418dc' production_time='2021-08-29T21:26:23.123Z' type='a-f-G-U-C' parent_callsign='dasuberdog' relation='p-p'/><precisionlocation altsrc='DTED2'/><color argb='-1'/><usericon iconsetpath='34ae1613-9645-4222-a9d2-e5f243dea2865/Military/bombing.png'/></detail></event>";
        validateLossy(messageType, 252, testXml);
        validateLossless(messageType, 582, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testWaypoint() {
        String messageType = "Waypoint";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='735fc2c5-2e8a-4e21-b793-a1bfef66fc28' type='b-m-p-w-GOTO' time='2021-08-29T21:22:45.171Z' start='2021-08-29T21:22:45.171Z' stale='2021-08-29T21:27:45.171Z' how='h-g-i-g-o'><point lat='39.72285198329387' lon='-104.99177830439078' hae='1594.3528328257194' ce='9999999.0' le='9999999.0'/><detail><status readiness='true'/><archive/><contact callsign='dasuberdog.29.152230'/><remarks/><archive/><link uid='ANDROID-53af0912586418dc' production_time='2021-08-29T21:22:30.997Z' type='a-f-G-U-C' parent_callsign='dasuberdog' relation='p-p'/><height_unit>4</height_unit><height unit='feet' value='0.0'>0.0</height><precisionlocation altsrc='DTED2'/><color argb='-1'/></detail></event>";
        validateLossy(messageType, 198, testXml);
        validateLossless(messageType, 552, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testCasevacAllFields() {
        String messageType = "Casevac w/ All Fields";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='5100a84f-0c90-4f53-8651-3af1637b8130' type='b-r-f-h-c' time='2021-09-03T02:55:39.593Z' start='2021-09-03T02:55:39.593Z' stale='2021-09-20T03:36:08.521Z' how='h-g-i-g-o'><point lat='39.65004002280003' lon='-105.09973608933406' hae='1670.374673893096' ce='9999999.0' le='9999999.0' /><detail><status readiness='false'/><archive/><contact callsign='calllsign'/><remarks></remarks><archive/><link uid='ANDROID-53af0912586418dc' production_time='2021-09-03T02:49:05.801Z' type='a-f-G-U-C' parent_callsign='dasuberdog' relation='p-p'/><precisionlocation altsrc='DTED2'/><color argb='-1'/><_flow-tags_ AndroidMedicalLine='2021-09-03T02:55:39.593Z'/><_medevac_ us_civilian='7' freq='67MHz' winds_are_from='nw' ambulatory='5' enemy='Enemy Troops, armed escort required' zone_prot_selection='0' title='title' hoist='true' us_military='6' nonus_military='8' security='3' routine='3' ventilator='true' equipment_other='true' marked_by='E - panel of lasers' urgent='1' terrain_other_detail='other terrain features' terrain_slope_dir='S' friendlies='some friends' terrain_rough='true' terrain_other='true' litter='4' nonus_civilian='9' priority='2' epw='10' terrain_loose='true' extraction_equipment='true' medline_remarks='remark' hlz_other='other marking' hlz_remarks='ready for evac' hlz_marking='4' terrain_slope='true' casevac='false' equipment_detail='other equipment' child='11'><zMistsMap><zMist s='Pulse Radial: Has Radial&#10;Airway: No Airway' t='aleve' i='Burn Deformity' z='zap 2' title='ZMIST2' m='Radiation'/><zMist s='Skin: Cold/Clammy&#10;Pulse Radial: Has Radial&#10;Bleeding: Massive' t='TQ, cric' i='Bleeding Puncture' z='zap number 1' title='ZMIST1' m='Frag Single Burn &gt; 20%'/></zMistsMap></_medevac_></detail></event>";
        validateLossy(messageType, 601, testXml);
        validateLossless(messageType, 1414, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testCasevacNoFields() {
        String messageType = "Casevac w/ Empty Fields";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='daf83b93-a7b2-4f75-8dc3-73a1da6ed5a4' type='b-r-f-h-c' time='2021-09-03T02:57:42.346Z' start='2021-09-03T02:57:42.346Z' stale='2021-09-20T03:38:11.274Z' how='h-g-i-g-o'><point lat='39.65004002280003' lon='-105.09973608933406' hae='1670.374673893096' ce='9999999.0' le='9999999.0' /><detail><status readiness='false'/><archive/><contact callsign='calllsign.1'/><remarks></remarks><archive/><link uid='ANDROID-53af0912586418dc' production_time='2021-09-03T02:57:22.725Z' type='a-f-G-U-C' parent_callsign='dasuberdog' relation='p-p'/><precisionlocation altsrc='DTED2'/><color argb='-1'/><_flow-tags_ AndroidMedicalLine='2021-09-03T02:57:42.349Z'/><_medevac_ medline_remarks='' equipment_none='true' terrain_none='true' casevac='false' zone_prot_selection='0' title='MED.2.205722'/></detail></event>";
        validateLossy(messageType, 236, testXml);
        validateLossless(messageType, 669, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testCircle() {
        String messageType = "Circle";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='df11ad17-79d8-436b-ba7f-6c12a7d147d3' type='u-d-c-c' time='2021-08-30T18:50:27.452Z' start='2021-08-30T18:50:27.452Z' stale='2021-08-31T18:50:27.452Z' how='h-e'><point lat='39.52907656661326' lon='-105.75665450279342' hae='3100.9553102169134' ce='9999999.0' le='9999999.0' /><detail><shape><ellipse major='10.557862958543073' minor='10.557862958543073' angle='360'/><link uid='df11ad17-79d8-436b-ba7f-6c12a7d147d3.Style' type='b-x-KmlStyle' relation='p-c'><Style><LineStyle><color>ff0000ff</color><width>4.0</width></LineStyle><PolyStyle><color>960000ff</color></PolyStyle></Style></link></shape><contact callsign='Drawing Circle 1'/><remarks></remarks><archive/><strokeColor value='-16776961'/><strokeWeight value='4.0'/><fillColor value='-1778384641'/><labels_on value='true'/><precisionlocation altsrc='DTED2'/></detail></event>";
        validateLossy(messageType, 209, testXml);
        validateLossless(messageType, 621, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testGeoFenceWithAltitude() {
        String messageType = "GeoFence w/ Altitude";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='07965d48-0d4b-4575-9c5b-c56a4b66ce10' type='u-d-r' time='2021-09-13T05:25:30.882Z' start='2021-09-13T05:25:30.882Z' stale='2021-09-14T05:25:30.882Z' how='h-e'><point lat='39.86657654739065' lon='-105.16496033254576' hae='1698.7690850953754' ce='9999999.0' le='9999999.0' /><detail><link point='39.93470375464395,-105.24931298523292,1743.6532212063455'/><link point='39.79887842819508,-105.24974402778368,1880.848705771484'/><link point='39.79843415916509,-105.08077499386248'/><link point='39.93425862060156,-105.08000930131891'/><tog enabled='1'/><strokeColor value='-16729857'/><strokeWeight value='3.4'/><fillColor value='-1778337537'/><remarks>Rmks</remarks><__geofence elevationMonitored='true' minElevation='0.7919789468754104' monitor='TAKUsers' trigger='Both' tracking='true' maxElevation='96.19437894687542' boundingSphere='85000.0'/><precisionlocation altsrc='DTED2'/><contact callsign='Warning'/><archive/><labels_on value='true'/></detail></event>";
        validateLossy(messageType, 258, testXml);
        validateLossless(messageType, 792, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void testGeoFenceWithoutAltitude() {
        String messageType = "GeoFence w/o Altitude";
        String testXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><event version='2.0' uid='07965d48-0d4b-4575-9c5b-c56a4b66ce10' type='u-d-r' time='2021-09-13T05:24:17.214Z' start='2021-09-13T05:24:17.214Z' stale='2021-09-14T05:24:17.214Z' how='h-e'><point lat='39.86657654739065' lon='-105.16496033254576' hae='1698.7690850953754' ce='9999999.0' le='9999999.0' /><detail><link point='39.93470375464395,-105.24931298523292,1743.6532212063455'/><link point='39.79887842819508,-105.24974402778368,1880.848705771484'/><link point='39.79843415916509,-105.08077499386248'/><link point='39.93425862060156,-105.08000930131891'/><tog enabled='1'/><strokeColor value='-16729857'/><strokeWeight value='3.4'/><fillColor value='-1778337537'/><remarks>Rmks</remarks><__geofence elevationMonitored='false' minElevation='NaN' monitor='All' trigger='Entry' tracking='false' maxElevation='NaN' boundingSphere='85000.0'/><precisionlocation altsrc='DTED2'/><contact callsign='Warning'/><archive/><labels_on value='true'/></detail></event>";
        validateLossy(messageType, 242, testXml);
        validateLossless(messageType, 761, testXml);
        generatePerformanceTableOutput(messageType, testXml);
    }

    public void generatePerformanceTableOutput(String messageType, String testXml) {
        CotEvent cotEvent = CotEvent.parse(testXml);

        CotShrinkerFactory cotShrinkerFactory = new CotShrinkerFactory();
        CotShrinker cotShrinker = cotShrinkerFactory.createCotShrinker();

        byte[] lossy = cotShrinker.toByteArrayLossy(cotEvent);
        byte[] lossless = cotShrinker.toByteArray(cotEvent);

        int originalSize = testXml.getBytes().length;
        int losslessSize = lossless.length;
        int lossySize = lossy.length;

        float losslessPercentReduction = (originalSize - losslessSize) / (float) originalSize * 100F;
        float lossyPercentReduction = (originalSize - lossySize) / (float) originalSize * 100F;
        Log.d(TAG + ".PerfTable", String.format("| %s | %4d | %4d (-%.2f%%) | %4d (-%.2f%%) |", messageType, originalSize, losslessSize, losslessPercentReduction, lossySize, lossyPercentReduction));
    }

    public void validateLossy(String messageType, int maxSize, String testXml) {
        CotEvent cotEvent = CotEvent.parse(testXml);

        CotShrinkerFactory cotShrinkerFactory = new CotShrinkerFactory();
        CotShrinker cotShrinker = cotShrinkerFactory.createCotShrinker();

        byte[] cotEventAsBytes = cotShrinker.toByteArrayLossy(cotEvent);
        CotEvent convertedCotEvent = cotShrinker.toCotEvent(cotEventAsBytes);

        // Reduce precision of known lossy fields
        CoordinatedTime fuzzedTime = cotEvent.getTime().addMilliseconds((int)(-1 * (cotEvent.getTime().getMilliseconds() % 1000)));
        CoordinatedTime fuzzedStart = cotEvent.getStart().addMilliseconds((int)(-1 * (cotEvent.getStart().getMilliseconds() % 1000)));
        CoordinatedTime fuzzedStale = cotEvent.getStale().addMilliseconds((int)(-1 * (cotEvent.getStale().getMilliseconds() % 1000)));

        CotPoint originalCotPoint = cotEvent.getCotPoint();
        CotPoint fuzzedCotPoint = new CotPoint(fuzzDouble(originalCotPoint.getLat(), LAT_LON_INT_CONVERSION_FACTOR), fuzzDouble(originalCotPoint.getLon(), LAT_LON_INT_CONVERSION_FACTOR), (int)originalCotPoint.getHae(), (int)originalCotPoint.getCe(), (int)originalCotPoint.getLe());

        cotEvent.setTime(fuzzedTime);
        cotEvent.setStart(fuzzedStart);
        cotEvent.setStale(fuzzedStale);
        cotEvent.setPoint(fuzzedCotPoint);

        CotDetail remarksDetail = cotEvent.getDetail().getFirstChildByName(0, "remarks");
        if (remarksDetail != null) {
            String timeStr = remarksDetail.getAttribute("time");
            if (timeStr != null) {
                remarksDetail.setAttribute("time", fuzzTimeFromString(timeStr));
            }
        }

        CotDetail linkDetail = cotEvent.getDetail().getFirstChildByName(0, "link");
        if (linkDetail != null && linkDetail.getAttribute("production_time") != null) {
            String timeStr = linkDetail.getAttribute("production_time");
            if (timeStr != null) {
                linkDetail.setAttribute("production_time", fuzzTimeFromString(timeStr));
            }
        }

        List<CotDetail> removeable = new ArrayList<>();
        for (CotDetail child : cotEvent.getDetail().getChildren()) {
            if (child.getElementName().equals("archive")) {
                removeable.add(child);
            }
        }

        for (CotDetail remove : removeable) {
            cotEvent.getDetail().removeChild(remove);
        }

        for (CotDetail child : cotEvent.getDetail().getChildren()) {
            if (child.getElementName().equals("link") && child.getAttribute("remarks") != null) {
                String[] uidSplit = child.getAttribute("uid").split("-");
                child.setAttribute("uid", uidSplit[uidSplit.length - 1]);
            }

            if (child.getElementName().equals("link") && child.getAttribute("point") != null) {
                String[] pointSplit = child.getAttribute("point").split(",");
                Double lat = fuzzDouble(Double.parseDouble(pointSplit[0]), LAT_LON_INT_CONVERSION_FACTOR);
                Double lon = fuzzDouble(Double.parseDouble(pointSplit[1]), LAT_LON_INT_CONVERSION_FACTOR);
                String newPoint = lat + "," + lon;
                if (pointSplit.length == 3) {
                     newPoint = newPoint + "," + pointSplit[2];
                }
                child.setAttribute("point", newPoint);
            }

            if (child.getElementName().equals("track") && child.getAttribute("speed") != null && child.getAttribute("course") != null) {
                String speedStr = child.getAttribute("speed");
                String courseStr = child.getAttribute("course");

                child.setAttribute("speed", Double.toString(fuzzDouble(Double.parseDouble(speedStr), SPEED_PRECISION_FACTOR)));
                child.setAttribute("course", Double.toString(fuzzDouble(Double.parseDouble(courseStr), COURSE_PRECISION_FACTOR)));
            }

            if (child.getElementName().equals("height") && child.getAttribute("value") != null) {
                String valueStr = child.getAttribute("value");

                String heightText = Double.toString(fuzzDouble(Double.parseDouble(valueStr), HEIGHT_PRECISION_FACTOR));
                child.setAttribute("value", heightText);
                child.setInnerText(heightText);
            }

            if (child.getElementName().equals("shape")) {
                fuzzShape(child);
            }
        }

        validateXmls(messageType, maxSize, testXml.getBytes().length, cotEventAsBytes.length, cotEvent.toString(), convertedCotEvent.toString());
    }

    public void validateLossless(String messageType, int maxSize, String testXml) {
        CotEvent cotEvent = CotEvent.parse(testXml);

        CotShrinkerFactory cotShrinkerFactory = new CotShrinkerFactory();
        CotShrinker cotShrinker = cotShrinkerFactory.createCotShrinker();

        byte[] cotEventAsBytes = cotShrinker.toByteArray(cotEvent);
        CotEvent convertedCotEvent = cotShrinker.toCotEvent(cotEventAsBytes);

        validateXmls(messageType, maxSize, testXml.getBytes().length, cotEventAsBytes.length, cotEvent.toString(), convertedCotEvent.toString());
    }

    public void validateXmls(String messageType, int maxSize, int originalSize, int size, String lhs, String rhs) {
        XmlComparer xmlComparer = new XmlComparer();
        boolean matched = xmlComparer.compareXmls(messageType, lhs, rhs);
        if (!matched) {
            Log.e(TAG, "Pre and post conversion payloads did not match!");
            Log.e(TAG, "lhs: " + lhs);
            Log.e(TAG, "rhs: " + rhs);
        }

        Log.d(TAG, messageType + ", original size: " + originalSize + ", shrunk size: " + size + ", expected: " + maxSize);
        if (size > maxSize) {
            Log.e(TAG, messageType + " size increased past previous value: " + maxSize + ", now: " + size + ", pre-shrink: " + originalSize);
        } else if (size < maxSize) {
            Log.e(TAG, "!!!!!!!!!!!!");
            Log.e(TAG, messageType + " size decreased from " + maxSize + " to " + size + "! Update to the new max size: " + size);
            Log.e(TAG, "!!!!!!!!!!!!");
        }
    }

    private void fuzzShape(CotDetail shapeDetail) {
        for (CotDetail child : shapeDetail.getChildren()) {
            if (child.getElementName().equals("ellipse") && child.getAttribute("major") != null && child.getAttribute("minor") != null) {
                String majorStr = child.getAttribute("major");
                String minorStr = child.getAttribute("minor");

                child.setAttribute("major", Double.toString(fuzzDouble(Double.parseDouble(majorStr), ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR)));
                child.setAttribute("minor", Double.toString(fuzzDouble(Double.parseDouble(minorStr), ELLIPSE_MAJOR_MINOR_PRECISION_FACTOR)));
            }
        }
    }

    private String fuzzTimeFromString(String timeStr) {
        try {
            CoordinatedTime time = CoordinatedTime.fromCot(timeStr);
            return time.addMilliseconds((int)(-1 * (time.getMilliseconds() % 1000))).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return new CoordinatedTime().toString();
        }
    }

    private double fuzzDouble(double d, double factor) {
        return ((int)(d * factor)) / factor;
    }
}

libcotshrink takes `CotEvent` instances from ATAK and turns them into minimally sized byte arrays.

It uses a combination of EXI (Efficient XML Interchange), Protobufs, and GZip.

# Adding to your Project

libcotshrink is now available on JCenter

Edit your `app/build.gradle` and add the library to your app/plugin's dependencies:

```groovy
dependencies {
    ...

    implementation 'com.paulmandal.atak:libcotshrink:0.1.0'
}

```

Run Gradle Sync and you will have access to libcotshrink's classes.

# API / Usage

The `CotShrinker` class contains the libcotshrink API. It has lossless and lossy variants:

`CotShrinker.toByteArray(CotEvent)` - Losslessly converts a `CotEvent` to a byte array, currently this uses EXI (Efficient XML Interchange)

`CotShrinker.toByteArrayLossy(CotEvent)` - Lossy conversion for a `CotEvent` to a byte array, uses custom protobufs. This will fall back to `toByteArray(CotEvent)` if the custom protobuf conversion fails.

`CotShrinker.toCotEvent(byte[])` - Will attempt to convert back to a `CotEvent`, first using the lossy conversion and then using the lossless if that one fails.

Example usage:

```java
CotEvent cotEvent = ...;
CotShrinkerFactory cotShrinkerFactory = new CotShrinkerFactory();
CotShrinker cotShrinker = cotShrinkerFactory.createCotShrinker();

// Convert to bytes
byte[] cotAsBytes = cotShrinker.toByteArray(cotEvent);
byte[] cotAsLossyBytes = cotShrinker.toByteArrayLossy(cotEvent);

// Convert back
CotEvent cotFromBytes = cotShrinker.toCotEvent(cotAsBytes);
CotEvent cotFromLossyBytes = cotShrinker.toCotEvent(cotAsLossyBytes);
```

# Lossy Conversion Details (WIP)

Using the `CotShrinker.toByteArrayLossy(CotEvent)` method will cause loss of precision in the following ways:

| Field       | Description                          |
| ----------- | ------------------------------------ |
| `time`      | to whole seconds since start of year |
| `stale`     | to whole seconds since `time`        |
| `start`     | reuses valuefrom `time`              |
| all `lat`   | to 7 decimal places                  |
| all `lon`   | to 7 decimal places                  |
| `point.hae` | to 14 bit `int`, whole meters        |
| `point.ce`  | to `int` from `double`               |
| `point.le`  | to `int` from `double`               |

# Contributing / TODO

* Figure out how to get unit tests working, they rely `CotEvent.parse()` but this does not seem to work during unit tests
* Use `sint32` instead of `int32` for fields that can be negative
* Support more message types (please identify them and how they are generated in Issues)
* Make more fields into enums (e.g. `CotEvent.type`)
* Create detail subobjects for each set of detail fields used by a specific message type, e.g. the detail field should have objs like GeoChatDetail, PliDetail, etc. that are composed of the existing detail fields. This MAY save some space, need to test with PLI or something like that first.
* Shrink messages further
* Use Dagger 2

# Performance Stats

| Message Type            | Original Size | Lossless Size  | Lossy Size    |
|-------------------------|---------------|----------------|---------------|
| PLI                     | 731           | 568 (-22.30%)  | 193 (-73.60%) |
| PLI w 0.0.0.0 endpoint  | 725           | 562 (-22.48%)  | 188 (-74.07%) |
| Complex Shape           | 800           | 598 (-25.25%)  | 226 (-71.75%) |
| Drawn Shape             | 963           | 711 (-26.17%)  | 264 (-72.59%) |
| Freehand                | 1867          | 958 (-48.69%)  | 606 (-67.54%) |
| Group Chat              | 1181          | 746 (-36.83%)  | 352 (-70.19%) |
| OP                      | 735           | 569 (-22.59%)  | 200 (-72.79%) |
| P2P Chat                | 951           | 665 (-30.07%)  | 309 (-67.51%) |
| Route                   | 2826          | 1680 (-40.55%) | 667 (-76.40%) |
| Sensor                  | 1106          | 859 (-22.33%)  | 305 (-72.42%) |
| Simple Shape            | 881           | 678 (-23.04%)  | 252 (-71.40%) |
| User Icon               | 770           | 606 (-21.30%)  | 258 (-66.49%) |
| Waypoint                | 756           | 577 (-23.68%)  | 204 (-73.02%) |
| Casevac w/ All Fields   | 1810          | 1438 (-20.55%) | 609 (-66.35%) |
| Casevac w/ Empty Fields | 876           | 694 (-20.78%)  | 242 (-72.37%) |
| Circle                  | 912           | 645 (-29.28%)  | 222 (-75.66%) |
| GeoFence w/ Altitude    | 1040          | 816 (-21.54%)  | 284 (-72.69%) |
| GeoFence w/o Altitude   | 1009          | 785 (-22.20%)  | 269 (-73.34%) |

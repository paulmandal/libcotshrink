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

| Message Type            | Original Size | Lossless Size  | Lossy Size     |
|-------------------------|---------------|----------------|----------------|
| PLI                     |           731 |  545 (-25.44%) |  171 (-76.61%) |
| PLI with negative HAE   |           720 |  543 (-24.58%) |  171 (-76.25%) |
| PLI w 0.0.0.0 endpoint  |           725 |  539 (-25.66%) |  166 (-77.10%) |
| Complex Shape           |           800 |  575 (-28.13%) |  222 (-72.25%) |
| Drawn Shape             |           963 |  687 (-28.66%) |  212 (-77.99%) |
| Freehand                |          1867 |  958 (-48.69%) |  600 (-67.86%) |
| Group Chat              |          1181 |  723 (-38.78%) |  346 (-70.70%) |
| OP                      |           735 |  546 (-25.71%) |  194 (-73.61%) |
| P2P Chat                |           951 |  642 (-32.49%) |  303 (-68.14%) |
| Route                   |          2826 | 1692 (-40.13%) |  544 (-80.75%) |
| Sensor                  |          1106 |  853 (-22.88%) |  285 (-74.23%) |
| Simple Shape            |           881 |  656 (-25.54%) |  240 (-72.76%) |
| User Icon               |           770 |  582 (-24.42%) |  252 (-67.27%) |
| Waypoint                |           756 |  552 (-26.98%) |  198 (-73.81%) |
| Casevac w/ All Fields   |          1810 | 1414 (-21.88%) |  601 (-66.80%) |
| Casevac w/ Empty Fields |           876 |  669 (-23.63%) |  236 (-73.06%) |
| Circle                  |           912 |  621 (-31.91%) |  209 (-77.08%) |
| GeoFence w/ Altitude    |          1040 |  792 (-23.85%) |  222 (-78.65%) |
| GeoFence w/o Altitude   |          1009 |  761 (-24.58%) |  217 (-78.49%) |

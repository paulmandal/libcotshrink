libcotshrink takes `CotEvent` instances from ATAK and turns them into minimally sized byte arrays.

# Adding to your Project

This module expects to be side-by-side in the same parent directory as the `AndroidTacticalAssaultKit-CIV` directory (ATAK source)
  OR in a project that is side-by-side with the ATAK source. Building will fail if it is not.
  
Add the module repo to your project:

`git submodule add git@github.com:paulmandal/libcotshrink.git`

Edit your `settings.gradle` and add the module:

```
include ':libcotshrink'
project(':libcotshrink').projectDir = new File('libcotshrink')
```

Edit your `app/build.gradle` and add the module to your app/plugin's dependencies:

```
dependencies {
    ...

    implementation project(':libcotshrink')
}

```

Run Gradle Sync and you will have access to libcotshrink's classes.

# API / Usage

The `CotShrinker` class contains the libcotshrink API. It has lossless and lossy variants:

`CotShrinker.toByteArray(CotEvent)` - Losslessly converts a `CotEvent` to a byte array, currently this uses EXI (Efficient XML Interchange)

`CotShrinker.toByteArrayLossy(CotEvent)` - Lossy conversion for a `CotEvent` to a byte array, uses custom protobufs. This will fall back to `toByteArray(CotEvent)` if the custom protobuf conversion fails.

`CotShrinker.toCotEvent(byte[])` - Will attempt to convert back to a `CotEvent`, first using the lossy conversion and then using the lossless if that one fails.

Example usage:

```
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
| `point.hae` | to 14 bit `int`, whole meters        |
| `point.ce`  | to `int` from `double`               |
| `point.le`  | to `int` from `double`               |

# Contributing / TODO

* Figure out how to get unit tests working, they rely on the ATAK lib but it doesn't seem to work during testing
* Improve structure of Cot*ProtobufConverter classes, there should be a reasonable directory tree there
* Shrink messages further

# Performance Stats

| Message Type            | Original Size | Lossless Size  | Lossy Size    |
|-------------------------|---------------|----------------|---------------|
| PLI                     | 731           | 568 (-22.30%)  | 193 (-73.60%) |
| PLI w 0.0.0.0 endpoint  | 725           | 562 (-22.48%)  | 188 (-74.07%) |
| Complex Shape           | 800           | 598 (-25.25%)  | 226 (-71.75%) |
| Drawn Shape             | 963           | 711 (-26.17%)  | 273 (-71.65%) |
| Freehand                | 1867          | 1286 (-31.12%) | 606 (-67.54%) |
| Group Chat              | 1181          | 746 (-36.83%)  | 355 (-69.94%) |
| OP                      | 735           | 569 (-22.59%)  | 200 (-72.79%) |
| P2P Chat                | 951           | 665 (-30.07%)  | 343 (-63.93%) |
| Route                   | 2826          | 1754 (-37.93%) | 887 (-68.61%) |
| Sensor                  | 1106          | 859 (-22.33%)  | 305 (-72.42%) |
| Simple Shape            | 881           | 680 (-22.81%)  | 252 (-71.40%) |
| User Icon               | 770           | 606 (-21.30%)  | 258 (-66.49%) |
| Waypoint                | 756           | 577 (-23.68%)  | 204 (-73.02%) |
| Casevac w/ All Fields   | 1810          | 1476 (-18.45%) | 696 (-61.55%) |
| Casevac w/ Empty Fields | 876           | 694 (-20.78%)  | 242 (-72.37%) |
| Circle                  | 912           | 645 (-29.28%)  | 256 (-71.93%) |
| GeoFence w/ Altitude    | 1040          | 816 (-21.54%)  | 294 (-71.73%) |
| GeoFence w/o Altitude   | 1009          | 785 (-22.20%)  | 290 (-71.26%) |

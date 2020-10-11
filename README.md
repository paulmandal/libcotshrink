libcotshrink takes `CotEvent` instances from ATAK and turns them into minimally sized byte arrays

# Usage

The `CotShrinker` class contains the libcotshrink API. It has lossless and lossy variants:

`CotShrinker.cotEventToBytes(CotEvent)` - Losslessly converts a CotEvent to a byte array, currently this uses EXI (Efficient XML Interchange)
`CotShrinker.cotEventToBytesLossy(CotEvent)` - Lossy conversion for a CotEvent to a byte array, uses custom protobufs. This will fall back to `cotEventToBytes(CotEvent)` if the custom protobuf conversion fails.

`CotShrinker.cotEventFromBytes(byte[])` - Will attempt to convert from a lossy and then lossless TODO: better explanation

Both methods also use GZip if it results in a reduced package size.

```
CotEvent cotEvent = ...;
CotShrinker cotShrinker = CotShrinkerFactory.getCotShrinker();

// Convert to bytes
byte[] cotAsBytes = cotShrinker.cotEventToBytes(cotEvent);
byte[] lossyCotAsBytes = cotShrinker.cotEventToBytesLossy(cotEvent);

// Convert back
CotEvent cotFromBytes = cotShrinker.cotEventFromBytes(cotAsBytes);
```

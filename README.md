# Decompression Planer (ZHL-16C)

## Disclaimer

I publish this in hope for review and comments. At the current state (2021-05-18) please do not base your planning on 
the given algorithm. If you still do so this would proof Darwin right and eventually lead to natural selection.

## Features

* dive configuration (altitude, type of water, ascent speed)
* plan configuration (depth start to end, duration and used gas)  
* gas configuration (Air, Nitrox and pO2)
* decompression calculation based on ZHL-16C and gradient factors
* gas switch calculation based on gas and pO2
* calculation of OTU and CNS

## Warning!

Trimix can be configured as gas and used for dive plan. Please DO NOT use Trimix as decompression gas 
because calculation no correct. 
  
## Help Wanted

If you are a Trimix diver and know how to auto-select the perfect decompression gas please contact me, so I can understand
how to better implement the feature.
  
## How To

Please refer to [`Main.kt`](./src/Main.kt)


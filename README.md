# SphereLink

## Known Issues:
[qr code scanner issue and fix]
[Could be optimized for batter life]
[persistant permission request after permission has been granted]

## Description:
The purpose of this app is to track distances of BLE devices.  [why did we create this, what s it for?] [connects to many devices by scanning barcodes]

## Architecure and Design

[keywords to use MVVM, room data base?, kotlin, jetpack compose]
[MVVM design used with navigation for implementation of UI]
[Give details about how distance is calculated]
[How often is distance calculated, how often are rssi values taken? how many?]
[3 database tables and there purpose]
[General Overview of the classes maybe?]
[Brief over view of the classes in the domain path]

[focus on describing the classes in the bluetooth and distance paths. Note distance calculation does more than the name. probably should be renamed]

## ESP32 Device

[Add description give details about the battery service, currently remember this value is random and not actual batter life, also this isnt being updated properly. Only upon connect]

Repo:
https://github.com/twobit-five/esp32-Device

![thumbnail_image009](https://user-images.githubusercontent.com/69398054/236332452-cde3c514-0e89-426b-ad6a-32610d4ee51c.jpg)

## Device List Screen

[Add Description of this screen and screenshot]
[what do the buttons do?]

## Device Details Screen

[Add Description of this screen and screenshot]

## Add Device Screen

[Add Description of this screen and screenshot]

### QR code Scanner [Section]

[choose to use the qr scanner libray due to ease of implementation]
[less permission since google handles it]

## Future Work
[implement actual battery levels]
[optimize batter]
[bonded device logic for faster reconnects and encryption during transmission]
[Bad wording but, setting preferred phy on connected devices to LE Coded, gives better reliability and greater distances at the cost of trasmission rate]
[improving calculation by some how using trianglution or possibly feature from ble 5.1 AoA and AoD]
[Notification System]
[Settings file for customization, distance threshold, interval, delay between readings etc]
[Additional service which could help us in determining the calibruated vale for base rssi]
[]

## Pictures (Remove after moving pictures to sections)
![thumbnail_image006](https://user-images.githubusercontent.com/69398054/236332346-4f54a794-3ea2-4d9d-b8fb-05ef127b3c41.jpg)
![Screenshot_20230504-161833_SphereLink](https://user-images.githubusercontent.com/69398054/236332669-12320b43-b5c8-4b6c-9c80-56518b51c4a8.jpg)
![thumbnail_image010](https://user-images.githubusercontent.com/69398054/236332464-c287622b-c1a4-44c5-9997-e70ccd5d0dca.jpg)
![thumbnail_image011](https://user-images.githubusercontent.com/69398054/236332481-c5f140a8-96e0-4177-8039-48e507707a7a.jpg)



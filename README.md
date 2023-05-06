# SphereLink

## Known Issues:
[qr code scanner issue and fix]
[can we shorten the display text of the link?]

The google code Scanner:

https://nam02.safelinks.protection.outlook.com/?url=https%3A%2F%2Fdevelopers.google.com%2Fml-kit%2Fvision%2Fbarcode-scanning%2Fcode-scanner%23%3A~%3Atext%3DAdd%2520the%2520Google%2520Play%2520services%2520dependency%2520for%2520the%2Cis%2520commonly%2520app%252Fbuild.gradle%253A%2520dependencies%2520%257B%2520implementation%2520%2527com.google.android.gms%253Aplay-services-code-scanner%253A16.0.0%2527%2520%257D&data=05%7C01%7Cjeremyberry%40mail.umkc.edu%7C52a29a4ae0c1454b8c6908db4dad365b%7Ce3fefdbef7e9401ba51a355e01b05a89%7C0%7C0%7C638189178605862338%7CUnknown%7CTWFpbGZsb3d8eyJWIjoiMC4wLjAwMDAiLCJQIjoiV2luMzIiLCJBTiI6Ik1haWwiLCJXVCI6Mn0%3D%7C3000%7C%7C%7C&sdata=F9CpiNSe1eVckMxSxf6q1YyqF1oKR8qxi51Vexb9L3k%3D&reserved=0

Issue:
https://nam02.safelinks.protection.outlook.com/?url=https%3A%2F%2Fissuetracker.google.com%2Fissues%2F261579118%3Fpli%3D1&data=05%7C01%7Cjeremyberry%40mail.umkc.edu%7C52a29a4ae0c1454b8c6908db4dad365b%7Ce3fefdbef7e9401ba51a355e01b05a89%7C0%7C0%7C638189178605862338%7CUnknown%7CTWFpbGZsb3d8eyJWIjoiMC4wLjAwMDAiLCJQIjoiV2luMzIiLCJBTiI6Ik1haWwiLCJXVCI6Mn0%3D%7C3000%7C%7C%7C&sdata=FrfdQZDZtcUqOE2w0jthhWZnq0cMEbB9mXbkjCCZbCk%3D&reserved=0

How to clear data from an app: (If you experience the issue): 
https://nam02.safelinks.protection.outlook.com/?url=https%3A%2F%2Fsupport.google.com%2Fgoogleplay%2Fanswer%2F9037938%3Fhl%3Den&data=05%7C01%7Cjeremyberry%40mail.umkc.edu%7C52a29a4ae0c1454b8c6908db4dad365b%7Ce3fefdbef7e9401ba51a355e01b05a89%7C0%7C0%7C638189178605862338%7CUnknown%7CTWFpbGZsb3d8eyJWIjoiMC4wLjAwMDAiLCJQIjoiV2luMzIiLCJBTiI6Ik1haWwiLCJXVCI6Mn0%3D%7C3000%7C%7C%7C&sdata=NT0o%2FktBL0WJ6yR4MbV3Cslo%2BdZeOIHtegR4dhLHI%2Fs%3D&reserved=0

[Could be optimized for batter life]
[persistant permission request after permission has been granted]
[connection issues if device has some how become bonded, dont bond the device!]

[occasional bluetooth connection issues, try restarting esp32. if doesnt work during testing some bluetooth component crashed and had to restart phone to fix.]

## Notification Service [has a bug, waiting to resolve prior to creating pull request.]
A notification service is added to notify the user in case the connected BLE device goes beyond certain threshold distance. The service take a threshold distance, say 10 meters, if the esp32 device goes further than 10 meters the service will send a notification/alert stating that the device is gone beyond 10 meters.

Verification the service properly is started:
![WhatsApp Image 2023-05-05 at 6 47 06 PM](https://user-images.githubusercontent.com/112286488/236586060-5e330028-3db5-4eb0-8de1-b370e511ca4b.jpeg)

Branch: (verify this branch)
https://github.com/twobit-five/SphereLink/tree/imran_master

May eliminate additional service and call logic as distances are calculated from the DistantCalculator class to create notification directly.

## Description:
The purpose of this app is to track distances of BLE devices.  [why did we create this, what s it for?] [connects to many devices by scanning barcodes]
## Architecure and Design

[include Avas drawing for the phone architecture]

[one to many design. ie one phone to many esp32 devices.]

[keywords to use MVVM, room data base?, kotlin, jetpack compose, dagger hilt dependency injection]
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

[Add Description of this screen and screenshot]
[choose to use the qr scanner libray due to ease of implementation]
[less permission since google handles it]

## Future Work
[implement actual battery levels]
[optimize batter]
[add bonded device logic for faster reconnects and encryption during transmission]
[Bad wording but, setting preferred phy on connected devices to LE Coded (on both ends), gives better reliability and greater distances at the cost of trasmission rate]
[improving calculation by some how using trianglution or possibly feature from ble 5.1 AoA and AoD]
[Notification System]
[Settings Ui for customization of, distance threshold, interval, delay between readings etc]
[Additional service which could help us in determining the calibruated vale for base rssi]
[implemting unit tests]
[better looking ui to present data]
[Add rssi stat graphs for additional details from the device details screen]
[Add graph which visualizes device ranges from main screen. ie device can be viewed in list or in a visual way.]

## Test Results:

[Describe testing enviroment]

15 ft - (4.572)

![thumbnail_image](https://user-images.githubusercontent.com/69398054/236593153-b29ac1c2-2fdf-4563-a3af-4db9bf240ea9.png)

45ft - (13.7m)

![thumbnail_image](https://user-images.githubusercontent.com/69398054/236593166-fe0f708f-d926-4956-bd28-ecf244ab164c.png)

75ft - (22.86m)

![thumbnail_image](https://user-images.githubusercontent.com/69398054/236593184-5192ebaa-1df7-4a3e-9b64-2f0d6d720bee.png)

90ft - (27.432m)

![thumbnail_image](https://user-images.githubusercontent.com/69398054/236593195-bc307c37-0484-40f3-bbcb-4ff771564a15.png)


## Some of the most resources for our Inspiration and Credits

MVVM Architecure:
https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=4636s

Notifications:
https://www.youtube.com/watch?v=LP623htmWcI&t=1285s

Dependency Injection:
https://www.youtube.com/watch?v=bbMsuI2p1DQ&t=423s

Resolution to dependency Issue (Beginning of Project):
https://stackoverflow.com/questions/75883656/dependency-issue-with-android-project

Permission Handler:
https://www.youtube.com/watch?v=D3JCtaK8LSU&t=1066s

And many more...

[add others as they are rediscovered]



## Pictures (Remove after moving pictures to sections)
![thumbnail_image006](https://user-images.githubusercontent.com/69398054/236332346-4f54a794-3ea2-4d9d-b8fb-05ef127b3c41.jpg)
![Screenshot_20230504-161833_SphereLink](https://user-images.githubusercontent.com/69398054/236332669-12320b43-b5c8-4b6c-9c80-56518b51c4a8.jpg)
![thumbnail_image010](https://user-images.githubusercontent.com/69398054/236332464-c287622b-c1a4-44c5-9997-e70ccd5d0dca.jpg)
![thumbnail_image011](https://user-images.githubusercontent.com/69398054/236332481-c5f140a8-96e0-4177-8039-48e507707a7a.jpg)



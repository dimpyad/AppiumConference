
This demo was prepared as per for #AppiumConference2021 presenatation By Dimpy Adhikary / Rashmi Konda - UIAutomator2 & Espresso drivers - The Perfect Duo for MQTT (IoT)

Apps used as part of the demo:
------------------------------
1) Deshboard App - https://github.com/ravendmaster/linear-mqtt-dashboard  (we need debug app here to be access by Appium Espresso driver)
2) Publisher App - https://play.google.com/store/apps/details?id=com.app.vetru.mqttdashboard&hl=en_IN&gl=US
3) Alert App - https://play.google.com/store/apps/details?id=gigiosoft.MQTTAlert&hl=en_IN&gl=US

Prerequisites:
-------------
Code:
   Java
   Appium
   Junit

Apps:
Deshboard App
--------------
1) Build the Desktop app listed above using Android Studio
2) Add the broker details (we used HiveMQ public broker for the demo - https://www.hivemq.com/public-mqtt-broker/ 
3) Add panels as per the tests to be performed.
   Note: We have provided the debug app which can be used directly in this repository with default panel added and default broker setting preconfigured.

Publisher App:
-------------
1) Add broker details (example : HiveMQ broker configuration)
2) Add Two tiles one for SwitchOn (payload 1) and one for SwitchOff (Payload 0) - those will be used to publish messages on a topic (example: hall/switch)

AlertApp:
---------
1) Add broker details (example : HiveMQ broker configuration)
2) Add a alert with payload 1

Execution Steps:
----------------
1) Prepare two devices one for the Deshboard app (debug App) and another one for the Publisher App and Alert App
2) Run the file MQTT.java as junit tests

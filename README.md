# LED-Phone-Notifications

This is a fairly simple project that I started to further my understanding in how Bluetooth connections work.

The python program is set to run on startup on a Raspberry Pi 3b. The Raspberry Pi becomes a host for a Bluetooth connection, in which it then waits for data to be sent from an Android phone that is running the accompanying app with information based on the notifications that the phone recieves.

Depending on the priority of the notification (a text from someone specific, or a message from a certain app) the Raspberry Pi will light either a red, yellow, or green LED.


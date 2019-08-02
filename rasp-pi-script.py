from bluetooth import *
from gpiozero import LED, Button

import time
import sys

green = LED(20)
yellow = LED(16)
red = LED(13)

blue = LED(12)

but = Button(15)

connected = False

def command(s):
	if s == "greenon":
		green.on()
	elif s == "greenoff":
		green.off()
	elif s == "yellowon":
		yellow.on()
	elif s == "yellowoff":
		yellow.off()
	elif s == "redon":
		red.on()
	elif s == "redoff":
		red.off()
	elif s == "allon":
		green.on()
		yellow.on()
		red.on()
	elif s == "alloff":
		green.off()
		yellow.off()
		red.off()
	elif s == "test":
		green.on()
		time.sleep(0.5)
		green.off()
		
		yellow.on()
		time.sleep(0.5)
		yellow.off()
		
		red.on()
		time.sleep(0.5)
		red.off()
		
		
def messages():
	
	global connected
	
	if connected:
		return
	
	connected = True
	
	red.off()
	yellow.on()
	
	socket = BluetoothSocket(RFCOMM)
	
	socket.bind(("",PORT_ANY))
	socket.listen(1)
	
	port = socket.getsockname()[1]
	
	uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"
	
	advertise_service(socket, "raspberrypi", service_id = uuid, 

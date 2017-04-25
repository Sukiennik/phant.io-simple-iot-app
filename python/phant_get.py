import phant
import time
from serial import Serial

def data_get(tuple, send):
	if send is True:
		print "\n>>> >>> >>>"
		print "---UNICODE CSV PRINT---"
		print tuple
		print "---PARSED VALUES---"
		print("BLUE: {0} ++ FLAG: {1} ++ GREEN: {2}, RED: {3} ++ DATE: {4}".format(tuple[0], tuple[1], tuple[2], tuple[3], tuple[4]))
		print ">>> >>> >>>"
	global blue
	blue = tuple[0]
	global flag
	flag = tuple[1]
	global green
	green = tuple[2]
	global red
	red = tuple[3]
	global datestamp
	datestamp = tuple[4]
	

def send_to_arduino(serial, red, green, blue, flag):
	serial.write('R')
	serial.write((red.encode('utf-8')))
	serial.write('G')
	serial.write(green.encode('utf-8'))
	serial.write('B')
	serial.write(blue.encode('utf-8'))
	serial.write('F')
	serial.write(flag.encode('utf-8'))

	
#FIRST CONNECT - CREATING DB AND TABLE FOR DATA
print "::Creating and filling database..."
connected = phant.first_read(
    'https://data.sparkfun.com/output/YGznqddEbRHZr1DXbQ6o', #'http://192.168.0.197:8080/output/0wLdKVzjaVFyjb0Z0v83SPbzQNQ1',
    'stream',
    value=int,
    key=str
)
print "::Database created and populated."

#CONTAINERS FOR DATA FROM DIFFERENT FIELDS
red = 0
green = 0
blue = 0
flag = 0
previous_datestamp = ""
datestamp = None

print '::Opening serial port...'
ser = Serial('/dev/ttyACM0', 9600) #Where /dev/ttyXYZ is port to which Arduino connects
print '::Serial port opened.'
print '::INITIALIZING ARDUINO BOARD...'
time.sleep(3)    
print '::BOARD INITIALIZED.'                  
print '>>>> >>>> >>>> >>>> >>>> >>>>'
print '>>>> >>>> >>>> >>>> >>>> >>>>'
print '>>>> >>>> >>>> >>>> >>>> >>>>' 


while True:
	for row in phant.read('https://data.sparkfun.com/output/YGznqddEbRHZr1DXbQ6o', #'http://192.168.0.197:8080/output/0wLdKVzjaVFyjb0Z0v83SPbzQNQ1',
    			      'stream',
    			      connected,
   			      value=int,
    	 		      key=str).execute('SELECT * FROM stream ORDER BY datetime(timestamp) DESC LIMIT 1'):
    		
		
		data_get(row, False) 
		if previous_datestamp != datestamp:
			data_get(row, True) 
			send_to_arduino(ser, red, green, blue, flag)
			previous_datestamp = datestamp
    	#time.sleep(1)
    	

	
	

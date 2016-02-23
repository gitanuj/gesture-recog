import socket
import atexit
import sys

def close_socket():
	s.close()

HOST = ''
PORT = 8888
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(0)

atexit.register(close_socket)
 
while 1:
    client, addr = s.accept()
    print addr[0] + ':' + str(addr[1]) + ' connected'

    while 1:
    	data = client.recv(1024)
    	print data
    	if not data:
    		client.close()
    		print addr[0] + ':' + str(addr[1]) + ' connection closed'
    		break
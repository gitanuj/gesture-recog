# Script to dump sensor data in supplied file

import socket
import sys

HOST = ''
PORT = 8888
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(0)
                 
while 1:
    client, addr = s.accept()
    time_series = []

    while 1:
        chunk = client.recv(1024)
        if chunk:
            time_series.append(chunk)
        else:
            client.close()
            break

    raw_file = open(str(sys.argv[1]), 'a')

    for chunk in time_series:
        for line in chunk.splitlines():
            raw_file.write(line + ',')
        
    raw_file.write('\n')
    raw_file.close()
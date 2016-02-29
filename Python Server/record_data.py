import socket
import sys
from parse_data import parse

HOST = ''
PORT = 8888

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(0)

def record():
    client, addr = s.accept()
    time_series = ''

    while 1:
        chunk = client.recv(1024)
        if chunk:
            time_series += chunk
        else:
            client.close()
            break

    return time_series + '\n'

if __name__ == '__main__':
    while 1:
        data = record()
        file = open(str(sys.argv[1]), 'a')
        file.writelines(data)
        file.close()
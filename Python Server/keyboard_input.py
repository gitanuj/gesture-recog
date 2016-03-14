import socket
import sys

HOST = ''
PORT = 8800

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)

# returns a raw dump of recorded data from phone (comma separated)
def record_keyboard_data():
    client, addr = s.accept()
    data = ''

    while 1:
        chunk = client.recv(1024)
        if chunk:
            data += chunk
        else:
            client.close()
            break

    return data

if __name__ == '__main__':
    while 1:
        data = record_keyboard_data()
        print data
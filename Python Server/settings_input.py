import socket
import sys

HOST = ''
PORT = 6969

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(0)

# gets setting configurations from the phone (comma separated)
def get_settings():
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
        data = get_settings()
        print data

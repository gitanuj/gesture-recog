import socket
import atexit
import sys
import csv

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

    accel_x = []
    accel_y = []
    accel_z = []
    accel = []
    gyro_x = []
    gyro_y = []
    gyro_z = []
    gyro = []

    while 1:
        data = client.recv(1024)
        values = data.split()

        if data: #there is data, add it
            accel_x.append(values[0])
            accel_y.append(values[1])
            accel_z.append(values[2])

            gyro_x.append(values[3])
            gyro_y.append(values[4])
            gyro_z.append(values[5])
            
            print data

        else: #no more data, write to file
            client.close()
            print addr[0] + ':' + str(addr[1]) + ' connection closed'
            print accel_x

            with open('accel_x.csv', 'a') as accelxfile:
                writer = csv.writer(accelxfile, lineterminator="\n")
                for x_data in accel_x:
                    writer.writerow([x_data])

            with open('accel_y.csv', 'a') as accelyfile:
                writer = csv.writer(accelyfile, lineterminator="\n")
                for y_data in accel_y:
                    writer.writerow([y_data])

            with open('accel_z.csv', 'a') as accelzfile:
                writer = csv.writer(accelzfile, lineterminator="\n")
                for z_data in accel_z:
                    writer.writerow([z_data])

            with open('gyro_x.csv', 'a') as gfile:
                writer = csv.writer(gfile, lineterminator="\n")
                for x_data in gyro_x:
                    writer.writerow([x_data])

            with open('gyro_y.csv', 'a') as gfile:
                writer = csv.writer(gfile, lineterminator="\n")
                for y_data in gyro_y:
                    writer.writerow([y_data])

            with open('gyro_z.csv', 'a') as gfile:
                writer = csv.writer(gfile, lineterminator="\n")
                for z_data in gyro_z:
                    writer.writerow([z_data])

    		break
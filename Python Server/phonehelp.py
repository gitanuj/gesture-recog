from record_data import record
from parse_data import parse
from classify import classify

if __name__ == '__main__':
	while 1:
		data = record()
		parsed_data = parse(data)

		ax = parsed_data[0]
		ay = parsed_data[1]
		az = parsed_data[2]
		gx = parsed_data[3]
		gy = parsed_data[4]
		gz = parsed_data[5]

		clazz = classify(ax, ay, az, gx, gy, gz)
		print 'Detected: ' + clazz
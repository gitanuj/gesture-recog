import sys
import os

def parse(data):
	parsed_data = [''] * 6

	for line in data:
		if not line.strip():
			continue
		
		for value_set in line.split(','):
			if not value_set.strip():
				continue

			values = value_set.split()
			for i in range(0, 6):
				parsed_data[i] += values[i] + ' '
	
		for p in parsed_data:
			p += '\n'

	return parsed_data

if __name__ == '__main__':
	with open(str(sys.argv[1])) as f:
		data = f.readlines()

	dirname = os.path.dirname(os.path.realpath(sys.argv[1]))
	parsed_data = parse(data)

	files = [None] * 6
	files[0] = open(dirname + '/ax.txt', 'w')
	files[1] = open(dirname + '/ay.txt', 'w')
	files[2] = open(dirname + '/az.txt', 'w')
	files[3] = open(dirname + '/gx.txt', 'w')
	files[4] = open(dirname + '/gy.txt', 'w')
	files[5] = open(dirname + '/gz.txt', 'w')

	for i, p in enumerate(parsed_data):
		files[i].write(p)
		files[i].close()
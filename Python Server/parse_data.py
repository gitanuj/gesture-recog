import sys
import os

# converts a string of recorded data to list of space separated time series strings
def parse(data):
	parsed_data = [''] * 6
	for value_set in data.split(','):
		for i, val in enumerate(value_set.split()):
			parsed_data[i] += val + ' '

	return parsed_data

if __name__ == '__main__':
	dirname = os.path.dirname(os.path.realpath(sys.argv[1]))
	with open(str(sys.argv[1])) as f:
		data = f.readlines()

	files = []
	files.append(open(dirname + '/ax.txt', 'a'))
	files.append(open(dirname + '/ay.txt', 'a'))
	files.append(open(dirname + '/az.txt', 'a'))
	files.append(open(dirname + '/gx.txt', 'a'))
	files.append(open(dirname + '/gy.txt', 'a'))
	files.append(open(dirname + '/gz.txt', 'a'))

	for line in data:
		parsed_data = parse(line)
		for i, p in enumerate(parsed_data):
			files[i].write(p)
			files[i].write('\n')

	for f in files:
		f.close()
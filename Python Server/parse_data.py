import sys

with open(str(sys.argv[1])) as f:
    data = f.readlines()

files = []
files.append(open('ax.txt', 'w'))
files.append(open('ay.txt', 'w'))
files.append(open('az.txt', 'w'))
files.append(open('gx.txt', 'w'))
files.append(open('gy.txt', 'w'))
files.append(open('gz.txt', 'w'))

for line in data:
	for value_set in line.split(','):
		for i, value in enumerate(value_set.split()):
			files[i].write(value + ' ')
	for file in files:
		file.write('\n')

for file in files:
	file.close()
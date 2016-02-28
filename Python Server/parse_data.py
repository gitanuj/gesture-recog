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
	if not line.strip():
		continue
		
	for value_set in line.split(','):
		if not value_set.strip():
			continue

		values = value_set.split()
		for i in range(0, 6):
			files[i].write(values[i] + ' ')
	for file in files:
		file.write('\n')

for file in files:
	file.close()
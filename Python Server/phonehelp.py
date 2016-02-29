from record_data import record
from parse_data import parse

if __name__ == '__main__':
	while 1:
		data = record()
		parsed_data = parse(data)
		# TODO calculate DTW
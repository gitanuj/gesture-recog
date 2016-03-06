from saxpy import SAX
import os

with open('data.txt') as f:
	data = f.readlines()

s = SAX(32,10)
for line in data:
	x = []
	for p in line.split():
		x.append(float(p))
	print s.to_letter_rep(x)[0]
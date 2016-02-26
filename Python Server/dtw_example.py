import numpy as np
import matplotlib.pyplot as plt
import pylab
from dtw import dtw
import sys

x = np.genfromtxt(str(sys.argv[1])).reshape(-1, 1)
y = np.genfromtxt(str(sys.argv[2])).reshape(-1, 1)

dist, cost, acc, path = dtw(x, y, dist=lambda x, y: np.linalg.norm(x - y, ord=1))

print dist

plt.imshow(acc.T, origin='lower', cmap=plt.cm.gray, interpolation='nearest')
plt.plot(path[0], path[1], 'w')
plt.xlim((-0.5, acc.shape[0]-0.5))
plt.ylim((-0.5, acc.shape[1]-0.5))

pylab.show()
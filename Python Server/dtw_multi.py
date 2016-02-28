import numpy as np
import sys
from math import sqrt

W = 25

def DTWDistance(s):
    n = len(s)
    len1 = len(s[0])
    len2 = len(s[n/2])

    DTW={}
    
    w = max(W, abs(len1-len2))
    
    for i in range(-1,len1):
        for j in range(-1,len2):
            DTW[(i, j)] = float('inf')
    DTW[(-1, -1)] = 0
  
    for i in range(len1):
        for j in range(max(0, i-w), min(len2, i+w)):
            dist = 0
            for count in range(n/2):
                dist += (s[count][i]-s[count+n/2][j])**2
            DTW[(i, j)] = dist + min(DTW[(i-1, j)],DTW[(i, j-1)], DTW[(i-1, j-1)])
        
    return sqrt(DTW[len1-1, len2-1])

data = []
for i in range(1, len(sys.argv)):
    data.append(np.genfromtxt(str(sys.argv[i])).reshape(-1, 1))

print DTWDistance(data)
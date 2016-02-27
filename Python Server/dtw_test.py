import numpy as np
import sys
from math import sqrt

def DTWDistance(s1, s2,w):
    DTW={}
    
    w = max(w, abs(len(s1)-len(s2)))
    
    for i in range(-1,len(s1)):
        for j in range(-1,len(s2)):
            DTW[(i, j)] = float('inf')
    DTW[(-1, -1)] = 0
  
    for i in range(len(s1)):
        for j in range(max(0, i-w), min(len(s2), i+w)):
            dist= (s1[i]-s2[j])**2
            DTW[(i, j)] = dist + min(DTW[(i-1, j)],DTW[(i, j-1)], DTW[(i-1, j-1)])
        
    return sqrt(DTW[len(s1)-1, len(s2)-1])

x = np.genfromtxt(str(sys.argv[1])).reshape(-1, 1)
y = np.genfromtxt(str(sys.argv[2])).reshape(-1, 1)

print x

print DTWDistance(x, y, 25)
#! /usr/bin/python
import os
import random
from scipy.stats import bernoulli

def main():
    cvs_files = (
        'procesamientoImagenes_codigoBarras.csv',
        'procesamientoImagenes_logotipos.csv',
        'procesamientoImagenes_ocr.csv')

    for filename in cvs_files: 
        path = os.path.abspath('..')+'/data/'+filename
        fl = open(path, "w")
        fl.write('frontal, izquierda, derecha, trasera\n')
        for trial in zip(bernoulli.rvs(random.random(), size=30),
                      bernoulli.rvs(random.random(), size=30),
                      bernoulli.rvs(random.random(), size=30),
                      bernoulli.rvs(random.random(), size=30) ):
            fl.write(str(trial[0])+', '+str(trial[1])+', '+str(trial[2])+', '+str(trial[3])+' \n')
        fl.close()
    print "Done."

main()

import math
import random
import sys
import os 
import ConfigParser

def poisson(lamb):
    acumulador = exponencial( lamb)
    contador = 0
    while( acumulador < 1):
        acumulador = acumulador + exponencial( lamb)
        contador+=1
    return contador
 
def exponencial(lamb):
    return (-1*math.log( random.random() ))/lamb

def generarExperimento1(n):
    config = ConfigParser.ConfigParser()
    config.read("config.ini")

    fl = open(config.get("MainSection", "TiempoAprendizajeNormalDatos"), "w")
    fl.write("tiempo1, tiempo2, tiempo3 \n")
    for i in range(n):
        fl.write( "%.2f, %.2f, %.2f\n"
                  % (exponencial(2.5), exponencial(2.5), exponencial(2.5))
                  )
    fl.close()

    fl = open(config.get("MainSection", "TiempoAprendizajeARDatos"), "w")
    fl.write("tiempo1, tiempo2, tiempo3 \n")
    for i in range(n):
        fl.write( "%.2f, %.2f, %.2f\n"
                  % (exponencial(2.5), exponencial(2.5), exponencial(2.5))
                  )
    fl.close()


    fl = open(config.get("MainSection", "ErroresAprendizajeNormalDatos"), "w")
    fl.write("errores1, errores2, errores3 \n")
    for i in range(n):
        fl.write( "%i, %i, %i\n"
                  % (poisson(10), poisson(10), poisson(10))
                  )
    fl.close()

    fl = open(config.get("MainSection", "ErroresAprendizajeARDatos"), "w")
    fl.write("errores1, errores2, errores3 \n")
    for i in range(n):
        fl.write( "%i, %i, %i\n"
                  % (poisson(10), poisson(10), poisson(10))
                  )
    fl.close()


"""
def generarExperimento2(n):
    fl = open("experimento2.cvs", "w")

    fl.write("tiempoH1, interaccionesH1, satisfaccionH1, tiempoH2, interaccionesH2, satisfaccionH2\n")

    for i in range(n):
        data_row = [exponencial(2.5), poisson(10), random.randint(1, 10), exponencial(2.5), poisson(10), random.randint(1, 10)]
        fl.write( "%.2f, %i, %i, %.2f, %i, %i\n" % (data_row[0],  data_row[1], data_row[2], data_row[3], data_row[4], data_row[5] ))

    fl.close()    
"""

if __name__ == '__main__':
    generarExperimento1(100)
    #generarExperimento2(100)

import ConfigParser
import math

def puntuacion_diferencia(vect):
    score = 0.0
    score += (vect[0] - vect[1])
    comp_val = min(vect[:2])
    score += (comp_val - vect[2])
    return score
    

def join_data():
    config = ConfigParser.ConfigParser()
    config.read("config.ini")

    fl = open(config.get("MainSection", "NormalDatos"), "w")
    
    fl.write("tiempoAprendizaje, errorAprendizaje \n")

    fl_tiempoaprendizaje = open(config.get("MainSection", "TiempoAprendizajeNormalDatos"), "r")
    fl_tiempoaprendizaje.readline()

    fl_erroresaprendizaje = open(config.get("MainSection", "ErroresAprendizajeNormalDatos"), "r")
    fl_erroresaprendizaje.readline()

    for tiempoaprendizaje_line in fl_tiempoaprendizaje:
        erroresaprendizaje_line = fl_erroresaprendizaje.readline()

        tiempo_aprendizaje = [float(i) for i in tiempoaprendizaje_line.split(",")]
        errores_aprendizaje = [float(i) for i in erroresaprendizaje_line.split(",")]
        
        fl.write( "%.2f, %.2f \n" %(puntuacion_diferencia(tiempo_aprendizaje), puntuacion_diferencia(errores_aprendizaje)) )

    fl.close()
    fl_tiempoaprendizaje.close()
    fl_erroresaprendizaje.close()

    # Realidad aumentada 

    fl = open(config.get("MainSection", "ARDatos"), "w")
    
    fl.write("tiempoAprendizaje, errorAprendizaje \n")

    fl_tiempoaprendizaje = open(config.get("MainSection", "TiempoAprendizajeARDatos"), "r")
    fl_tiempoaprendizaje.readline()

    fl_erroresaprendizaje = open(config.get("MainSection", "ErroresAprendizajeARDatos"), "r")
    fl_erroresaprendizaje.readline()

    for tiempoaprendizaje_line in fl_tiempoaprendizaje:
        erroresaprendizaje_line = fl_erroresaprendizaje.readline()

        tiempo_aprendizaje = [float(i) for i in tiempoaprendizaje_line.split(",")]
        errores_aprendizaje = [float(i) for i in erroresaprendizaje_line.split(",")]
        
        tiempo_aprendizaje_prom  = sum(tiempo_aprendizaje)/len(tiempo_aprendizaje )
        errores_aprendizaje_prom  = sum(tiempo_aprendizaje)/len(tiempo_aprendizaje )

        tiempo_aprendizaje_promvar = sum( [ math.fabs(x - tiempo_aprendizaje_prom)  for x in tiempo_aprendizaje] )
        errores_aprendizaje_promvar = sum( [ math.fabs(x - errores_aprendizaje_prom)  for x in errores_aprendizaje] )

        fl.write( "%.2f, %.2f \n" %(tiempo_aprendizaje_promvar, errores_aprendizaje_promvar) ) 

    fl.close()
    fl_tiempoaprendizaje.close()
    fl_erroresaprendizaje.close()


if __name__ == '__main__':
    join_data()

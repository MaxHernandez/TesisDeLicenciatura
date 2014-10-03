#! /usr/bin/python
# -*- coding: utf-8 -*-
import csv, os

def generate_table(pmatrix, column_titles):
    global table_header, table_footer
    text = ""
    path = os.path.abspath('..')+'/output/'+'procesamientoImagenesTabla.tex'
    fl = open(path, "w")
    flp = open("plantillaTabla.tex", "r")

    for i in range(len(pmatrix[0])):
        text += "\\textbf{%s}   & %.2f\\%% & %.2f\\%% & %.2f\\%% \\\\ \\hline \n"%(column_titles[i], pmatrix[0][i], pmatrix[1][i], pmatrix[2][i])
    
    for line in flp:
        line = line.replace('{INSERT}', text)
        fl.write(line)

    fl.close()

def generateChartData(pmatrix):
    fl = open("data.dat", "w")

    angle = (0, 90, 180, 270)

    for i in range(len(pmatrix[0])):
        fl.write(str(angle[i])+' '+str(pmatrix[0][i])+' '+str(pmatrix[1][i])+' '+str(pmatrix[2][i])+'\n')

    fl.close()

def main():
    cvs_files = (
        'procesamientoImagenes_codigoBarras.csv',
        'procesamientoImagenes_logotipos.csv',
        'procesamientoImagenes_ocr.csv')
    column_titles = (
        'Frontal',
        'Izquierda',
        'Derecha', 
        'Trasera')

    prom_matrix = list()

    for filename in cvs_files:
        path = os.path.abspath('..')+'/data/'+filename
        fl = open(path, "rb")
        csvr = list(csv.reader(fl))[1:]
        fl.close()

        temp = [0, 0, 0, 0]        
        for row in csvr:
            for i in range(len(row)):
                temp[i] += int(row[i])

        nvals = len(csvr)
        for i in range(len(temp)):
            temp[i] = (float(temp[i]) / nvals) * 100
        prom_matrix.append(temp)

    generate_table(prom_matrix, column_titles)
    generateChartData(prom_matrix)

    print "Done."


main()

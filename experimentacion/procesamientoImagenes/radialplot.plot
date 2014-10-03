#set term latex
#set output "procesamientoImagenesGrafica.tex"
set term eps
set output "procesamientoImagenesGrafica.eps"

unset border
set polar
set angles degrees #set gnuplot on degrees instead of radians

set style line 10 lt 1 lc 0 lw 0.3 #redefine a new line style for the grid
set style data filledcurves 
set style fill solid 0.5
set style fill transparent solid 0.5

set grid polar 90 #set the grid to be displayed every 60 degrees
set grid ls 10
set xrange [-120:120] #make gnuplot to go until 6000
set yrange [-120:120]
set xtics axis #disply the xtics on the axis instead of on the border
set ytics axis


set xtics scale 0 #"remove" the tics so that only the y tics are displayed
set xtics ("" 0, "" 20, "" 40, "" 60, "" 80, "" 100)
# set the xtics only go from 0 to 6000 with increment of1000 but do not display anything. This has to be done otherwise the grid will not be displayed correctly.
set ytics 0, 20, 100 #make the ytics go from the center (0) to 6000 with incrment of 1000

set size square 
set key lmargin

set_label(x, text) = sprintf("set label '%s' at (130*cos(%f)), (120*sin(%f)) center", text, x, x) #this places a label on the outside

#here all labels are created
eval set_label(0, "Frontal")
eval set_label(90, "Izquierda")
eval set_label(180, "Derecha")
eval set_label(270, "Trasera")

set style line 11 lt 1 lw 2 pt 0 ps 2 #set the line style for the plot

set title "Porcentaje de procesamiento imagenes"
set title font ",19"

plot "data.dat" u 1:2 t "Código de barras", "data.dat" u 1:3 t "Logotipo", "data.dat" u 1:4 t "OCR"

#plot "-" u 1:2 t "Código de barras", "-" u 1:3 t "Logotipo", "-" u 1:4 t "OCR"
#0   34 23 90
#90  50 60 67
#180 47 78 2
#270 38 98 23
#0   34 23 90
#e

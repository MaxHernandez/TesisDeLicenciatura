width = 0.1
hist(x,width) = width * floor(x/width) + width/2.0
set term png
set output "histograma_tiempoAprenizaje.png"

set offset graph 0.05,0.05,0.05,0.0
set boxwidth width*0.8

set style histogram rows
set style fill solid border -1

set xlabel "Puntuación diferencia de aprendizaje"
set ylabel "Frecuencia"

set tics out nomirror
set key opaque

#count and plot
plot "experimento1_normal.cvs" u (hist($1,width)):(1.0)  smooth freq w boxes lc rgb"#6368ff" t'Barra de búsqueda', \
      "experimento1_ar.cvs" u (hist($1,width)):(1.0)  smooth freq w boxes lc rgb"#63ff68" t'Realidad aumentada'

reset
width = 1
hist(x,width) = width * floor(x/width) + width/2.0
set term png
set output "histograma_erroresAprenizaje.png"

set offset graph 0.05,0.05,0.05,0.0
set boxwidth width*0.8

set style histogram rows
set style fill solid border -1

set tics out nomirror

set xlabel "Puntuación diferencia de aprendizaje"
set ylabel "Frecuencia"

#count and plot
plot "experimento1_normal.cvs" u (hist($2,width)):(1.0) smooth freq w boxes lc rgb"#6368ff" t'Barra de búsqueda', \
     "experimento1_ar.cvs" u (hist($2,width)):(1.0) smooth freq w boxes lc rgb"#63ff68"  t'Realidad aumentada' 

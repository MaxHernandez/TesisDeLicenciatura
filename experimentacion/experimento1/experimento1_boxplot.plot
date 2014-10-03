set term png
set output "boxplot_tiempoAprenizaje.png"

set xlabel "Herramientas"
set ylabel "Puntuación diferencia de aprendizaje"

set style fill solid 0.25 border -1
set style boxplot outliers pointtype 19
set style data boxplot 

set boxwidth  0.5
set pointsize 0.5

unset key
set border 2
set xtics ("Barra de búsqueda" 1, "Realidad aumenada" 2) scale 0.0
set xtics nomirror
set ytics nomirror

plot "experimento1_normal.cvs" using (1):($1) lc rgb"#6368ff" , "experimento1_ar.cvs" using (2):($1) lc rgb"#63ff68"

reset
set term png
set output "boxplot_erroresAprenizaje.png"

set xlabel "Herramientas"
set ylabel "Puntuación diferencia de aprendizaje"

set style fill solid 0.25 border -1
set style boxplot outliers pointtype 19
set style data boxplot 

set boxwidth  0.5
set pointsize 0.5

unset key
set border 2
set xtics ("Barra de búsqueda" 1, "Realidad aumentada" 2) scale 0.0
set xtics nomirror
set ytics nomirror

plot "experimento1_normal.cvs" using (1):($2) lc rgb"#6368ff", "experimento1_ar.cvs" using (2):($2) lc rgb"#63ff68"
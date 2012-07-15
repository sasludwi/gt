# plots the threshold vs the number of edges in the resulting graph
set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 800, 600 
set output 't_vs_number_components.png'
set key inside right top vertical Right noreverse noenhanced autotitles nobox

# Title
set title  "theta vs number of components"
set xlabel "theta (threshold)"
set ylabel "number of components"
set auto y	# set auto y ; set logscale y
set logscale x	# set auto x ; set logscale x
set xtics auto
set ytics auto
plot "../components.dat" using 1:2 title 'Human' lw 3 with linespoints, "../components.dat" using 1:3 title "Chimpanzee" lw 3 with linespoints


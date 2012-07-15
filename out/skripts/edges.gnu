# plots the threshold vs the number of edges in the resulting graph
set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 800, 600 
set output 't_vs_number_edges.png'
set key inside right top vertical Right noreverse noenhanced autotitles nobox

# Title
set title  "theta vs number of edges"
set xlabel "theta (threshold)"
set ylabel "number of edges"
set logscale y	# set auto y ; set logscale y
set logscale x	# set auto x ; set logscale x
set xtics auto
set ytics auto
plot "../edges.dat" using 1:2 title 'Human' lw 3 with linespoints, "../edges.dat" using 1:3 title "Chimpanzee" lw 3 with linespoints


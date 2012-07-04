# plots the threshold vs the number of edges in the resulting graph
clear
reset
set key off
set border 3
set auto

# TODO: set in title if human or chimpanzee
set title  "theta vs number_edges"
set xlabel "theta (threshold)"
set ylabel "number_edges"
set auto y	# set auto y ; set logscale y
set logscale x	# set auto x ; set logscale x
set xtics auto
set ytics auto
plot "edges.dat" with linespoints
pause -1


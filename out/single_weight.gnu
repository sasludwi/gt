# plots the threshold vs the node-degree
clear
reset
set key off
set border 3
set auto

set xlabel "node-degree"
set ylabel "frequency (absolute value)"

set auto y	# set auto y ; set logscale y
set auto x	# set auto x ; set logscale x
set xtics auto
set ytics auto

set style histogram clustered gap 1
set style fill solid border -1

binwidth=0.00001
set boxwidth binwidth
bin(x,width)=width*floor(x/width) + binwidth/2.0

plot 'weights_1.0E-5.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

pause -1


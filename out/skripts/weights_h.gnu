# plots the threshold vs the number of edges in the resulting graph
set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 800, 600 
set output 't_vs_weights_human.png'
set nokey

# Title

set xlabel "edge weight"
set ylabel "frequency (absolute value)"
set auto y	# set auto y ; set logscale y
set auto x	# set auto x ; set logscale x
set xtics auto
set ytics auto
set xrange[0.5:1.05]
set yrange[0:13000]

set style histogram clustered gap 0
set style fill solid noborder
binwidth=0.001
set boxwidth binwidth
bin(x,width)=width*floor(x/width) + binwidth/2.0


set multiplot;                          # get into multiplot mode

set size 0.5, 0.3333;  

# obere Zeile
set origin 0.0,0.6666  
set title  "Human - weight distribution for theta = 1.0E-6"
plot '../weights_h_1.0E-6.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

set origin 0.5,0.6666
set title  "Human - weight distribution for theta = 1.0E-5"
plot '../weights_h_1.0E-5.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

# mittlere Zeile
set origin 0.0,0.3333
set title  "Human - weight distribution for theta = 1.0E-4"
plot '../weights_h_1.0E-4.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

set origin 0.5,0.3333  
set title  "Human - weight distribution for theta = 1.0E-3"
plot '../weights_h_0.0010.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

# untere Zeile
set origin 0.0,0.0
set title  "Human - weight distribution for theta = 1.0E-2"
plot '../weights_h_0.01.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

set origin 0.5,0.0
set title  "Human - weight distribution for theta = 5.0E-2"
plot '../weights_h_0.05.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes


unset multiplot                         # exit multiplot mode



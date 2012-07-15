# plots the threshold vs the number of edges in the resulting graph
set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 800, 600 
set output 't_vs_degrees_chimp.png'
set nokey

# Title

set xlabel "degree"
set ylabel "frequency (absolute value)"
set auto y	# set auto y ; set logscale y
set auto x	# set auto x ; set logscale x
set xtics auto
set ytics auto
set xrange[0:620]
#set yrange[0:160]

set style histogram clustered gap 0
set style fill solid noborder
binwidth=1
set boxwidth binwidth
bin(x,width)=width*floor(x/width) + binwidth/2.0


set multiplot;                          # get into multiplot mode

set size 0.5, 0.3333;  

# obere Zeile
set origin 0.0,0.6666  
set title  "Chimpanzee - degree distribution for theta = 1.0E-6"
plot '../deg_c_1.0E-6.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

set origin 0.5,0.6666
set title  "Chimpanzee - degree distribution for theta = 1.0E-5"
plot '../deg_c_1.0E-5.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

# mittlere Zeile
set origin 0.0,0.3333
set title  "Chimpanzee - degree distribution for theta = 1.0E-4"
plot '../deg_c_1.0E-4.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

set origin 0.5,0.3333  
set title  "Chimpanzee - degree distribution for theta = 1.0E-3"
plot '../deg_c_0.0010.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

# untere Zeile
set origin 0.0,0.0
set title  "Chimpanzee - degree distribution for theta = 1.0E-2"
plot '../deg_c_0.01.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

set origin 0.5,0.0
set title  "Chimpanzee - degree distribution for theta = 5.0E-2"
plot '../deg_c_0.05.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes


unset multiplot                         # exit multiplot mode



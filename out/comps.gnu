# plots the threshold vs number components
clear
reset
set key off
set border 3
set auto

# TODO: set in title if human or chimpanzee
#set xlabel "node-degree"
#set ylabel "frequency (absolute value)"
set auto y	# set auto y ; set logscale y
set auto x	# set auto x ; set logscale x
set xrange[-50:750]
#set yrange[0:14000]
set xtics auto
set ytics auto

set style histogram clustered gap 0
set style fill solid noborder
binwidth=1
set boxwidth binwidth
bin(x,width)=width*floor(x/width) + binwidth/2.0


set multiplot;                          # get into multiplot mode

set size 0.3333, 0.25;  

set origin 0.0,0.75  
set title  "degree distribution for theta = 1.0E-5"
plot 'comp_1.0E-5.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

set origin 0.33333,0.75
set title  "degree distribution for theta = 2.0E-5"
plot 'comp_2.0E-5.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

set origin 0.66666,0.75
set title  "degree distribution for theta = 5.0E-5"
plot 'comp_5.0E-5.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes


set origin 0.0,0.5  
set title  "degree distribution for theta = 1.0E-4"
plot 'comp_1.0E-4.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

set origin 0.33333,0.5
set title  "degree distribution for theta = 2.0E-4"
plot 'comp_2.0E-4.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

set origin 0.66666,0.5
set title  "degree distribution for theta = 5.0E-4"
plot 'comp_5.0E-4.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes


set origin 0.0,0.25  
set title  "degree distribution for theta = 1.0E-3"
plot 'comp_0.0010.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

set origin 0.33333,0.25
set title  "degree distribution for theta = 2.0E-3"
plot 'comp_0.0020.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

set origin 0.66666,0.25
set title  "degree distribution for theta = 5.0E-3"
plot 'comp_0.0050.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes


set origin 0.0,0.0
set title  "degree distribution for theta = 1.0E-2"
plot 'comp_0.01.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes 

set origin 0.33333,0.0
set title  "degree distribution for theta = 2.0E-2"
plot 'comp_0.02.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes

set origin 0.66666,0.0
set title  "degree distribution for theta = 5.0E-2"
plot 'comp_0.05.dat' using (bin($1,binwidth)):(1.0) smooth freq with boxes


unset multiplot                         # exit multiplot mode




pause -1


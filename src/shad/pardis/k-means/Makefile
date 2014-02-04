all:	kmeans

kmeans:	kmeans.cpp
	g++ -g -fopenmp -o $@ $<

clean:
	rm kmeans

gen:	gen.cpp
	g++ -o $@ $<

points.csv:	gen
	./gen >$@

clusters.csv:	points.csv kmeans
	./kmeans <points.csv >$@

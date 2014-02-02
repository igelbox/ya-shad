#include <cstdlib>
#include <cfloat>
#include <cstdio>
#include <vector>

struct Point {
    double x, y;
};

double dist_sqr(const Point &p0, const Point &p1) {
    double dx = p1.x - p0.x, dy = p1.y - p0.y;
    return dx * dx + dy * dy;
}

void clusterize(Point *points, unsigned num_points, Point *clusters, unsigned num_clusters, double (*fdist)(const Point &p0, const Point &p1)) {
    Point min = {DBL_MAX, DBL_MAX}, max = {-DBL_MAX, -DBL_MAX};
    //calculate dimensions
    for (unsigned i = 0; i < num_points; i++) {
        Point &p = points[i];
        if (p.x < min.x)
            min.x = p.x;
        if (p.y < min.y)
            min.y = p.y;
        if (p.x > max.x)
            max.x = p.x;
        if (p.y > max.y)
            max.y = p.y;
    }

    //initalize cluster centroids
    for (unsigned i = 0; i < num_clusters; i++) {
        Point &c = clusters[i];
        c.x = (max.x - min.x) * rand() / RAND_MAX + min.x;
        c.y = (max.y - min.y) * rand() / RAND_MAX + min.y;
    }

    unsigned point_cluster[num_points];
    unsigned changed;
    //main loop
    do {
        changed = 0;

        //find closest centroid for each point
        for (unsigned i = 0; i < num_points; i++) {
            unsigned nearest_cluster_index = 0;
            double nearest_cluster_distance = DBL_MAX;
            for (unsigned j = 0; j < num_clusters; j++) {
                double dist = fdist(clusters[j], points[i]);
                if (dist < nearest_cluster_distance) {
                    nearest_cluster_distance = dist;
                    nearest_cluster_index = j;
                }
            }
            if (point_cluster[i] != nearest_cluster_index)
                changed++;
            point_cluster[i] = nearest_cluster_index;
        }

        //recalculate cluster centroids
        unsigned cluster_points[num_clusters];
        for (unsigned i = 0; i < num_clusters; i++) {
            Point &c = clusters[i];
            c.x = c.y = 0.0;
            cluster_points[i] = 0;
        }
        for (unsigned i = 0; i < num_points; i++) {
            Point &p = points[i];
            unsigned pci = point_cluster[i];
            Point &c = clusters[pci];
            c.x += p.x;
            c.y += p.y;
            cluster_points[pci]++;
        }
        for (unsigned i = 0; i < num_clusters; i++) {
            Point &c = clusters[i];
            unsigned cpc = cluster_points[i];
            if (cpc) {
                c.x /= cpc;
                c.y /= cpc;
            } else {
                c.x = c.y = 0.0;
            }
        }
    } while (changed);
}

int main(int argc, char **argv) {
    std::vector<Point> points;

    //load points from stdin
    Point p;
    while (scanf("%lf;%lf\n", &p.x, &p.y) != EOF)
        points.push_back(p);

    unsigned num_clusters = 3;
    Point clusters[num_clusters];
    clusterize(&points[0], points.size(), clusters, num_clusters, dist_sqr);

    for (unsigned i = 0; i < num_clusters; i++) {
        Point &c = clusters[i];
        printf("%f;%f\n", c.x, c.y);
    }
    return EXIT_SUCCESS;
}

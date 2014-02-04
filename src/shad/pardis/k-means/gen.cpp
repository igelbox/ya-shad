#include <cstdlib>
#include <cstdio>
#include <cmath>

static const double//
        SIZE_X = 100.0,
        SIZE_Y = 100.0,
        RADIUS_MAX = 1.0;
static const unsigned//
        CLUSTER_COUNT = 2000,
        POINTS_PER_CLUSTER_MIN = 20,
        POINTS_PER_CLUSTER_MAX = 50;

template<class T> T uniform(T range) {
    return range * rand() / RAND_MAX;
}

int main() {
    srand(1);
    for (unsigned i = 0; i < CLUSTER_COUNT; i++) {
        double x = uniform(SIZE_X);
        double y = uniform(SIZE_Y);
        double r = uniform(RADIUS_MAX);
        unsigned cnt = uniform(POINTS_PER_CLUSTER_MAX - POINTS_PER_CLUSTER_MIN) + POINTS_PER_CLUSTER_MIN;
        for (unsigned _ = 0; _ < cnt; _++) {
            double rr = r * rand() / RAND_MAX;
            double aa = uniform(2.0 * M_PI);
            printf("%f;%f\n", x + rr * cos(aa), y + rr * sin(aa));
        }
    }
    return EXIT_SUCCESS;
}

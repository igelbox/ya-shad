#include <cstdlib>
#include <cstdio>
#include <cmath>

double drand() {
    return (double) rand() * 100.0 / RAND_MAX;
}

int main() {
    srand(1);
    for (unsigned i = 0; i < 5; i++) {
        double x = 100.0 * rand() / RAND_MAX;
        double y = 100.0 * rand() / RAND_MAX;
        double r = 30.0 * rand() / RAND_MAX;
        for (unsigned _ = 0; _ < 20; _++) {
            double rr = r * rand() / RAND_MAX;
            double aa = 2.0 * M_PI * rand() / RAND_MAX;
            printf("%f;%f\n", x + rr * cos(aa), y + rr * sin(aa));
        }
    }
    return EXIT_SUCCESS;
}

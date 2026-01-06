#include <pty.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/select.h>

int main() {
    int master, slave;
    char name[128];
    char buf[256];

    // configure in a way that echo is disabled (-echo raw)
    // https://www.man7.org/linux/man-pages/man3/termios.3.html
    if (openpty(&master, &slave, name, NULL, NULL) == -1) {
        perror("openpty");
        return 1;
    }

    // disable echo and set raw mode on the slave pty
        {
            struct termios tio;
            if (tcgetattr(slave, &tio) == -1) {
                perror("tcgetattr");
            } else {
                cfmakeraw(&tio);        // set raw mode
                tio.c_lflag &= ~ECHO;  // ensure echo is disabled
                if (tcsetattr(slave, TCSANOW, &tio) == -1) {
                    perror("tcsetattr");
                }
            }
        }

    // Tell Java which device the client should open
    printf("%s\n", name);
    fflush(stdout);

    while (1) {
        fd_set fds;
        FD_ZERO(&fds);
        FD_SET(master, &fds);
        FD_SET(STDIN_FILENO, &fds);

        select(master + 1, &fds, NULL, NULL, NULL);

        if (FD_ISSET(master, &fds)) {
            int n = read(master, buf, sizeof(buf));
            if (n > 0) write(STDOUT_FILENO, buf, n);
        }

        if (FD_ISSET(STDIN_FILENO, &fds)) {
            int n = read(STDIN_FILENO, buf, sizeof(buf));
            if (n > 0) write(master, buf, n);
        }
    }
}

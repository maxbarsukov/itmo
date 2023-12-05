/* stack-smash.c */

#include <stdio.h>
#include <stdlib.h>

struct user {
  const char *name, *password;
} const users[] = {{"Cat", "Meowmeow"}, {"Skeletor", "Nyarr"}};

void print_users() {
  printf("Users:\n");
  for (size_t i = 0; i < sizeof(users) / sizeof(struct user); i++) {
    printf("%s: %s\n", users[i].name, users[i].password);
  }
}

void fill(FILE *f, char *where) {
  size_t read_total = 0;
  for (;;) {
    size_t read = fread(where + read_total, 1, 1, f);
    if (!read)
      break;
    else
      read_total += read;
  }
}

void vulnerable(FILE *f) {
  char buffer[8];
  fill(f, buffer);
}

int main(int argc, char **argv) {
  vulnerable(stdin);

  puts("nothing happened");
}

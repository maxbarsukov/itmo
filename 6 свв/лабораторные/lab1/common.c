#include "common.h"

int
strlen(const char *s)
{
  int len = 0;
  while (s[len] != '\0')
  {
    len++;
  }

  return len;
}

void
reverse(char *s)
{
  if (!s || !*s)
  {
    return;
  }

  int i, j;
  char c;

  for (i = 0, j = strlen(s)-1; i<j; i++, j--)
  {
    c = s[i];
    s[i] = s[j];
    s[j] = c;
  }
}

int
atoi(const char *s)
{
  int res = 0;
  while (*s)
  {
    res = res * 10 + (*s - '0');
    s++;
  }
  return res;
}

void
itoa(int n, char *s)
{
  int i, sign;

  if ((sign = n) < 0)
    n = -n;

  i = 0;

  do
  {
    s[i++] = n % 10 + '0';
  } while ((n /= 10) > 0);

  if (sign < 0)
  {
    s[i++] = '-';
  }

  s[i] = '\0';
  reverse(s);
}

#include "io.h"
#include "common.h"

void putchar(char ch);

void
puts(const char *s)
{
  while (*s)
  {
    putchar(*s++);
  }
}

void
printf(const char *fmt, ...)
{
  va_list vargs;
  va_start(vargs, fmt);

  while (*fmt)
  {
    if (*fmt == '%')
    {
      fmt++;

      int width = 0;
      int zero_pad = 0;
      int left_align = 0;

      while (1)
      {
        if (*fmt == '0') zero_pad = 1;
        else if (*fmt == '-') left_align = 1;
        else break;
        fmt++;
      }

      while (*fmt >= '0' && *fmt <= '9')
      {
          width = width * 10 + (*fmt++ - '0');
      }

      switch (*fmt)
      {
        case 'c':
        {
          char c = (char) va_arg(vargs, int);
          putchar(c);
          break;
        }

        case 's':
        {
          const char *s = va_arg(vargs, const char *);
          if (!s) s = "(null)";
          int len = strlen(s);

          if (width > len && !left_align)
          {
              for (int i = len; i < width; i++)
                  putchar(zero_pad ? '0' : ' ');
          }

          puts(s);

          if (width > len && left_align)
          {
              for (int i = len; i < width; i++)
                  putchar(' ');
          }

          break;
        }

        case 'd':
        case 'i':
        {
          int num = va_arg(vargs, int);
          char buffer[32];
          itoa(num, buffer);

          int len = strlen(buffer);

          if (width > len && !left_align)
          {
              for (int i = len; i < width; i++)
                  putchar(zero_pad ? '0' : ' ');
          }

          puts(buffer);

          if (width > len && left_align)
          {
              for (int i = len; i < width; i++)
                  putchar(' ');
          }
          break;
        }

        case 'u':
        {
          unsigned num = va_arg(vargs, unsigned);
          char buffer[32];
          itoa((int)num, buffer);

          int len = strlen(buffer);

          if (width > len && !left_align)
          {
              for (int i = len; i < width; i++)
                  putchar(zero_pad ? '0' : ' ');
          }

          puts(buffer);

          if (width > len && left_align)
          {
              for (int i = len; i < width; i++)
                  putchar(' ');
          }
          break;
        }

        case 'x':
        case 'X':
        {
          unsigned num = va_arg(vargs, unsigned);
          char buffer[32];
          char *p = buffer;
          const char *digits = (*fmt == 'X') ? "0123456789ABCDEF" : "0123456789abcdef";

          if (num == 0)
          {
              *p++ = '0';
          } else {
              while (num > 0)
              {
                  *p++ = digits[num & 0xF];
                  num >>= 4;
              }
          }

          *p = '\0';
          reverse(buffer);

          int len = strlen(buffer);
          if (width > len && !left_align)
          {
              for (int i = len; i < width; i++)
                  putchar(zero_pad ? '0' : ' ');
          }

          puts(buffer);

          if (width > len && left_align)
          {
              for (int i = len; i < width; i++)
                  putchar(' ');
          }
          break;
        }

        case '\0':
        {
          putchar('%');
          goto end;
        }

        case '%':
        {
          putchar('%');
          break;
        }

        default:
        {
            putchar('%');
            putchar(*fmt);
            break;
        }

      }
    }

    else
    {
      putchar(*fmt);
    }

    fmt++;
  }

end:
  va_end(vargs);
}

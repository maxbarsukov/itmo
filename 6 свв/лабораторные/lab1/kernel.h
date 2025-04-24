#pragma once

struct sbiret {
  long error;
  long value;
};

#define SBI_SUCCESS           0
#define SBI_ERR_FAILED        -1
#define SBI_ECALL_0_1_PUTCHAR 0x01
#define SBI_ECALL_0_1_GETCHAR 0x02
#define SBI_EXT_BASE          0x10
#define SBI_EXT_VER           0x00
#define SBI_EXT_PMU           0x504D55
#define SBI_EXT_CTR_NUM       0x00
#define SBI_EXT_CTR_DTLS      0x01
#define SBI_EXT_HSM           0x48534D
#define SBI_EXT_SRST          0x53525354
#define SBI_EXT_SHUTDOWN      0x00

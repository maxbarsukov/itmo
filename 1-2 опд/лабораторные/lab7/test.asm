ORG  0x1D0

RESULT: WORD 0x0

CHECK1: WORD 0x0
CHECK2: WORD 0x0
CHECK3: WORD 0x0

RES1: WORD 0x0
RES2: WORD 0xFFFF
RES3: WORD 0x7D9A

ARG1: WORD 0x0
ARG2: WORD 0x0

ARG3: WORD 0xAAAA
ARG4: WORD 0x5555

ARG5: WORD 0xAAFF
ARG6: WORD 0xD765

ORG 0x01E3
START:  CALL TEST1
        CALL TEST2
        CALL TEST3
        LD #0x1
        AND CHECK1
        AND CHECK2
        AND CHECK3
        ST RESULT
STOP:   HLT 

TEST1:  LD ARG1
        PUSH
        LD ARG2
        PUSH
        LD #0x77
        WORD 0x0F01 ; XORSP
        CMP #0x77
        BNE ERROR1
        POP
        ST CHECK1
        CMP RES1
        BEQ DONE1
ERROR1: POP
        POP
        CLA
        RET
DONE1:  POP 
        POP 
        LD #0x1
        ST CHECK1
        CLA 
        RET 

TEST2:  LD ARG3
        PUSH
        LD ARG4
        PUSH
        LD #0x77
        WORD 0x0F01 ; XORSP
        CMP #0x77
        BNE ERROR2
        POP
        ST CHECK2
        CMP RES2
        BEQ DONE2
ERROR2: POP
        POP
        CLA
        RET
DONE2:  POP 
        POP 
        LD #0x1
        ST CHECK2
        CLA 
        RET 

TEST3:  LD ARG5
        PUSH
        LD ARG6
        PUSH
        LD #0x77
        WORD 0x0F01 ; XORSP
        CMP #0x77
        BNE ERROR3
        POP
        ST CHECK3
        CMP RES3
        BEQ DONE3
ERROR3: POP
        POP
        CLA
        RET
DONE3:  POP 
        POP 
        LD #0x1
        ST CHECK3
        CLA 
        RET 
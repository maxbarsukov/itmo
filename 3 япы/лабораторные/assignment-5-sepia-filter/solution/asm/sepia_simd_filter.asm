;;; Sepia filter

; Coefficients for sepia filter
section .rodata
;;          | * b   | * g   | * r
;;          v   +   v   +   v
coef_r: dd 0.189, 0.769, 0.393, 0.0
coef_g: dd 0.168, 0.686, 0.349, 0.0
coef_b: dd 0.131, 0.534, 0.272, 0.0


section .text

;; Now: rdx = 256; rdi = address of current pixel ([rdi] = b, [rdi+1] = g, [rdi+2] = r)
;;
;; xmm0: coefficients for b
;; xmm1: coefficients for g
;; xmm2: coefficients for r
;; xmm3 const: current pixel + blue for next pixel
%macro sepia_for_color 2
  ; xmm4: temp for working with xmm3 values

  ; We are not changing xmm3 because we still need source colors for next transformations for each color
  movdqu xmm4, xmm3       ; xmm4 <- xmm3
  mulps  xmm4, %1         ; * xmmN here is like a mask that multiplies needed colors on coef and usless next blue on 0

  ; xmm4: (coef*b, coef*g, coef*r, 0*next_b = 0)
  ; Now we need to sum whole xmm4 to get new value of needed color

  ; xx = { xx3, xx2, xx1, xx0 }
  haddps xmm4, xmm4 ; xx = {xx3+xx2, xx1+xx0, xx3+xx2, xx1+xx0}
  haddps xmm4, xmm4 ; xx = {xx2+xx3+xx1+xx0, xx3+xx2+xx1+xx0, xx3+xx2+xx1+xx0, xx3+xx2+xx1+xx0}

  ; Extraxting sum from xmm4
  cvtps2dq xmm4, xmm4     ; floating point => packed 32-bit ints
  extractps rax, xmm4, 0  ; get first => get (xx2+xx3+xx1+xx0) => get sum

  ; if sum > 255, sum = 255
  cmp rax, rdx            ; Compare rax with 0xff = 255
  cmovg rax, rdx          ; Conditionally move rdx to rax if rax > 0xff
  mov byte[rdi + %2], al  ; write curremnt color byte
%endmacro

global make_sepia_with_simd

;; Transforms source image to sepia
;;
;; rdi: pointer to source image structure
make_sepia_with_simd:
  mov rsi, [rdi]        ; rsi <- height
  imul rsi, [rdi + 8]   ; rsi <- height * width
  imul rsi, 3           ; rsi <- height * width * pixel size, now rsi contains contents length

  mov rdi, [rdi + 16]   ; rdi <- start of data
  add rsi, rdi          ; rsi <- ptr to end of data (start of data + length of data)

  movdqu xmm2, [rel coef_r] ; <- mask for getting r
  movdqu xmm1, [rel coef_g] ; <- mask for getting g
  movdqu xmm0, [rel coef_b] ; <- mask for getting b

  ; Now: rdi = start of data, rsi = end of data, xmm0|1|2 contains multipliers
  ; for (rdi = start; rdi < rsi; rdi += 3) {
  .loop:
      ; grabbing 4 values
    ; /----\
    ; |bgrb|grbgrbgrbgrbgrbgrbgrbgr
    ;  ^  ^   ^
    ;  | and cycle starts from b
    movdqu xmm3, [rdi]      ; xmm3 <- 4 next values
    ; so we got bgr for current pixel and b from next one

    pmovzxbd xmm3, xmm3     ; BYTE => DWORD zero extension
    cvtdq2ps xmm3, xmm3     ; DWORD => floating point
    ; Now xmm3 contains bgrb
    ;                      L next pixel blue, we dont need it so we are multiplying it on 0 in any coef_X

    mov rdx, 0xff           ; rdx <- 255 as max color value to use it in `cmovg`
    sepia_for_color xmm2, 2 ; <- r = min(coef_r_b * cur.b + coef_r_g * cur.g + coef_r_r * cur.r + 0 * next.b, 255)
    sepia_for_color xmm1, 1 ; <- g = min(coef_g_b ...
    sepia_for_color xmm0, 0 ; <- b = ...

    add rdi, 3              ; counter += 3, going to next pixel
    cmp rdi, rsi            ; break if counter >= end of data
    jl .loop

  ret

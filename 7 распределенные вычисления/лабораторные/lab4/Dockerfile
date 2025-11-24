FROM ubuntu
RUN apt update && apt install -y clang
WORKDIR /pa4
COPY ./src .
COPY ./vendor/libruntime.so .
ENV LD_LIBRARY_PATH="/pa4"
RUN clang -std=c99 -Wall -pedantic *.c -o pa4 -L. -lruntime && \
    chmod +x pa4

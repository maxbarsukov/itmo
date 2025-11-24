DIR="pa4"

docker build -t $DIR .
docker run -it --rm $DIR /bin/bash

DIR="pa4"
NAME="$DIR.tar.gz"

rm $NAME
mkdir $DIR
cp ./src/*.{c,h} $DIR/
tar cfvz $NAME $DIR
rm -rf $DIR

#!/bin/bash

find . -type f | while read -r file; do
  filename=$(basename "$file")
  ext="${filename##*.}"
  if [[ "$ext" == "$filename" || "$ext" == "${ext,,}" ]]; then
    continue
  fi
  base="${filename%.*}"
  newname="${base}.${ext,,}"

  newpath="$(dirname "$file")/$newname"

  if [[ "$file" != "$newpath" ]]; then
    echo "Renaming: $file → $newpath"
    mv "$file" "$newpath"
  fi
done

#!/bin/bash

if [[ -z "$1" ]]; then
  echo "Использование: $0 /путь/к/директории"
  exit 1
fi

TARGET_DIR="$1"

if [[ ! -d "$TARGET_DIR" ]]; then
  echo "Ошибка: директория '$TARGET_DIR' не существует"
  exit 1
fi

find "$TARGET_DIR" -type f | while read -r file; do
  filename=$(basename "$file")
  ext="${filename##*.}"

  if [[ "$ext" == "$filename" ]]; then
    continue
  fi

  upper_ext="${ext^^}"
  base="${filename%.*}"
  newname="${base}.${upper_ext}"
  newpath="$(dirname "$file")/$newname"

  if [[ "$file" != "$newpath" ]]; then
    echo "Переименование: $file → $newpath"
    mv "$file" "$newpath"
  fi
done

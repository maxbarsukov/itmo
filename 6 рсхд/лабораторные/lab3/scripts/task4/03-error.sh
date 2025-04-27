#!/usr/bin/env bash

# Фиксируем время сбоя
SCHEMA_TIME=$(psql -h localhost -p 9114 -U postgres1 -d fakebrownroad -t -c "SELECT now();")
echo "Цель восстановления: $SCHEMA_TIME"

psql -h localhost -p 9114 -U data_user -d fakebrownroad -c "DELETE FROM table1 WHERE id % 2 = 1;"

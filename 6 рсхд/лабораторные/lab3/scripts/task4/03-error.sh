#!/usr/bin/env bash

# Фиксируем время сбоя
SCHEMA_TIME=$(psql -h localhost -p 9114 -U postgres1 -d fakebrownroad -t -c "SELECT now();")
psql -h localhost -p 9114 -U data_user -d fakebrownroad -c "BEGIN; DELETE FROM table1 WHERE id % 2 = 1; COMMIT;"

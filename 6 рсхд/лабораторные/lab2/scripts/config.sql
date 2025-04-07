-- Принимать подключения с любого IP-адреса
ALTER SYSTEM SET listen_addresses = '*';
-- Номер порта по варианту
ALTER SYSTEM SET port = 9114;

ALTER SYSTEM SET max_connections = 20;
ALTER SYSTEM SET superuser_reserved_connections = 5;
ALTER SYSTEM SET shared_buffers = '40MB';
ALTER SYSTEM SET work_mem = '1MB';
ALTER SYSTEM SET temp_buffers = '4MB';
ALTER SYSTEM SET effective_cache_size = '100MB';
ALTER SYSTEM SET maintenance_work_mem = '20MB';

ALTER SYSTEM SET wal_level = 'replica';
ALTER SYSTEM SET wal_buffers = '1MB';
ALTER SYSTEM SET wal_compression = 'on';
ALTER SYSTEM SET max_wal_size = '100MB';
-- min_wal_size должен быть минимум вдвое больше wal_segment_size (16MB)
ALTER SYSTEM SET min_wal_size = '40MB';
ALTER SYSTEM SET checkpoint_timeout = '5min';
ALTER SYSTEM SET checkpoint_completion_target = 0.9;
ALTER SYSTEM SET fsync = on;
ALTER SYSTEM SET synchronous_commit = 'remote_write';
ALTER SYSTEM SET wal_log_hints = on;
ALTER SYSTEM SET commit_delay = 1000;
ALTER SYSTEM SET commit_siblings = 2;
ALTER SYSTEM SET default_statistics_target = 50;
ALTER SYSTEM SET random_page_cost = 1.1;
ALTER SYSTEM SET effective_io_concurrency = 2;
ALTER SYSTEM SET max_worker_processes = 2;
ALTER SYSTEM SET max_parallel_workers_per_gather = 0;
ALTER SYSTEM SET bgwriter_delay = '100ms';
ALTER SYSTEM SET bgwriter_lru_maxpages = 100;
ALTER SYSTEM SET bgwriter_lru_multiplier = 2.0;

ALTER SYSTEM SET log_destination = 'csvlog';
ALTER SYSTEM SET logging_collector = on;
ALTER SYSTEM SET log_filename = 'postgresql-%Y-%m-%d_%H%M%S.csv';
ALTER SYSTEM SET log_min_messages = 'notice';
ALTER SYSTEM SET log_connections = on;      -- Начало сессии
ALTER SYSTEM SET log_disconnections = on;   -- Завершение сессии
ALTER SYSTEM SET log_duration = on;         -- Время выполнения команд
ALTER SYSTEM SET log_min_duration_statement = 0;-- Логировать все запросы
ALTER SYSTEM SET log_checkpoints = on;
ALTER SYSTEM SET log_lock_waits = on;
ALTER SYSTEM SET deadlock_timeout = '1s';
ALTER SYSTEM SET idle_in_transaction_session_timeout = '1min';
ALTER SYSTEM SET statement_timeout = '30s';
ALTER SYSTEM SET lock_timeout = '10s';

ALTER USER postgres1 WITH PASSWORD '1234';

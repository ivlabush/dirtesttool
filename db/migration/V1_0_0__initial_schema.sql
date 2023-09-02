CREATE TABLE IF NOT EXISTS public.tasks (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(16),
    base_url VARCHAR(2084)
);

CREATE TABLE IF NOT EXISTS public.task_results (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    task_id VARCHAR(36),
    url VARCHAR(4086),
    status_code INTEGER,
    CONSTRAINT fk_task_results_task FOREIGN KEY (task_id) REFERENCES public.tasks(id)
);

CREATE TABLE IF NOT EXISTS public.errors (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    task_id VARCHAR(36),
    url VARCHAR(4086),
    status_code INTEGER,
    message VARCHAR(200),
    stacktrace VARCHAR(2000),
    CONSTRAINT fk_errors_task FOREIGN KEY (task_id) REFERENCES public.tasks(id)
)
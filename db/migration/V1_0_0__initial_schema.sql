CREATE TABLE IF NOT EXISTS public.tasks (
    id VARCHAR NOT NULL PRIMARY KEY,
    name VARCHAR,
    base_url VARCHAR
);

CREATE TABLE IF NOT EXISTS public.task_results (
    id VARCHAR NOT NULL PRIMARY KEY,
    task_id VARCHAR NOT NULL,
    url VARCHAR ,
    status_code INTEGER,
    CONSTRAINT fk_task_results_task FOREIGN KEY (task_id) REFERENCES public.tasks(id)
);

CREATE TABLE IF NOT EXISTS public.errors (
    id VARCHAR NOT NULL PRIMARY KEY,
    task_id VARCHAR NOT NULL,
    url VARCHAR,
    status_code INTEGER,
    message VARCHAR,
    stacktrace VARCHAR,
    CONSTRAINT fk_errors_task FOREIGN KEY (task_id) REFERENCES public.tasks(id)
)
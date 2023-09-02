CREATE TABLE IF NOT EXISTS public.tasks (
    id varchar(36) NOT NULL PRIMARY KEY,
    name VARCHAR(16),
    base_url VARCHAR(2084)
);

CREATE TABLE IF NOT EXISTS public.task_results (
    id varchar(36) NOT NULL PRIMARY KEY,
    task_id VARCHAR(36),
    url VARCHAR(4086),
    status_code VARCHAR(40),
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES public.tasks(id)
)
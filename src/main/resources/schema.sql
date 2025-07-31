create table departments
(
    id               bigserial
        primary key,
    created_at       timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_at       timestamp with time zone default CURRENT_TIMESTAMP,
    name             varchar(100)                                       not null
        unique,
    description      text                                               not null,
    established_date date                                               not null
);

alter table departments
    owner to hrbank_user;

create table files
(
    id         bigserial
        primary key,
    filename   varchar(50)                                        not null,
    type       varchar(50)                                        not null,
    size       bigint                                             not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null
);

alter table files
    owner to hrbank_user;

create table employees
(
    id              bigserial
        primary key,
    name            varchar(100)                                       not null,
    email           varchar(100)                                       not null
        unique,
    position        varchar(20)                                        not null,
    hire_date       date                                               not null,
    status          varchar(20)
        constraint employees_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ACTIVE'::character varying, 'ON_LEAVE'::character varying, 'RESIGNED'::character varying])::text[])),
    employee_number varchar(50)
        unique,
    created_at      timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_at      timestamp with time zone default CURRENT_TIMESTAMP,
    department_id   bigint
                                                                       references departments
                                                                           on delete set null,
    profile_id      bigint
                                                                       references files
                                                                           on delete set null
);

alter table employees
    owner to hrbank_user;

create table backups
(
    id         bigserial
        primary key,
    worker     varchar(45)                                        not null,
    status     varchar(20)                                        not null
        constraint backups_status_check
            check ((status)::text = ANY
                   ((ARRAY ['IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'SKIPPED'::character varying, 'FAILED'::character varying])::text[])),
    started_at timestamp with time zone                           not null,
    ended_at   timestamp with time zone                           not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    file_id    bigint
        references files
            on delete cascade
);

alter table backups
    owner to hrbank_user;

create table change_logs
(
    id              bigserial
        primary key,
    type            varchar(20)                                        not null
        constraint change_logs_type_check
            check ((type)::text = ANY
                   ((ARRAY ['CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying])::text[])),
    employee_number varchar(50)                                        not null,
    memo            text,
    ip_address      varchar(45)                                        not null,
    created_at      timestamp with time zone default CURRENT_TIMESTAMP not null
);

alter table change_logs
    owner to hrbank_user;

create table employee_change_details
(
    id         bigserial
        primary key,
    field_name varchar(100)                                       not null,
    old_value  text,
    new_value  text,
    log_id     bigint
        references change_logs
            on delete cascade,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null
);

alter table employee_change_details
    owner to hrbank_user;

alter table employees
drop constraint employees_department_id_fkey;



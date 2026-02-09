

--09/02/2026

CREATE TABLE roles (
    id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,   -- ADMIN, EDITOR, VIEWER
    description VARCHAR(200)
);

CREATE TABLE permissions (
    id BIGINT IDENTITY PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,   -- ARTICLE_UPLOAD, USER_MANAGE
    description VARCHAR(200)
);

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users (
    id BIGINT IDENTITY PRIMARY KEY,

    user_code VARCHAR(50) NOT NULL UNIQUE,     -- BH-2024-1234
    azure_object_id VARCHAR(100) NULL,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    location VARCHAR(150),

    job_title VARCHAR(150),
    department VARCHAR(150),
    date_joined DATE,

    active BIT NOT NULL DEFAULT 1,   -- 1 = active, 0 = inactive

    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    last_login DATETIME2
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE articles (
    id BIGINT IDENTITY PRIMARY KEY,

    title VARCHAR(255) NOT NULL,
    source VARCHAR(150),                 -- WHO, MOH, FDA, etc
    country VARCHAR(100) NOT NULL,       -- Region / Country
    article_type VARCHAR(100),           -- Regulation, Policy, Circular
    clinical_type VARCHAR(100),           -- Clinical / Non-clinical

    status VARCHAR(50) NOT NULL,          -- DRAFT / PUBLISHED / ARCHIVED

    file_name VARCHAR(255) NOT NULL,      -- actual file name
    file_path VARCHAR(500) NOT NULL,      -- Azure shared folder path
    file_size BIGINT,                     -- bytes

    uploaded_by BIGINT NOT NULL,          -- users.id (logical relation)
    uploaded_at DATETIME2 DEFAULT GETDATE(),

    active BIT NOT NULL DEFAULT 1          -- soft delete
);

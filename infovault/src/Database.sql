

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
-------09/02/2026---------------------------------------------------------------------------------------
CREATE TABLE user_login_logs (
    id BIGINT IDENTITY PRIMARY KEY,

    user_id BIGINT NOT NULL,         -- users.id
    login_time DATETIME2 NOT NULL,
    logout_time DATETIME2 NULL,      -- NULL = still logged in

    ip_address VARCHAR(50),
    user_agent VARCHAR(255),

    created_at DATETIME2 DEFAULT GETDATE()
);
-----------------------------------------11-02-2026---------------------------------------------------------
ALTER TABLE [infovault].[dbo].[articles]
ADD article_content NVARCHAR(MAX) NULL;

CREATE TABLE countries (
    id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    active BIT NOT NULL DEFAULT 1
);


CREATE TABLE update_types (
    id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    active BIT NOT NULL DEFAULT 1
);

CREATE TABLE impact_levels (
    id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    active BIT NOT NULL DEFAULT 1
);

CREATE TABLE clinical_types (
    id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    active BIT NOT NULL DEFAULT 1
);

DROP TABLE article;

CREATE TABLE articles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,

    title NVARCHAR(255) NOT NULL,

    source_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    update_type_id BIGINT NOT NULL,
    clinical_type_id BIGINT NOT NULL,

    article_content NVARCHAR(MAX),

    status NVARCHAR(50),

    file_name NVARCHAR(255),
    file_size BIGINT,

    uploaded_by NVARCHAR(100),

    created_at DATETIME2 DEFAULT SYSDATETIME(),
    updated_at DATETIME2 DEFAULT SYSDATETIME(),

    active BIT DEFAULT 1
);

----------------13-02-2026--------------------------------
CREATE TABLE sources (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    active BIT NOT NULL DEFAULT 1
);
ALTER TABLE sources
ADD created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME();
ALTER TABLE sources
ADD updated_at DATETIME2 NOT NULL DEFAULT SYSDATETIME();

----------------16-02-2026--------------------------------

ALTER TABLE articles
ADD created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME();


----------------17-02-2026--------------------------------
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    active BIT DEFAULT 1,
    role_id BIGINT NOT NULL, -- logical reference to roles.id (no FK)
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);


CREATE TABLE roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE, -- ADMIN, USER, MANAGER
    description VARCHAR(255),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);


CREATE TABLE permissions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE, -- HOME_VIEW, REPORTS_VIEW
    description VARCHAR(255),
    created_at DATETIME2 DEFAULT GETDATE()
);


CREATE TABLE role_permissions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    role_id BIGINT NOT NULL,       -- logical reference to roles.id
    permission_id BIGINT NOT NULL  -- logical reference to permissions.id
);


INSERT INTO roles (name, description)
VALUES
('ADMIN', 'Full system access'),
('USER', 'Basic user access');


INSERT INTO permissions (code, description)
VALUES
('HOME_VIEW', 'Access Home Tab'),
('RCM_UPDATES', 'Access Dashboard'),
('CASE_MANAGEMENT', 'Access Reports'),
('USER_MANAGEMENT', 'Manage Users'),
('COMMUNICATIONS', 'Manage COMMUNICATIONS Users'),
('ARTICLES','Manage Articles');

--giving admin all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ADMIN';

--giving user only home and dashboard view permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p
    ON p.code IN ('HOME_VIEW', 'RCM_UPDATES','CASE_MANAGEMENT')
WHERE r.name = 'USER';
--insert admin
INSERT INTO users (email, first_name, last_name, active, role_id)
SELECT
    'admin@infovault.com',
    'System',
    'Admin',
    1,
    r.id
FROM roles r
WHERE r.name = 'ADMIN';

ALTER TABLE [infovault].[dbo].[articles]
ADD impact_level_id BigInt;

-- GRADI database initialization script
-- recommended setup is on application.properties file

-- Encoding
SET client_encoding = 'UTF8';

-- Create tables
CREATE TABLE gradi_messages
(
    code character varying(10) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    subject character varying(255),
    text text,
    type integer,
    visibility integer
);
CREATE TABLE gradi_messages_recipients
(
    message_code character varying(10) NOT NULL,
    user_code character varying(10) NOT NULL
);
CREATE TABLE gradi_resource_attributes
(
    name character varying(255) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    value character varying(255),
    resource_property_name character varying(255) NOT NULL,
    resource_code character varying(10) NOT NULL
);
CREATE TABLE gradi_resource_files
(
    code character varying(10) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    name character varying(255) NOT NULL,
    resource_code character varying(10) NOT NULL
);
CREATE TABLE gradi_resource_permissions
(
    index bigint NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    type integer,
    resource_code character varying(10) NOT NULL,
    user_code character varying(10) NOT NULL
);
CREATE TABLE gradi_resource_properties
(
    name character varying(255) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    type integer NOT NULL,
    resource_type_code character varying(10) NOT NULL
);
CREATE TABLE gradi_resource_types
(
    code character varying(10) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    brief text,
    description text,
    name character varying(255) NOT NULL
);
CREATE TABLE gradi_resources
(
    code character varying(10) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    description text,
    name character varying(255) NOT NULL,
    visibility integer NOT NULL,
    parent_resource_code character varying(10),
    resource_type_code character varying(10)
);
CREATE TABLE gradi_users
(
    code character varying(10) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    description text,
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    role integer NOT NULL,
    surname character varying(255) NOT NULL
);


-- Set tables' permissions
ALTER TABLE gradi_messages OWNER TO postgres;
ALTER TABLE gradi_messages_recipients OWNER TO postgres;
ALTER TABLE gradi_resource_attributes OWNER TO postgres;
ALTER TABLE gradi_resource_files OWNER TO postgres;
ALTER TABLE gradi_resource_permission_sequence OWNER TO postgres;
ALTER TABLE gradi_resource_permissions OWNER TO postgres;
ALTER TABLE gradi_resource_properties OWNER TO postgres;
ALTER TABLE gradi_resource_types OWNER TO postgres;
ALTER TABLE gradi_resources OWNER TO postgres;
ALTER TABLE gradi_users OWNER TO postgres;


-- Create sequences
CREATE SEQUENCE gradi_resource_permission_sequence START WITH 1 INCREMENT BY 50 NO MINVALUE NO MAXVALUE CACHE 1;
SELECT pg_catalog.setval('gradi_resource_permission_sequence', 1, false);


-- Create primary keys
ALTER TABLE ONLY gradi_messages ADD CONSTRAINT gradi_messages_pkey PRIMARY KEY (code);
ALTER TABLE ONLY gradi_messages_recipients ADD CONSTRAINT gradi_messages_recipients_pkey PRIMARY KEY (message_code, user_code);
ALTER TABLE ONLY gradi_resource_attributes ADD CONSTRAINT gradi_resource_attributes_pkey PRIMARY KEY (name);
ALTER TABLE ONLY gradi_resource_files ADD CONSTRAINT gradi_resource_files_pkey PRIMARY KEY (code);
ALTER TABLE ONLY gradi_resource_permissions ADD CONSTRAINT gradi_resource_permissions_pkey PRIMARY KEY (index);
ALTER TABLE ONLY gradi_resource_properties ADD CONSTRAINT gradi_resource_properties_pkey PRIMARY KEY (name);
ALTER TABLE ONLY gradi_resource_types ADD CONSTRAINT gradi_resource_types_pkey PRIMARY KEY (code);
ALTER TABLE ONLY gradi_resources ADD CONSTRAINT gradi_resources_pkey PRIMARY KEY (code);
ALTER TABLE ONLY gradi_users ADD CONSTRAINT gradi_users_pkey PRIMARY KEY (code);


-- Create unique keys
ALTER TABLE ONLY gradi_users ADD CONSTRAINT uk_ecpe2bu618juq1qlweh8feg0 UNIQUE (email);


-- Create foreign keys
ALTER TABLE ONLY gradi_resource_permissions ADD CONSTRAINT fk1hwkmi6odyl1vh41r38kdgdm1 FOREIGN KEY (user_code) REFERENCES gradi_users(code);
ALTER TABLE ONLY gradi_resources ADD CONSTRAINT fk4qfe7lm3w4cu7cuivw5aa407o FOREIGN KEY (parent_resource_code) REFERENCES gradi_resources(code);
ALTER TABLE ONLY gradi_messages_recipients ADD CONSTRAINT fk4qoscgmi3hxxcmr3fah1w6csm FOREIGN KEY (user_code) REFERENCES gradi_users(code);
ALTER TABLE ONLY gradi_resource_attributes ADD CONSTRAINT fk6u32pgxdp22eun8i5p4xhotex FOREIGN KEY (resource_code) REFERENCES gradi_resources(code);
ALTER TABLE ONLY gradi_messages_recipients ADD CONSTRAINT fke5bq53qlrctdb6whbrx50d5yu FOREIGN KEY (message_code) REFERENCES gradi_messages(code);
ALTER TABLE ONLY gradi_resource_permissions ADD CONSTRAINT fkfa5n157xc6wa81g0c29i3gd1i FOREIGN KEY (resource_code) REFERENCES gradi_resources(code);
ALTER TABLE ONLY gradi_resource_attributes ADD CONSTRAINT fkj9vjqok2kb8bo83e4l6l8n9u1 FOREIGN KEY (resource_property_name) REFERENCES gradi_resource_properties(name);
ALTER TABLE ONLY gradi_resource_files ADD CONSTRAINT fkmbhyhljf4h1fm7nroqx1svjoc FOREIGN KEY (resource_code) REFERENCES gradi_resources(code);
ALTER TABLE ONLY gradi_resource_properties ADD CONSTRAINT fkpgawmj58bmkgbo5jmrnii24we FOREIGN KEY (resource_type_code) REFERENCES gradi_resource_types(code);
ALTER TABLE ONLY gradi_resources ADD CONSTRAINT fks2he8i41qt4l89d2e5e6h8maq FOREIGN KEY (resource_type_code) REFERENCES gradi_resource_types(code);


-- Create admin data
-- email and password will be set at software's first start
INSERT INTO gradi_users(code, created_at, updated_at, name, surname, email, password, role, description)
VALUES('0000000000', now(), now(), 'Administrator', 'Administrator', 'admin@domain', 'password', 2, 'System''s administrator');

-- Create 'Generic' resource type
INSERT INTO gradi_resource_types(code, created_at, updated_at, name, brief)
VALUES('0000000001', now(), now(), 'Generic', 'Generic resources');
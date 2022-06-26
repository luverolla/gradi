CREATE DOMAIN public.permission_type AS character varying(5)
	COLLATE "default"
	CONSTRAINT CHECK (VALUE::text = 'own'::text OR VALUE::text = 'write'::text OR VALUE::text = 'read'::text);

CREATE DOMAIN public.property_type AS character varying
	COLLATE "default"
	CONSTRAINT CHECK (VALUE::text = 'string'::text OR VALUE::text = 'text'::text OR VALUE::text = 'numeric'::text OR VALUE::text = 'datetime'::text OR VALUE::text = 'fixed'::text OR VALUE::text = 'resource'::text);

CREATE DOMAIN public.resource_visibility AS character varying
	COLLATE "default"
	CONSTRAINT CHECK (VALUE::text = 'public'::text OR VALUE::text = 'internal'::text OR VALUE::text = 'restricted'::text);

CREATE DOMAIN public.user_role AS character varying(8)
	COLLATE "default"
	CONSTRAINT CHECK (VALUE::text = 'user'::text OR VALUE::text = 'operator'::text OR VALUE::text = 'admin'::text);

-- Drop table

-- DROP TABLE public.message;

CREATE TABLE public.message (
	code int4 NOT NULL,
	subject varchar NOT NULL,
	datetime timestamptz NOT NULL,
	"text" text NOT NULL DEFAULT ''::text,
	CONSTRAINT message_pk PRIMARY KEY (code)
);

-- Permissions

ALTER TABLE public.message OWNER TO gradi;
GRANT ALL ON TABLE public.message TO gradi;


-- public."type" definition

-- Drop table

-- DROP TABLE public."type";

CREATE TABLE public."type" (
	code int4 NOT NULL,
	"name" varchar NOT NULL,
	"description" text NOT NULL DEFAULT ''::text,
	created_at timestamptz NOT NULL,
	updated_at timestamptz NOT NULL,
	CONSTRAINT type_pk PRIMARY KEY (code)
);

-- Table Triggers

create trigger type_trig_created_at before
insert
    on
    public.type for each row execute function set_created_at();
create trigger type_trig_updated_at before
update
    on
    public.type for each row execute function set_updated_at();

-- Permissions

ALTER TABLE public."type" OWNER TO gradi;
GRANT ALL ON TABLE public."type" TO gradi;


-- public."user" definition

-- Drop table

-- DROP TABLE public."user";

CREATE TABLE public."user" (
	code int4 NOT NULL,
	"name" varchar NOT NULL,
	surname varchar NOT NULL,
	email varchar NOT NULL,
    "password" text NOT NULL,
	"role" public.user_role NOT NULL,
	created_at timestamptz NOT NULL,
	updated_at timestamptz NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (code)
);
CREATE UNIQUE INDEX user_email_idx ON public."user" USING btree (email);

-- Table Triggers

create trigger user_trig_created_at before
insert
    on
    public."user" for each row execute function set_created_at();
create trigger user_trig_updated_at before
update
    on
    public."user" for each row execute function set_updated_at();

-- Permissions

ALTER TABLE public."user" OWNER TO gradi;
GRANT ALL ON TABLE public."user" TO gradi;


-- public.property definition

-- Drop table

-- DROP TABLE public.property;

CREATE TABLE public.property (
	"name" varchar NOT NULL,
	data_type public.property_type NOT NULL,
	"type" int4 NOT NULL,
	created_at timestamptz NOT NULL,
	updated_at timestamptz NOT NULL,
	description text NOT NULL,
	CONSTRAINT property_pk PRIMARY KEY (type, name),
	CONSTRAINT property_fk_type FOREIGN KEY ("type") REFERENCES public."type"(code) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Table Triggers

create trigger property_trig_created_at before
insert
    on
    public.property for each row execute function set_created_at();
create trigger property_trig_updated_at before
update
    on
    public.property for each row execute function set_updated_at();

-- Permissions

ALTER TABLE public.property OWNER TO gradi;
GRANT ALL ON TABLE public.property TO gradi;


-- public.resource definition

-- Drop table

-- DROP TABLE public.resource;

CREATE TABLE public.resource (
	code int4 NOT NULL,
	"type" int4 NOT NULL,
	title varchar NOT NULL,
	description text NOT NULL,
	created_at timestamptz NOT NULL,
	updated_at timestamptz NOT NULL,
	parent int4 NULL,
	visibility public.resource_visibility NOT NULL,
	CONSTRAINT resource_pk PRIMARY KEY (code),
	CONSTRAINT resource_fk FOREIGN KEY (parent) REFERENCES public.resource(code) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT resource_fk_type FOREIGN KEY ("type") REFERENCES public."type"(code) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Table Triggers

create trigger resource_trig_created_at before
insert
    on
    public.resource for each row execute function set_created_at();
create trigger resource_trig_updated_at before
update
    on
    public.resource for each row execute function set_updated_at();

-- Permissions

ALTER TABLE public.resource OWNER TO gradi;
GRANT ALL ON TABLE public.resource TO gradi;


-- public."attribute" definition

-- Drop table

-- DROP TABLE public."attribute";

CREATE TABLE public."attribute" (
	"number" int2 NOT NULL,
	resource int4 NOT NULL,
	value text NOT NULL,
	created_at timestamptz NOT NULL,
	updated_at timestamptz NOT NULL,
	property_type int4 NOT NULL,
	property_name varchar NOT NULL,
	CONSTRAINT attribute_pk PRIMARY KEY (resource, property_type, property_name, number),
	CONSTRAINT attribute_fk_property FOREIGN KEY (property_type,property_name) REFERENCES public.property("type","name") ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT attribute_fk_resource FOREIGN KEY (resource) REFERENCES public.resource(code) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Table Triggers

create trigger attribute_trig_created_at before
insert
    on
    public.attribute for each row execute function set_created_at();
create trigger attribute_trig_updated_at before
update
    on
    public.attribute for each row execute function set_updated_at();

-- Permissions

ALTER TABLE public."attribute" OWNER TO gradi;
GRANT ALL ON TABLE public."attribute" TO gradi;


-- public.recipient definition

-- Drop table

-- DROP TABLE public.recipient;

CREATE TABLE public.recipient (
	message int4 NOT NULL,
	"user" int4 NOT NULL,
	CONSTRAINT recipient_pk PRIMARY KEY (message, "user"),
	CONSTRAINT recipient_fk_message FOREIGN KEY (message) REFERENCES public.message(code) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT recipient_fk_user FOREIGN KEY ("user") REFERENCES public."user"(code) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Permissions

ALTER TABLE public.recipient OWNER TO gradi;
GRANT ALL ON TABLE public.recipient TO gradi;


-- public."permission" definition

-- Drop table

-- DROP TABLE public."permission";

CREATE TABLE public."permission" (
	resource int4 NOT NULL,
	"user" int4 NOT NULL,
	"type" public.permission_type NOT NULL,
	created_at timestamptz NOT NULL,
	updated_at timestamptz NOT NULL,
	CONSTRAINT permission_pk PRIMARY KEY (resource, "user"),
	CONSTRAINT permission_fk_resource FOREIGN KEY (resource) REFERENCES public.resource(code) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT permission_fk_user FOREIGN KEY ("user") REFERENCES public."user"(code) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Table Triggers

create trigger permission_trig_created_at before
insert
    on
    public.permission for each row execute function set_created_at();
create trigger permission_trig_updated_at before
update
    on
    public.permission for each row execute function set_updated_at();

-- Permissions

ALTER TABLE public."permission" OWNER TO gradi;
GRANT ALL ON TABLE public."permission" TO gradi;



CREATE OR REPLACE FUNCTION public.set_created_at()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
	BEGIN
		new.created_at := now();
		new.updated_at := now();
		return new;
	END;
$function$
;

-- Permissions

ALTER FUNCTION public.set_created_at() OWNER TO gradi;
GRANT ALL ON FUNCTION public.set_created_at() TO public;
GRANT ALL ON FUNCTION public.set_created_at() TO gradi;
GRANT ALL ON FUNCTION public.set_created_at() TO postgres;

CREATE OR REPLACE FUNCTION public.set_updated_at()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
	BEGIN
		new.updated_at := now();
	
		if new.created_at <> old.created_at then
			new.created_at := old.created_at;
		end if;
	
		return new;
	END;
$function$
;

-- Inserts

INSERT INTO public.user (code, "name", surname, email, "password", "role", created_at, updated_at)
VALUES (0, "Admin", "Admin", "admin@domain", "$2a$12$R7Iz//wf29WYD4oGn0bJ7OKlKOO.PHMr94MA.vXsvRHyybXPWWoNu", "admin", now()::timestamptz, now()::timestamptz);

INSERT INTO public.type (code, "name", "description", created_at, updated_at)
VALUES (0, "Generic", "Generic resource type", now()::timestamptz, now()::timestamptz);

-- Permissions

ALTER FUNCTION public.set_updated_at() OWNER TO gradi;
GRANT ALL ON FUNCTION public.set_updated_at() TO public;
GRANT ALL ON FUNCTION public.set_updated_at() TO gradi;
GRANT ALL ON FUNCTION public.set_updated_at() TO postgres;


-- Permissions

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
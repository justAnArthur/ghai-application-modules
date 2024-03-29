CREATE TABLE "User" (
  "id" uuid PRIMARY KEY,
  "confirmed" boolean,
  "email" varchar,
  "password" varchar,
  "role" varchar
);

CREATE TABLE "Admin" (
  "id" uuid PRIMARY KEY,
  "name" varchar,
  "user_id" uuid,
  "responsible_for_region" varchar
);

CREATE TABLE "Coworker" (
  "id" uuid PRIMARY KEY,
  "name" varchar,
  "user_id" uuid,
  "created_by" uuid,
  "prefered_countries" varchar[]
);

CREATE TABLE "Client" (
  "id" uuid PRIMARY KEY,
  "user_id" uuid,
  "registration_date" date,
  "iname" varchar,
  "last_name" varchar,
  "date_of_birth" date,
  "region_in_new_country" varchar,
  "country" varchar
);

CREATE TABLE "DocumentRequest" (
  "id" uuid PRIMARY KEY,
  "status" varchar,
  "requested_template_id" uuid,
  "client_id" uuid,
  "reviewed_by_id" uuid,
  "generated_document_id" uuid,
  "feedback" varchar
);

CREATE TABLE "EventWatcher" (
  "id" uuid PRIMARY KEY,
  "request_id" uuid,
  "created_at" date,
  "previous_state" varchar
);

CREATE TABLE "Document" (
  "id" uuid PRIMARY KEY,
  "owned_by_id" uuid,
  "name" varchar,
  "path" varchar
);

CREATE TABLE "DocumentFields" (
  "id" uuid PRIMARY KEY,
  "request_id" uuid,
  "template_field_id" uuid,
  "value" varchar
);

CREATE TABLE "TemplateField" (
  "id" uuid PRIMARY KEY,
  "template_id" uuid,
  "name" varchar,
  "type" varchar,
  "required" boolean
);

CREATE TABLE "Template" (
  "id" uuid PRIMARY KEY,
  "name" varchar,
  "file_path" varchar,
  "required_templates_generated_ids" uuid[]
);

COMMENT ON COLUMN "User"."email" IS '
Is Email confirmed. But requires Email service and server.
';

COMMENT ON COLUMN "Client"."region_in_new_country" IS 'Допустим, для меня это - Братислава';

COMMENT ON COLUMN "DocumentRequest"."feedback" IS 'Заполняет, если что-то не понравилось, либо просто заметки. Клиент это будет видеть, только, когда статут != approved';

COMMENT ON COLUMN "EventWatcher"."previous_state" IS 'да, как, JSON, в любом случае только для отображения Админу';

COMMENT ON COLUMN "Document"."path" IS 'To file in a system';

COMMENT ON COLUMN "DocumentFields"."value" IS '
Ибо в любом случае, чтобы добавить что-то на изображение, либо PDF - его нужно серелизоровать.
';

COMMENT ON COLUMN "Template"."file_path" IS '
Будь то изображение (преуказ, виза, как документ), будьто генерированная справка.
';

ALTER TABLE "Admin" ADD FOREIGN KEY ("user_id") REFERENCES "User" ("id");

ALTER TABLE "Coworker" ADD FOREIGN KEY ("user_id") REFERENCES "User" ("id");

ALTER TABLE "Coworker" ADD FOREIGN KEY ("created_by") REFERENCES "Admin" ("id");

ALTER TABLE "Client" ADD FOREIGN KEY ("user_id") REFERENCES "User" ("id");

ALTER TABLE "DocumentRequest" ADD FOREIGN KEY ("requested_template_id") REFERENCES "Template" ("id");

ALTER TABLE "DocumentRequest" ADD FOREIGN KEY ("client_id") REFERENCES "Client" ("id");

ALTER TABLE "DocumentRequest" ADD FOREIGN KEY ("reviewed_by_id") REFERENCES "Coworker" ("id");

ALTER TABLE "DocumentRequest" ADD FOREIGN KEY ("generated_document_id") REFERENCES "Document" ("id");

ALTER TABLE "EventWatcher" ADD FOREIGN KEY ("request_id") REFERENCES "DocumentRequest" ("id");

ALTER TABLE "Document" ADD FOREIGN KEY ("owned_by_id") REFERENCES "Client" ("id");

ALTER TABLE "DocumentFields" ADD FOREIGN KEY ("request_id") REFERENCES "DocumentRequest" ("id");

ALTER TABLE "DocumentFields" ADD FOREIGN KEY ("template_field_id") REFERENCES "TemplateField" ("id");

ALTER TABLE "TemplateField" ADD FOREIGN KEY ("template_id") REFERENCES "Template" ("id");

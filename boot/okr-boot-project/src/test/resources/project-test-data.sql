SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE PROJECT;
ALTER TABLE PROJECT ALTER COLUMN ID RESTART WITH 1;
SET REFERENTIAL_INTEGRITY TRUE;

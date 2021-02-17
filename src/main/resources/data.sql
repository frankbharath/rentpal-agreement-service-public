ALTER TABLE IF EXISTS property ADD COLUMN IF NOT EXISTS propertysearch_tsv tsvector;

CREATE INDEX IF NOT EXISTS property_search_index ON property USING gin(propertysearch_tsv);

CREATE OR REPLACE FUNCTION property_trigger() RETURNS trigger AS '
    begin
        new.propertysearch_tsv :=
                                setweight(to_tsvector(''pg_catalog.english'', coalesce(new.property_name,'''')), ''A'') ||
                                setweight(to_tsvector(''pg_catalog.english'', coalesce(new.city,'''')), ''A'') ||
                                setweight(to_tsvector(''pg_catalog.english'', coalesce(new.postal,'''')), ''B'') ||
                                setweight(to_tsvector(''pg_catalog.english'', coalesce(new.address_line1,'''')), ''C'') ||
                                setweight(to_tsvector(''pg_catalog.english'', coalesce(new.address_line2,'''')), ''C'');
        return new;
    end;
' LANGUAGE plpgsql;

DO
'
BEGIN
    IF NOT EXISTS(SELECT *
                  FROM information_schema.triggers
                  WHERE event_object_table = ''property''
                    AND trigger_name = ''tsvectorupdateforproperty''
        )
    THEN
        CREATE TRIGGER tsvectorupdateforproperty BEFORE INSERT OR UPDATE ON property FOR EACH ROW EXECUTE PROCEDURE property_trigger();
    END IF;
END;
'



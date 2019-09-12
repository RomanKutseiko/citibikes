ALTER TABLE station ADD COLUMN coordinates POINT DEFAULT POINT(0, 0) NOT NULL;
UPDATE station SET coordinates = POINT(longitude, latitude);
ALTER TABLE station DROP COLUMN longitude;
ALTER TABLE station DROP COLUMN latitude;

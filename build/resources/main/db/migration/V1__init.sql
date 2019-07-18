CREATE TABLE AppUser(
    id BIGSERIAL PRIMARY KEY,
    user_type VARCHAR(15) DEFAULT 'SUBSCRIBER',
    gender SMALLINT DEFAULT 0,
    birthday date,
    email TEXT
);

CREATE TABLE Bike(
     id BIGSERIAL PRIMARY KEY,
     station_id BIGINT NOT NULL,
     info TEXT
);

CREATE TABLE Station(
     id BIGSERIAL PRIMARY KEY,
     name TEXT NOT NULL,
     latitude FLOAT NOT NULL,
     longitude FLOAT NOT NULL
);

CREATE TABLE Trip(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    bike_id BIGINT NOT NULL,
    start_station_id BIGINT NOT NULL,
    end_station_id BIGINT,
    start_time TIMESTAMP DEFAULT now(),
    end_time TIMESTAMP
);

ALTER TABLE Bike ADD CONSTRAINT bike_station_fk FOREIGN KEY (station_id) references Station(id);

ALTER TABLE Trip ADD CONSTRAINT trip_user_fk FOREIGN KEY (user_id) references AppUser(id);
ALTER TABLE Trip ADD CONSTRAINT trip_bike_fk FOREIGN KEY (bike_id) references Bike(id);
ALTER TABLE Trip ADD CONSTRAINT trip_start_station_fk FOREIGN KEY (start_station_id) references Station(id);
ALTER TABLE Trip ADD CONSTRAINT trip_end_station_fk FOREIGN KEY (end_station_id) references Station(id);


CREATE INDEX user_id_idx ON AppUser(id);
CREATE INDEX bike_id_idx ON Bike(id);
CREATE INDEX station_id_idx ON Station(id);
CREATE INDEX trip_id_idx ON Trip(id);


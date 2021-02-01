DROP DATABASE IF EXISTS da_racing;
CREATE DATABASE da_racing;
USE da_racing;

DROP TABLE IF EXISTS race_outcome;
DROP TABLE IF EXISTS race;
DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS track;

CREATE TABLE car (
  id INTEGER NOT NULL AUTO_INCREMENT,
  acceleration INTEGER NOT NULL,
  braking INTEGER NOT NULL,
  cornering_ability INTEGER NOT NULL,
  description VARCHAR(255),
  name VARCHAR(64) NOT NULL,
  top_speed INTEGER NOT NULL,
  PRIMARY KEY (id));
  
ALTER TABLE car ADD constraint name_uq UNIQUE (name);

CREATE TABLE track(
    id INTEGER NOT NULL AUTO_INCREMENT,
    sequence VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (id));

CREATE TABLE race (
	id INTEGER NOT NULL AUTO_INCREMENT,
    track_id INTEGER REFERENCES track(id),
    race_time DATETIME NOT NULL,
    primary key (id));
    
CREATE TABLE race_outcome (
	id INTEGER NOT NULL AUTO_INCREMENT,
    race_id INTEGER REFERENCES race(id),
    car_id INTEGER REFERENCES car(id),
    score INTEGER NOT NULL,
    primary key (id));
    
ALTER TABLE race_outcome ADD constraint race_car UNIQUE (race_id, car_id);
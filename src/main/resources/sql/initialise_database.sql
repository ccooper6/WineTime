DROP TABLE IF EXISTS wines;
--SPLIT
CREATE TABLE IF NOT EXISTS wines (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     item TEXT,
                                     buyer_first_name TEXT,
                                     buyer_last_name TEXT,
                                     price REAL,
                                     lat REAL,
                                     lng REAL,
                                     UNIQUE (lat, lng));
--SPLIT
DROP TABLE IF EXISTS users;
--SPLIT
CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     username TEXT UNIQUE,
                                     password TEXT);
--SPLIT
INSERT INTO users (username, password) VALUES ('admin', 'admin');
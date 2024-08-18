DROP TABLE IF EXISTS wine;
/* SPLIT */
CREATE TABLE IF NOT EXISTS wine (
                                     id INTEGER PRIMARY KEY NOT NULL,
                                     name TEXT NOT NULL,
                                     vintage INTEGER,
                                     price REAL,
                                     description TEXT,
                                     UNIQUE (name));
/* SPLIT */
DROP TABLE IF EXISTS user;
/* SPLIT */
CREATE TABLE IF NOT EXISTS user (
                                     id INTEGER PRIMARY KEY NOT NULL,
                                     username TEXT UNIQUE NOT NULL,
                                     password INT NOT NULL);
/* SPLIT */
DROP TABLE IF EXISTS tag;
/* SPLIT */
CREATE TABLE IF NOT EXISTS tag (
                                     name TEXT PRIMARY KEY UNIQUE NOT NULL,
                                     type TEXT NOT NULL CHECK ( type in ('Variety','Location','Winery','Country') ));
/* SPLIT */
DROP TABLE IF EXISTS competition;
/* SPLIT */
CREATE TABLE IF NOT EXISTS competition (
                                     id INTEGER PRIMARY KEY NOT NULL,
                                     name TEXT NOT NULL,
                                     year INTEGER,
                                     UNIQUE (name, year));
/* SPLIT */
DROP TABLE IF EXISTS likes;
/* SPLIT */
CREATE TABLE IF NOT EXISTS likes (
                                     uid INTEGER NOT NULL,
                                     tname INTEGER NOT NULL,
                                     value INTEGER NOT NULL,
                                     FOREIGN KEY (uid) REFERENCES user(id),
                                     FOREIGN KEY (tname) REFERENCES tag(name));
/* SPLIT */
DROP TABLE IF EXISTS logs;
/* SPLIT */
CREATE TABLE IF NOT EXISTS logs (
                                     uid INTEGER NOT NULL,
                                     wid INTEGER NOT NULL,
                                     date DATE NOT NULL,
                                     FOREIGN KEY (uid) REFERENCES user(id),
                                     FOREIGN KEY (wid) REFERENCES wine(id));
/* SPLIT */
DROP TABLE IF EXISTS reviews;
/* SPLIT */
CREATE TABLE IF NOT EXISTS reviews (
                                     uid INTEGER NOT NULL,
                                     wid INTEGER NOT NULL,
                                     rating REAL,
                                     description TEXT,
                                     date DATE,
                                     FOREIGN KEY (uid) REFERENCES user(id),
                                     FOREIGN KEY (wid) REFERENCES wine(id));
/* SPLIT */
DROP TABLE IF EXISTS owned_by;
/* SPLIT */
CREATE TABLE IF NOT EXISTS owned_by (
                                     wid INTEGER NOT NULL,
                                     tname TEXT NOT NULL,
                                     FOREIGN KEY (wid) REFERENCES wine(id),
                                     FOREIGN KEY (tname) REFERENCES tag(name));
/* SPLIT */
DROP TABLE IF EXISTS participated_in;
/* SPLIT */
CREATE TABLE IF NOT EXISTS participated_in (
                                     wid INTEGER NOT NULL,
                                     cid INTEGER NOT NULL,
                                     award TEXT,
                                     FOREIGN KEY (wid) REFERENCES wine(id),
                                     FOREIGN KEY (cid) REFERENCES competition(id));
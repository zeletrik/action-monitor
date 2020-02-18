DROP TABLE IF EXISTS am_user;
CREATE TABLE am_user (
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(45) NOT NULL UNIQUE,
  password VARCHAR(45) NOT NULL
);

DROP TABLE IF EXISTS role;
CREATE TABLE role (
  role_id SERIAL PRIMARY KEY,
  role VARCHAR(45) NOT NULL UNIQUE
);

/** JOIN TABLES */
DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role(
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  role_id INT NOT NULL
);

DROP TABLE IF EXISTS message;
CREATE TABLE message(
  id SERIAL PRIMARY KEY,
  from_user_id INT NOT NULL,
  to_user_id INT NOT NULL,
  msg TEXT,
  timestamp DATE NOT NULL DEFAULT CURRENT_DATE
);


INSERT INTO am_user (user_id, username, password)
VALUES (1, 'user', 'password');
INSERT INTO am_user (user_id, username, password)
VALUES (2, 'admin','password');
INSERT INTO am_user (user_id, username, password)
VALUES (3, 'mary','password');

INSERT INTO role (role_id, role)
VALUES (1, 'USER');
INSERT INTO role (role_id, role)
VALUES (2, 'ADMIN');

INSERT INTO user_role (id, user_id, role_id)
VALUES (1, 1, 1);
INSERT INTO user_role (id, user_id, role_id)
VALUES (2, 2, 2);
INSERT INTO user_role (id, user_id, role_id)
VALUES (3, 3, 1);
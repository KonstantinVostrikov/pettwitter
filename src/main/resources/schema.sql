DROP TABLE users CASCADE;
CREATE TABLE IF NOT EXISTS users
(
    username varchar(50)  NOT NULL PRIMARY KEY,
    password varchar(500) NOT NULL,
    enabled  boolean      NOT NULL
);

DROP TABLE authorities;
CREATE TABLE IF NOT EXISTS authorities
(
    username  varchar(50) NOT NULL,
    authority varchar(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username ON authorities (username, authority);


-- USER ACCOUNTS
DROP TABLE IF EXISTS user_accounts CASCADE;
CREATE TABLE IF NOT EXISTS user_accounts
(
    id       VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL REFERENCES users (username) ON DELETE CASCADE,
    name     VARCHAR(40) NOT NULL,
    photo    VARCHAR(255)
);


-- POSTS
DROP TABLE IF EXISTS posts CASCADE;
CREATE TABLE IF NOT EXISTS posts
(
    id         VARCHAR(36) PRIMARY KEY,
    content    VARCHAR(1024),
    author     VARCHAR(36) REFERENCES user_accounts (id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);


-- LIKES
DROP TABLE IF EXISTS posts_likes_counter;
CREATE TABLE posts_likes_counter
(
    post_id VARCHAR(36) REFERENCES posts (id) ON UPDATE CASCADE,
    user_id VARCHAR(36) REFERENCES user_accounts (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT likes_product_pkey PRIMARY KEY (post_id, user_id) -- explicit pk
);


-- IMAGES
DROP TABLE IF EXISTS image;
CREATE TABLE IF NOT EXISTS image
(
    id        VARCHAR(36) PRIMARY KEY,
    name      VARCHAR(120),
    type      VARCHAR(120),
    file_size bigint NOT NULL,
    file_data bytea NOT NULL
);
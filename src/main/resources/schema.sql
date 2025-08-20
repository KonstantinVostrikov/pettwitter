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

INSERT INTO users
VALUES ('alice', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('bob', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('charlie', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('diana', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('ethan', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('fiona', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('george', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('hannah', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('ian', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE),
       ('julia', '{bcrypt}$2a$12$XGKukcLHrKRKzUguTLuXpOlyq.IjyrVzmI7Qw8TbYO6XRMAqsLuFq', TRUE);

INSERT INTO authorities
VALUES ('alice', 'read'),
       ('bob', 'read'),
       ('charlie', 'read'),
       ('diana', 'read'),
       ('ethan', 'read'),
       ('fiona', 'read'),
       ('george', 'read'),
       ('hannah', 'read'),
       ('ian', 'read'),
       ('julia', 'read');

-- USER ACCOUNTS
DROP TABLE IF EXISTS user_accounts CASCADE;
CREATE TABLE IF NOT EXISTS user_accounts
(
    id       VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL REFERENCES users (username) ON DELETE CASCADE,
    name     VARCHAR(40) NOT NULL,
    photo    VARCHAR(255)
);
INSERT INTO user_accounts
VALUES ('8e238d63-ac33-4763-9cbf-86370fb7a8e2', 'alice', 'aliceName'),
       ('2b32c85b-adee-4bbd-b9f8-3f09c4348087', 'bob', 'bob'),
       ('1f9b3a67-4cce-411a-8e8d-9f6c1f7e3f17', 'charlie', 'charlie'),
       ('3c4377df-d5e5-456f-9444-32e5d1524ac9', 'diana', 'diana'),
       ('74c5d6e2-dcbe-4b98-b4f4-122b9d118f90', 'ethan', 'ethan'),
       ('de6b6a3f-d103-4658-bf83-84f2cbdcbb38', 'fiona', 'fiona'),
       ('95c7187d-1e6e-4c30-b991-7355b8c44b66', 'george', 'george'),
       ('f35768ae-7a5d-4d17-96b8-68158aa7e6cd', 'hannah', 'hannah'),
       ('32aeb823-d943-4d9b-a8cc-146d60c826be', 'ian', 'ian'),
       ('6ff7ea60-0a13-4f6c-93c2-6e6b70ff26bc', 'julia', 'julia');


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
INSERT INTO posts
VALUES ('fc326668-c01f-4fd5-b7e0-b0be21a89b86',
        'first post',
        '8e238d63-ac33-4763-9cbf-86370fb7a8e2',
        '2025-05-28 02:02:24.806827',
        '2025-05-28 02:02:24.806827'),
       ('e7f860e2-6e4b-439d-8e0c-08b3a6df7f92', 'second post', '8e238d63-ac33-4763-9cbf-86370fb7a8e2',
        '2025-05-28 02:04:24.806827',
        '2025-05-28 02:04:24.806827'),
       ('49554627-6d97-46b5-a4c5-4c134c9d7d31', 'third post', '8e238d63-ac33-4763-9cbf-86370fb7a8e2',
        '2025-05-28 02:06:24.806827',
        '2025-05-28 02:06:24.806827'),
       ('f3da1c9b-35d8-47f6-881e-aa368f338484', 'fourth post', '8e238d63-ac33-4763-9cbf-86370fb7a8e2', NOW(), NOW());


-- More posts for pagination
INSERT INTO posts
SELECT gen_random_uuid(),
       concat('Event ', gs::text),
       '8e238d63-ac33-4763-9cbf-86370fb7a8e2',
       NOW() - (gs || ' days')::interval,
       NOW() - (gs || ' days')::interval
FROM generate_series(0, 13) AS gs;

-- LIKES
DROP TABLE IF EXISTS posts_likes_counter;
CREATE TABLE posts_likes_counter
(
    post_id VARCHAR(36) REFERENCES posts (id) ON UPDATE CASCADE,
    user_id VARCHAR(36) REFERENCES user_accounts (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT likes_product_pkey PRIMARY KEY (post_id, user_id) -- explicit pk
);
INSERT INTO posts_likes_counter
VALUES ('fc326668-c01f-4fd5-b7e0-b0be21a89b86', '8e238d63-ac33-4763-9cbf-86370fb7a8e2'),
       ('fc326668-c01f-4fd5-b7e0-b0be21a89b86', '2b32c85b-adee-4bbd-b9f8-3f09c4348087'),
       ('fc326668-c01f-4fd5-b7e0-b0be21a89b86', '1f9b3a67-4cce-411a-8e8d-9f6c1f7e3f17'),
       ('fc326668-c01f-4fd5-b7e0-b0be21a89b86', '3c4377df-d5e5-456f-9444-32e5d1524ac9'),
       ('fc326668-c01f-4fd5-b7e0-b0be21a89b86', '74c5d6e2-dcbe-4b98-b4f4-122b9d118f90'),
       ('fc326668-c01f-4fd5-b7e0-b0be21a89b86', 'de6b6a3f-d103-4658-bf83-84f2cbdcbb38'),
       ('e7f860e2-6e4b-439d-8e0c-08b3a6df7f92', '95c7187d-1e6e-4c30-b991-7355b8c44b66'),
       ('e7f860e2-6e4b-439d-8e0c-08b3a6df7f92', 'f35768ae-7a5d-4d17-96b8-68158aa7e6cd'),
       ('e7f860e2-6e4b-439d-8e0c-08b3a6df7f92', '32aeb823-d943-4d9b-a8cc-146d60c826be'),
       ('49554627-6d97-46b5-a4c5-4c134c9d7d31', '6ff7ea60-0a13-4f6c-93c2-6e6b70ff26bc');
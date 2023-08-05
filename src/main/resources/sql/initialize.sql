# Data tables

CREATE TABLE IF NOT EXISTS player
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pw_pbkdf2 BINARY(62),
    qq BIGINT,
    nickname TINYTEXT,
    description TEXT,
    registered_on INT
);

CREATE TABLE IF NOT EXISTS java_profile
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('JAVA_MICROSOFT', 'JAVA_LITTLE_SKIN'),
    current_player_name TINYTEXT,
    uuid BINARY(36)
);

CREATE TABLE IF NOT EXISTS bedrock_profile
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    current_player_name TINYTEXT,
    xuid BIGINT
);

CREATE TABLE IF NOT EXISTS server
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token BINARY(128),
    group_id BIGINT,
    name TINYTEXT,
    description TEXT,
    java_server_meta TEXT DEFAULT NULL,
    bedrock_server_meta TEXT DEFAULT NULL,
    applying_type ENUM('NOT_NEEDED', 'BY_PLAYER_META_ONLY', 'BY_MESSAGE', 'BY_FORM')
);

# Bonds

CREATE TABLE IF NOT EXISTS player_java_profile
(
    player_id BIGINT,
    java_profile_id BIGINT
);

CREATE TABLE IF NOT EXISTS player_bedrock_profile
(
    player_id BIGINT,
    bedrock_profile_id BIGINT
);

CREATE TABLE IF NOT EXISTS bedrock_verification
(
    created_at BIGINT,
    player_id BIGINT,
    verification_code BINARY(6)
);

CREATE TABLE IF NOT EXISTS player_server
(
    player_id BIGINT,
    server_id BIGINT,
    relationship ENUM('OWNER', 'MANAGER', 'PLAYER'),
    specified_nickname TINYTEXT
);

CREATE TABLE IF NOT EXISTS server_tag
(
    server_id BIGINT,
    tag_id SMALLINT
);

CREATE TABLE IF NOT EXISTS applying_session
(
    created_at BIGINT,
    source BIGINT,
    target BIGINT,
    json_load TEXT,
    result TEXT DEFAULT NULL
);
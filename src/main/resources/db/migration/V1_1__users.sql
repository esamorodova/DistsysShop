CREATE TABLE users (
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    refreshToken VARCHAR,
    refreshTokenElapsedAt TIMESTAMP,
    accessToken VARCHAR,
    accessTokenElapsedAt TIMESTAMP,
    PRIMARY KEY (email)
);
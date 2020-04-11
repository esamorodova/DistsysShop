CREATE TABLE users (
    email SERIAL VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    refreshToken VARCHAR,
    refreshTokenElapsedAt TIMESTAMP,
    accessToken VARCHAR,
    accessTokenElapsedAt TIMESTAMP
);
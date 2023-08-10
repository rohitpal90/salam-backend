ALTER TABLE jwt_refresh RENAME TO jwt_refresh_tokens;
ALTER table jwt_refresh_tokens RENAME COLUMN created to created_at;
ALTER TABLE request RENAME TO requests;
ALTER TABLE plan RENAME TO plans;
ALTER TABLE role RENAME TO roles;
ALTER TABLE transition RENAME TO transitions;
ALTER TABLE user RENAME TO users;
ALTER TABLE user_role RENAME TO role_user;

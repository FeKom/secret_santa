INSERT INTO roles (role_id, role_name) VALUES (1, 'OWNER')
ON CONFLICT (role_id) DO NOTHING;

INSERT INTO roles (role_id, role_name) VALUES (2, 'BASIC')
ON CONFLICT (role_id) DO NOTHING;
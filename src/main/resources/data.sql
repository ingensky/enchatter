INSERT INTO usr (ID, username, PASSWORD) VALUES
   (1000, 'user', '1'),
   (1001, 'admin', '2');

INSERT INTO user_role (roles, user_id) VALUES
  ('USER', 1000),
  ('ADMIN', 1001),
  ('USER', 1001);


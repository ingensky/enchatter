DELETE FROM message;
DELETE FROM dialog;
DELETE FROM user_role;
DELETE FROM usr;

INSERT INTO usr (ID, username, PASSWORD) VALUES
   (1000, 'user', '1'),
   (1001, 'admin', '2'),
   (1002, 'u', 'u'),
   (1003, 'q', 'q'),
   (1004, 't', 't'),
   (1005, 'r', 'r');

INSERT INTO user_role (roles, user_id) VALUES
  ('USER', 1000),
  ('ADMIN', 1001),
  ('USER', 1001),
  ('USER', 1002),
  ('USER', 1003),
  ('USER', 1004),
  ('USER', 1000);

INSERT INTO dialog (id, interlocutor_one_id, interlocutor_two_id) VALUES
(2000, 1000, 1001),
(2001, 1002, 1003),
(2002, 1000, 1004);

INSERT INTO message (id, user_id, dialog_id, text, creation_time) VALUES
(3000, 1000, 2000, 'hello', '2018-10-23 20:00:00'),
(3001, 1001, 2000, 'hello you', '2018-10-23 20:01:00'),
(3002, 1000, 2000, 'tell me a joke', '2018-10-23 20:02:00'),
(3003, 1000, 2000, 'can you', '2018-10-23 20:03:00'),
(3004, 1001, 2000, 'of course', '2018-10-23 20:04:00'),
(3005, 1001, 2000, 'tuk tuk', '2018-10-23 20:05:00'),
(3006, 1000, 2000, 'who is where?', '2018-10-23 20:06:00'),
(3007, 1003, 2001, 'hello new', '2018-10-23 20:07:00'),
(3008, 1000, 2002, 'hello 1004', '2018-10-23 20:08:00'),
(3009, 1004, 2002, 'hello user', '2018-10-23 20:09:00');


INSERT INTO users (id, email, password) VALUES
-- User eugen@email.com/pass
(1, 'eugen@email.com', '$2a$04$kqRvgmJBlWZQQ2c9NT9IH.ZhxFY07Y2xE73vmLHxBq2hNTvGvUc5m'),
-- User eric@email.com/123
(2, 'eric@email.com', '$2a$10$BuPWnSrcLPxwMcCwu7eyBu/x7817Mo3MzYbZGVFfgIewEUUXPmpy6'),
-- User test@user.com/pass
(3, 'test@user.com', '$2a$04$kqRvgmJBlWZQQ2c9NT9IH.ZhxFY07Y2xE73vmLHxBq2hNTvGvUc5m');

INSERT INTO possession (id, name, owner_id) VALUES
(1, 'Eugen Possession', 1),
(2, 'Common Possesion', 1),
(3, 'Eric Possession', 2),
(4, 'Test Possession', 3);

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 1, 'eugen@email.com'),
(2, 1, 'eric@email.com'),
(3, 1, 'test@user.com');

INSERT INTO acl_class (id, class) VALUES 
(1, 'com.baeldung.lss.model.Possession');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, 1), -- Eugen Possession object identity
(2, 1, 2, NULL, 1, 1), -- Common Possession object identity
(3, 1, 3, NULL, 2, 1), -- Eric Possession object identity
(4, 1, 4, NULL, 3, 1); -- Test Possession object identity

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 0, 1, 16, 1, 0, 0), -- eugen@email.com has Admin permission for Eugen Possession 
(2, 2, 0, 1, 16, 1, 0, 0), -- eugen@email.com has Admin permission for Common Possession
(3, 2, 1, 2, 1, 1, 0, 0),  -- eric@email.com has Read permission for Common Possession 
(4, 3, 0, 2, 16, 1, 0, 0), -- eric@email.com has Admin permission for Eric Possession
(5, 4, 0, 3, 16, 1, 0, 0); -- test@user.com has Admin permission for Eric Possession
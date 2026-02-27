-- Demo permissions
INSERT INTO user_permission(username, resource, action) VALUES ('alice','PAYMENT','CREATE');
INSERT INTO user_permission(username, resource, action) VALUES ('alice','AUDIT','VIEW');
INSERT INTO user_permission(username, resource, action) VALUES ('bob','AUDIT','VIEW');

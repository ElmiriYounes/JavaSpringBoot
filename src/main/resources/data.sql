INSERT INTO users (email, password, lastname, firstname, role)
SELECT 'profA@gmail.com', '$2a$12$Xtp0tTx8nsXce2k5UU6GcuN6iG33uRHmcLCQIswChowU/hqLE6XIu', 'Durand', 'Dupont', 'TEACHER'
WHERE NOT EXISTS (SELECT * FROM users WHERE email = 'profA@gmail.com');

INSERT INTO users (email, password, lastname, firstname, role)
SELECT 'profB@gmail.com', '$2a$12$dSGErpzetKLd8qA5WPh5z.EK3l0WRNBJPrSnjUR5p9iCC3Ypgx9Iq', 'Billi', 'Olli', 'TEACHER'
WHERE NOT EXISTS (SELECT * FROM users WHERE email = 'profB@gmail.com');
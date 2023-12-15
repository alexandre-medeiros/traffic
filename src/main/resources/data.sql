delete from citation;
delete from vehicle;
delete from owner;

-- Insert sample data into the Owner table
insert into owner (id, name, email, phone)
values
    (1, 'John Doe', 'john.doe@example.com', '555-123-4567'),
    (2, 'Jane Smith Doe', 'jane.smith@example.com', '555-987-6543'),
    (3, 'Alice Johnson', 'alice.johnson@example.com', '555-555-5555'),
    (4, 'Bob Wilson', 'bob.wilson@example.com', '555-111-2222'),
    (5, 'Sarah Brown', 'sarah.brown@example.com', '555-333-4444'),
    (6, 'Michael Davis', 'michael.davis@example.com', '555-999-8888'),
    (7, 'Emma White', 'emma.white@example.com', '555-777-6666'),
    (8, 'William Lee', 'william.lee@example.com', '555-666-9999'),
    (9, 'Olivia Taylor', 'olivia.taylor@example.com', '555-222-3333'),
    (10, 'James Johnson', 'james.johnson@example.com', '555-444-7777');

-- Insert sample data into the Vehicle table
insert into vehicle (id, owner_id, make, model, plate, status, created_date, arrested_date)
values
    (1, 1, 'Toyota', 'Camry', 'ABC1234', 'REGULAR', '2023-11-17 08:00:00', null),
    (2, 2, 'Honda', 'Civic', 'XYZ5678', 'REGULAR', '2023-11-16 10:30:00', null),
    (3, 3, 'Ford', 'Focus', 'DEF4321', 'ARRESTED', '2023-11-15 14:45:00', '2023-11-16 09:15:00'),
    (4, 4, 'Chevrolet', 'Malibu', 'JKL9876', 'REGULAR', '2023-11-14 12:20:00', null),
    (5, 5, 'Nissan', 'Altima', 'MNO8765', 'ARRESTED', '2023-11-13 16:30:00', '2023-11-15 11:00:00'),
    (6, 6, 'Toyota', 'Rav4', 'PQR5432', 'REGULAR', '2023-11-12 09:45:00', null),
    (7, 7, 'Honda', 'Accord', 'GHI7890', 'ARRESTED', '2023-11-11 11:15:00', '2023-11-14 08:45:00'),
    (8, 8, 'Ford', 'Fusion', 'STU0123', 'REGULAR', '2023-11-10 15:30:00', null),
    (9, 9, 'Chevrolet', 'Cruze', 'VWX4567', 'REGULAR', '2023-11-09 13:00:00', null);

-- Insert 20 citations for some vehicles
insert into citation (id, vehicle_id, description, amount, date_violation)
values
    (1, 1, 'Speeding', 50.00, '2023-11-18 09:30:00'),
    (2, 1, 'Illegal Parking', 30.00, '2023-11-19 11:45:00'),
    (3, 2, 'Running Red Light', 75.00, '2023-11-18 14:15:00'),
    (4, 2, 'Expired Registration', 40.00, '2023-11-19 08:30:00'),
    (5, 3, 'Speeding', 60.00, '2023-11-18 12:00:00'),
    (6, 3, 'No Seat Belt', 20.00, '2023-11-19 10:00:00'),
    (7, 4, 'Illegal U-turn', 45.00, '2023-11-18 15:45:00'),
    (8, 4, 'Failure to Signal', 35.00, '2023-11-19 09:15:00'),
    (9, 5, 'Reckless Driving', 100.00, '2023-11-18 11:30:00'),
    (10, 5, 'DUI', 150.00, '2023-11-19 07:45:00'),
    (11, 6, 'Speeding', 50.00, '2023-11-18 10:45:00'),
    (12, 6, 'Illegal Parking', 30.00, '2023-11-19 12:00:00'),
    (13, 7, 'Running Red Light', 75.00, '2023-11-18 13:15:00'),
    (14, 7, 'Expired Registration', 40.00, '2023-11-19 09:30:00'),
    (15, 7, 'Running Stop Sign', 60.00, '2023-11-18 16:00:00'),
    (16, 7, 'Speeding in School Zone', 40.00, '2023-11-19 08:00:00'),
    (17, 8, 'No Parking Zone', 30.00, '2023-11-18 14:45:00'),
    (18, 8, 'Failure to Yield', 35.00, '2023-11-19 09:30:00'),
    (19, 9, 'Speeding', 50.00, '2023-11-18 10:15:00'),
    (20, 9, 'Expired License', 45.00, '2023-11-19 11:00:00');

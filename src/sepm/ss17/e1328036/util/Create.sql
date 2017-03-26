CREATE SEQUENCE seq_boxes;
CREATE SEQUENCE seq_reservations;
CREATE SEQUENCE seq_invoices;

CREATE TABLE Boxes (
	bid INTEGER DEFAULT seq_boxes.nextval PRIMARY KEY,
	size NUMERIC(7,2) NOT NULL CHECK (size > 0),
	sawdust_amount INTEGER CHECK (sawdust_amount >= 0),
	straw_amaount INTEGER CHECK (straw_amaount >= 0),
	has_window BOOLEAN NOT NULL DEFAULT false,
	price NUMERIC(7,2) NOT NULL CHECK (price > 0),
	image VARCHAR DEFAULT NULL,
	is_deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE Reservations(
	rid INTEGER DEFAULT seq_reservations.nextval PRIMARY KEY,
	is_deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE BoxReservations (
	brid INTEGER DEFAULT seq_invoices.nextval PRIMARY KEY,
	bid INTEGER NOT NULL,
	rid INTEGER NOT NULL,
	client_name VARCHAR(40) NOT NULL,
	horse_name VARCHAR(40) NOT NULL,
	date_from DATE,
	date_to DATE,
	FOREIGN KEY(bid) REFERENCES Boxes(bid),
	FOREIGN KEY(rid) REFERENCES Reservations(rid),
	CHECK (date_from < date_to)
);

INSERT INTO Boxes VALUES
	(DEFAULT, 11.9, 15, 28, false, 20, 'E:\G3no\TU Wien\Software Engineering und Projektmanagement\Einzelbeispiel\Wendy\src\sepm\ss17\e1328036\images\HorseBox1.JPG', DEFAULT),
	(DEFAULT, 10.9, 18, 28, false, 15, 'E:\G3no\TU Wien\Software Engineering und Projektmanagement\Einzelbeispiel\Wendy\src\sepm\ss17\e1328036\images\HorseBox2.JPG', DEFAULT),
	(DEFAULT, 12.9, 20, 31, true, 25, 'E:\G3no\TU Wien\Software Engineering und Projektmanagement\Einzelbeispiel\Wendy\src\sepm\ss17\e1328036\images\HorseBox3.JPG', DEFAULT),
	(DEFAULT, 13.0, 21, 33, true, 28.5, 'E:\G3no\TU Wien\Software Engineering und Projektmanagement\Einzelbeispiel\Wendy\src\sepm\ss17\e1328036\images\HorseBox4.JPG', DEFAULT),
	(DEFAULT, 11.1, 17, 24, false, 17.5, 'E:\G3no\TU Wien\Software Engineering und Projektmanagement\Einzelbeispiel\Wendy\src\sepm\ss17\e1328036\images\HorseBox5.JPG', DEFAULT),
	(DEFAULT, 11.6, 19, 32, false, 22.5, DEFAULT, DEFAULT);

INSERT INTO Reservations VALUES
	(DEFAULT, DEFAULT),
	(DEFAULT, DEFAULT),
	(DEFAULT, DEFAULT);

INSERT INTO BoxReservations VALUES
	(DEFAULT, 1, 1, 'Leonardo Bigollo', 'Fusaichi Pegasus', '2014-03-14', '2014-03-18'),
	(DEFAULT, 2, 2, 'John Atanasoff', 'Man o War', '2014-03-30', '2014-04-05'),
	(DEFAULT, 3, 2, 'John Nash', 'Empire Maker', '2014-03-30', '2014-04-06'),
	(DEFAULT, 4, 3, 'John Atanasoff', 'War Admiral', '2014-03-28', '2014-04-04'),
	(DEFAULT, 5, 3, 'Ada Lovelace', 'Bucephalus', '2014-04-09', '2014-04-21');
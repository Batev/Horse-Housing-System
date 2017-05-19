## Horse-Housing-System (Wendy's Guesthouse)

**Program for renting horse houses.**

The program has a [Three-tier architecture](https://en.wikipedia.org/wiki/Multitier_architecture) consisting of a:
* Persistence Layer: Uses [H2 database](http://www.h2database.com/html/main.html). The communication between the program and the database happens with prepared statements.
* Service Layer: Varifies data input and makes complicated computations.
* Presentation Layer: Uses [JavaFX](https://docs.oracle.com/javase/8/javase-clienttechnologies.htm). Simple interface for the users.

_A simple sql create script could be found [here](https://github.com/Batev/Horse-Housing-System/blob/master/src/sepm/ss17/e1328036/util/Create.sql) for creating and filling the database._

_The assignment is part of the [Software Engineering und Projektmanagement](https://tiss.tuwien.ac.at/course/educationDetails.xhtml?dswid=2286&dsrid=450&semester=2017S&courseNr=188909) course at the Vienna University of Technology.
The full assignment could be found [here](https://1drv.ms/b/s!Anf4OKi6Pl-_gYYDdtz4Ji4wKvoXDw) in German._

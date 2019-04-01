###SETUP
If you just want to run a working build of the code:

`java -jar rdbms.jar`

If you want to build the source code on your own machine, you'll need to:






Database commands:

CREATE:

example #1:

    create table <table name> (<column0 name> <type0>, <column1 name> <type1>, etc.)

example #2:

    create table <table name> as <select clause>

NOTE: method 2 requires using the select command, described below



LOAD:

example:

    load <table name>


STORE:

example:

    store <table name>


DROP TABLE:

example:

    drop table <table name>


INSERT INTO:

example:

    insert into <table name> values <literal0>,<literal1>,etc.

NOTE: insert values must match the given table


PRINT:

example:

    print <table name>


SELECT:

example #1:

    select <column expr0>,<column expr1>,... from <table0>,<table1>,... where <cond0> and <cond1> and ...

example #2:

    select <column expr> from <table0>

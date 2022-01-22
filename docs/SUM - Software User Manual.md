# SUM - Software User Manual

This document illustrates all steps to install and use the software web app.
For the REST API documentation, the [RAD](./RAD%20-%20REST%20API%20Documentation.md)
should be consulted instead.

> Currently, only the installation part is shown.
> When the webapp frontend is complete, this document will be updated

## Package

The software is provided as a compressed folder, containing these files:
* `gradi-XX.YY.ZZ.jar`: java executable, where `XX.YY.ZZ` is the version.
* `application.properties`: configuration file
* `db.sql`: database configuration script

## Installation

To correctly install the software on local machine, these steps must be followed __IN ORDER__:
* files organization
* database configuration
* database initialization
* configuration
* first boot

### Files organization

All these files must be placed in the same folder, that, from this point, will be referred as
the `app/` folder.

Moreover, a subfolder for uploads files is required. Make sure that the user, currently logged onto
the local system has read and write permission on that folder.
This folder will be referred to as `uploads`.

The full structure must be the following:
```
├── app/
│   ├── uploads/
│   ├── db.sql
│   ├── gradi-XX.YY.ZZ.jar
│   ├── application.properties
```

### Database configuration

This software relays on [PostgresSQL RDBMS](https://www.postgresql.org).

After installing a local copy, the administrator must choose a combination
of username and password. This combination must be reported in `application.properties` file.
The `db.sql` script contains references to the username too, so must be edited as well.
The default combination is `postgres/root`.

Then an empty database must be created. The default name for it is `gradi`. Must be 
reported to the `application.properties` too.

### Database initialization

Once database server is installed and on, the `db.sql` script must be executed.
Open a terminal, move to the `app/` directory and execute this command:

```shell
psql -U {username} -d {database} -f db.sql
```

where `{username}` and `{database}` must be replaced with the PostgresSQL
chosen username and database's name.

The prompt will ask for the chosen PostgresSQL password: put it in and press <kbd>Enter</kbd>.
Note that you won't be able to see the password while you're typing.

When the command completes, the database will be initialized.

### Configuration

Now, it's time to set up variables in `application.properties` file.
The following variables __MUST__ be set up __BEFORE__ system's first boot.

| Variable                           | Description                   | Default            |
|------------------------------------|-------------------------------|--------------------|
| `spring.datasource.username`       | PostgresSQL username          | `postgres`         |
| `spring.datasource.password`       | PostgresSQL password          | `root`             |
| `spring.datasource.url`            | PostgresSQL connection string | `.../gradi`        |
| `gradi.admin.email`                | Administrator's email address | `admin@domain.io`  |
| `gradi.admin.password`             | Administrator's password      | `adminpassword`    |
| `gradi.system.domain`              | Application's domain          | `gradi.domain.io`  |
| `gradi.system.directories.upload`  | Upload directory              | `/path/to/uploads` |
| `gradi.system.email.smtp.name`     | Email sender's name           | `The GRADI System` |
| `gradi.system.email.smtp.host`     | SMTP server's hostname        | `smtp.domain.io`   |
| `gradi.system.email.smtp.port`     | SMTP server's port            | `000`              |
| `gradi.ststem.email.smtp.username` | SMTP server's username        | `gradi@domain.io`  |
| `gradi.system.email.smtp.password` | SMTP server's password        | `password`         |

For the `spring.datasource.url`, only the part after the last slash (`/`)
must be changed, and it's the chosen database's name.

The uploads' directory __MUST NOT__ terminate with a slash (`/`). The system
will add it automatically to build paths.

The `gradi.system.domain` is the subdomain at which the local GRADI installation is reachable.
Ideally, if your agency (can be a company, school, or other kind of institute)
has its main domain on `myagency.com`, the GRADI installation could be installed
on a `gradi.myagency.com` subdomain. 
Currently, this variable only appears on email, when a new user is registered.

For the SMTP-related settings, the mail provider shall be consulted.

### First boot

Now that everything is set up, it's time to start the software.
So, open a terminal, move to the `app/` directory, and execute the following:

```shell
java -jar ./gradi-XX.YY.ZZ.jar
```

If all above steps has been followed correctly and in order, the application should be
started without errors. To check that, open a browser, and go to this address:

```
http://localhost:9000/api/public/self-test
```

If the message `GRADI has been installed correctly on this system`, appears,
everything is alright.

### Further steps

After installing, other minor settings can be changed, as application's port, request files size
or JWT tokens parameters. Remember that, after one (or more) settings in the `application.properties`
file changes, the system __MUST__ be restarted to keep working properly.

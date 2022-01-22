# GRADI

This is the GitHub repository for the GRADI software.

<blockquote>
    <strong>Development status</strong>
    <p>REST backend is complete and no visible bugs/error have been found</p>
    <p>
        Documentation writing is in progress. Once it's complete,
        I will move to frontend webapp and this message will disappear.
    </p>
</blockquote>

GRADI (italian short of _**GR**ande **A**rchivio **DI**gitale_, in english
_Great Digital Archive_) it's a software for management, editing and
consultation of digital resources of all kinds.

Registered users marked as editors, can create and manage resources,
assigning read or write permissions to other editors and make them 
available for consultation for non-editor users, and also for
everyone outside the platform too.

A resource in GRADI is composed one or more physical files, of different types too.
It's uniquely identified by its code and is provided with a name and an optional description.
Additional properties can be added specifying a resource type.

Resource types are dynamically added by administrator, and provided of a set
of properties each one with its value type (string, numeric, datetime, ...).
A unique name is automatically generated.

The software is provided with a backend server, listening on `localhost:9000` and consists of
a secured REST API, listening on `localhost:9000/api`, and a web interface, listening on
`localhost:9000/web/`.

## Documentation

Inside `/docs` folder, the following documents are provided:

* __RAD - REST API Documentation__: lists REST endpoints, tells how to make
  requests and how to understand responses. Mainly intended for frontend or mobile
  developers who want to make client applications.
* __SDD - Software Design Document__: illustrates the idea behind this software,
  how it has been realized, and what type of data has been used. Intended for
  everyone interested to my idea, or wants to improve it.
* __SUM - Software User Manual__: tells how to install, use the software and how to
  solve errors or conflicts. Not really intended for developers, but for people who
  want to install and use the software locally.

In the same folder, under `/docs/apidocs` subdirectory, the API JavaDocs' pages are hosted.

## Contribution

All form of contribution are well accepted. Please use the  *Issue* section to report
bugs or security failures, and the *Pull requests* section to propose improvements 
or new features. You can also contact me via email. In this last case, please make sure
that email's subject starts with `[GRADI]` in order for me to categorize them correctly.

## Licensing

The whole software is under [Apache 2.0 license](./LICENSE)

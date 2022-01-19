# GRADI

This is the Github repository for the GRADI software.

<blockquote>
    <strong>Developement status</strong>
    <p>RESTful backend is complete and no visible bugs/error have been found</p>
    <p>
        Documentation writing is in progress. Once it's complete,
        I will move to frontend webapp and this message will disappear.
    </p>
</blockquote>

GRADI stands for _**GR**ande **A**rchivio **DI**gitale_, that can be translated in english
as _Great Digital Archive_. It's a software for management, editing and
consultation of digital resources of all kinds.

Registered users marked as editors can create, upload and perform all kind of operations
on their own resources, such as assigning read and/or write permissions.

Non-editor users cannot create or edit resources, but they can view and consult them,
if creator make them public or assign them read permissions.

A resource in GRADI is composed by one or more physical files, of different types too.
It's uniquely identified by its code and is provided with a name and an optional description.
Other custom properties can be added by providing a resource type.

Resource types are dynamically added (only by administrator), and provided of a set
of properties each one with its value type, such as string, numeric, datetime, boolean or text.
A unique code is automatically generated. Providing the right resource types is important
since their properties are used as search filters and sorting parameters.

The software is provided with a server, listening on `localhost:9000` and consists of
a secured RESTful API and a web interface, in order to allow other developers to build 
frontend application without too much effort. The endpoints are `localhost:9000/web`
for the web interface and `localhost:9000/api/` for the RESTful API.

## Documentation

Inside `/docs` folder, the following documents are provided:
* __RAD - REST API Documentation__: lists RESTful endpoints, tells how to make
  requests and how to understand responses. Mainly intended for frontend or mobile
  developers who want to make client applications.
* __SDD - Software Design Document__: illustrates the idea behind this software,
  how it has been realized, and what type of data has been used. Intended for
  everyone interested to my idea, or wants to improve it.
* __SUM - Software User Manual__: tells how to install, use the software and how to
  solve errors or conflicts. Not really intended for developers, but for people who
  want to install and use the software locally.

In the same folder, under `/docs/api` subdirectory, the API JavaDocs' pages are hosted.

## Contribution

All form of contribution are well accepted. Please use the  *Issue* section to report
bugs or security failures, and the *Pull requests* section to propose improvements 
or new features. You can also contact me via email. In this last case, please make sure
that email's subject starts with `[GRADI]` in order for me to categorize them correctly.

## Licensing

The whole software is under [Apache 2.0 license](./LICENSE)

# GRADI

This is the Github repository for the GRADI software.

<blockquote>
    <strong>
        ATTENTION: At the time I'm writing, the software is still incomplete. When all the work is finished, this message will disappear
    </strong>
</blockquote>

GRADI stands for _**GR**ande **A**rchivio **DI**gitale_, that can be translated in english as _Great Digital Archive_. As the name suggests, it is a software for management, editing and consultation of digital resources of all kinds.

Registered users marked as editors can create, upload and perform all kind of operations on their own resources, such as assigning read and/or write permissions.

Non-editor users, as mentioned before, cannot create or edit resources but they can view and consult them, if creator mades them public.

A resource in GRADI is composed by one or more physical files, of different types too. It's uniquely identified by its code and is provided with a name and an optional description. Other custom properties can be added by providing a resource type.

Resource types can be dinamically added only by administrator, who provides also its set of properties and their value type, such as string, numeric, datetime, boolean or text. An unique code is automatically generated. Providing the right resource types is important since their properties are used as search filters and sorting parameters.

The sofware is provided with a server, listening on `localhost:9000` and consists of a secured RESTful API and a web interface, in order to allow other developers to build frontend application without too much effort. The endpoints are `localhost:9000/web` for the web interface and `localhost:9000/api/` for the RESTful API.

## Contribution

All form of contribution are well accepted. Please use the  *Issue* section to report bugs or security failures, and the *Pull requests* section to propose improvements or new features. You can also send me an email. In this last case, please make sure that email's subject starts with `[GRADI]` in order for me to categorize them correctly.

## Licensing

The whole software is under [Apache 2.0 license](./LICENSE)
# contacts

Obligatory contacts app for a talk on building microservices with clojure.

The app provides a minimal json api for working with contacts. Data is stored using event sourcing. Events are published as atom feed.

## Setup

    lein ring server-headless

## Usage

    curl -i localhost:3000/contacts -XPOST --data '{"firstname" : "hannes", "lastname" : "oderso", "email" : "hannes@example.com"}' -H'Content-Type: application/json'
    curl -i localhost:3000/contacts/<id>
    curl -i localhost:3000/contacts/<id> -XPUT --data '{"lastname" : "partialupdatewithput"}' -H'Content-Type: application/json'
    curl -i localhost:3000/feed

## Author information and license

Copyright 2015 innoQ Deutschland GmbH. Published under the Apache 2.0 license.

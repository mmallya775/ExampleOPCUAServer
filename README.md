# Example Milo OPC UA server

A small project for learning how an OPC UA server works, built with
[Eclipse Milo](https://github.com/eclipse-milo/milo).

## Requirements

- Java 25
- Maven

## Running

Open the project in IntelliJ IDEA and run `ExampleOPCUAServer.main`.

The server listens on `opc.tcp://127.0.0.1:12686/wiki` until you press Enter.

## Connecting with a client

I used Prosys OPC UA Browser with these settings:

- Endpoint URL: `opc.tcp://127.0.0.1:12686/wiki`
- Security mode and policy: None
- Authentication: Anonymous

On macOS, Prosys OPC UA Browser can be installed with Homebrew.
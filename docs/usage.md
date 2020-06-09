---
layout: default
---

* [Introduction](#introduction)
    - [Aside: Codename ADBaaS](#aside-codename-adbaas)
* [Requirements](#requirements)
* [Starting the Service](#starting-the-service)
* [Usage examples](#usage-examples)
    - [Emulators](#emulators)
        - [List available emulators](#list-available-emulators)
    - [Devices](#devices)
        - [List active devices](#list-active-devices)
    - [Screenshots](#screenshots)
        - [Take screenshots (as Base64 Image)](#take-screenshot-as-base64-image)

# Using ADBaaS

## Introduction
ADBaaS provides a simple HTTP API to ADB and other Android SDK tools. It builds on top of the platform tools executing commands designed to be using in a Command Line Interface (CLI), so a lot of its main job is to read and parse the output of these commands, and construct a nice and extensible API over them.

### Aside: Codename ADBaaS
The initial release of this API was a simple wrapper around some ADB services. It then came the idea of doing the same with other commands like **emulator**.

## Requirements

* **ANDROID_HOME** environment variable should be defined as defined in [Android documentation](https://developer.android.com/studio/command-line/variables)
* Java 11 or newer

## Starting the Service

As a Java application, starting the service is as easy as

```shell script
java -jar adbaas.jar
```

This will start the service with the following configuration:

```yaml
Listening Port: 8443
TLS/SSL Enabled: true
TLS7SSL Certificates: Included inside artifact
URL: https://127.0.0.1:8443/api
Application log file: ./logs/service.log
API Keys: fa7166bd-eb36-4299-b04a-f91c7e2e907c,959bf96d-7277-4dc8-92c0-747571c1d92b,c2d94953-01bc-439d-892f-e25c2777f41e #Test API Keys
```

There are a number of parameters that can be sent in order to configure this service, for example, changing the listening port.

```shell script
java -jar -Dserver.port=443 adbaas.jar
```

However, as this is a Spring Boot application, an **application.properties** file can (and should) be passed in order to override the built-in one.

```shell script
java -jar app.jar --spring.config.location=file:///path/to/my/own/application.properties
```

## Usage examples

### Emulators

Interacts with Android SDK's emulators

#### List available emulators

Request

```
GET /emulators/ HTTP/1.1
Host: https://localhost:8443/api
X-API-Key: fa7166bd-eb36-4299-b04a-f91c7e2e907c
```

cURL
```sh
curl --location --request GET 'https://localhost:8443/api/emulators/' \
--header 'X-API-Key: fa7166bd-eb36-4299-b04a-f91c7e2e907c'
```

Response

```json
[
    "Android_Wear_Square_API_28",
    "Nexus_5X_API_29_x86"
]
```
    
### Devices

Interacts with Android SDK's ADB (Android Debug Bridge)

#### List active devices

Request

```
GET /devices/ HTTP/1.1
Host: https://localhost:8443/api
X-API-Key: fa7166bd-eb36-4299-b04a-f91c7e2e907c
```

cURL

```sh
curl --location --request GET 'https://localhost:8443/api/devices/' \
--header 'X-API-Key: fa7166bd-eb36-4299-b04a-f91c7e2e907c'
```

Response`

```json
[
    {
        "serialNumber": "RF8M74G8Q9R",
        "status": "device",
        "usb": "338690048X",
        "product": "d2sxx",
        "model": "SM N975F",
        "device": "d2s",
        "transportId": "3"
    },
    {
        "serialNumber": "emulator-5554",
        "status": "device",
        "product": "sdk gphone x86",
        "model": "Android SDK built for x86",
        "device": "generic x86",
        "transportId": "1"
    },
    {
        "serialNumber": "emulator-5556",
        "status": "device",
        "product": "sdk gwear sw x86",
        "model": "sdk gwear sw x86",
        "device": "generic x86",
        "transportId": "2"
    }
]
```

### Screenshots

#### Take screenshot (as Base64 image)

Request

```
GET /screenshots/base64?deviceId=emulator-5554 HTTP/1.1
Host: https://localhost:8443/api
X-API-Key: fa7166bd-eb36-4299-b04a-f91c7e2e907c
```

cURL

```sh
curl --location --request GET 'https://localhost:8443/api/screenshots/base64?deviceId=emulator-5554' \
--header 'X-API-Key: fa7166bd-eb36-4299-b04a-f91c7e2e907c'
```

Response
```
PNG Image encoded as Base64 text
```

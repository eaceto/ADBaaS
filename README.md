![ADB as a Service](https://repository-images.githubusercontent.com/263344005/b581bd00-9e12-11ea-8b5a-01de42fb1d7d)

ADB as a Service (ADBaaS) is a small service that talks to Android ADB, emulator and other Android SDK tools, and provides a HTTP(s) API.

1. [Why I created it?](#why)
2. [Features](#features)
3. [Requirements](#requirements)
4. [Running](#running)
5. [Configuration](#configuration)
6. [API Documentation](#documentation)
7. [Usage](https://github.com/eaceto/ADBaaS/blob/master/docs/usage.md#using-adbaas)


## Why

Why creating a small wrapper around ADB and other Android SDK tools? Why implementing a HTTP API? Because a HTTP API is easier to integrate with other softwares for performing tasks like:

* Automated testings
* Running apps (and tests) on several devices simultaneuosly
* Sharing emulators and devices with multiple developers
* Running apps on remote emulators to free up local resources

## Features

- [x] List available emulators
- [x] List available devices and emulators
- [x] List applications installed on a device or emulator
- [x] Take a screenshot of the device or emulator (in PNG or Base64)
- [x] Install and uninstall an application
- [x] Revoke or grant permission on an application
- [x] Stream Logcat events in real time
- [x] HTTP(s) in all the endpoints
- [x] API access using API Key
- [x] API health monitoring

## Requirements

* **ANDROID_HOME** environment variable should be defined as defined in [Android documentation](https://developer.android.com/studio/command-line/variables)

* Java 11 or newer

## Running

```bash
java -jar adbaas.jar
```

## Configuration

* Changing server port

```bash
java -Dserver.port=9090 -jar adbaas.jar
```

* Setting allowed API Key

```bash
java -Dadbaas.api.keys.allowed=fa7166bd-eb36-4299-b04a-f91c7e2e907c -jar adbaas.jar
```

## Documentation 

### API Doc: 

* You can find online documentation at [https://eaceto.github.io/ADBaaS/index.html](https://eaceto.github.io/ADBaaS/index.html). There is also a Postman Collection that can be found at the */docs/postman* folder in the repository.
* OpenAPI 3.0.0 Spec: [https://eaceto.github.io/ADBaaS/openapi/openapi.yaml](https://eaceto.github.io/ADBaaS/openapi/openapi.yaml) 

### External references

* Generating certificates for localhost [https://letsencrypt.org/docs/certificates-for-localhost/](https://letsencrypt.org/docs/certificates-for-localhost/)

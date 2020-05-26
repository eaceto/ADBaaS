# ADBaaS
A small service that talks to Android ADB and Emulator using a HTTP API. Written in Java using Spring Boot.

![ADB as a Service](https://repository-images.githubusercontent.com/263344005/b581bd00-9e12-11ea-8b5a-01de42fb1d7d)

## Why

Why creating a small wrapper around ADB and other Android SDK tools? Why implementing a HTTP API? Because a HTTP API is easier to integrate with other softwares for performing tasks like:

* Automated testings
* Running apps (and tests) on several devices simultaneuosly
* Sharing emulators and devices with multiple developers
* Running apps on remote emulators to free up local resources

## Requisites

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

## Documentation 

### API Doc: 

* You can find online documentation at [https://eaceto.github.io/ADBaaS/index.html](https://eaceto.github.io/ADBaaS/index.html). There is also a Postman Collection that can be found at the */docs/postman* folder in the repository.
* Postman API collection: [https://eaceto.github.io/ADBaaS/postman/adbaas_api.json](https://eaceto.github.io/ADBaaS/postman/adbaas_api.json) 

### External references

* Generating certificates for localhost [https://letsencrypt.org/docs/certificates-for-localhost/](https://letsencrypt.org/docs/certificates-for-localhost/)
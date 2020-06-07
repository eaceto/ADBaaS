---
layout: default
---

A small service that talks to Android ADB and Emulator using a HTTP API. Written in Java using Spring Boot.

# Why

Why creating a small wrapper around ADB and other Android SDK tools? Why implementing a HTTP API? Because a HTTP API is easier to integrate with other softwares for performing tasks like:

* Automated testings
* Running apps (and tests) on several devices simultaneuosly
* Sharing emulators and devices with multiple developers
* Running apps on remote emulators to free up local resources

# Requisites

* **ANDROID_HOME** environment variable should be defined as defined in [Android documentation](https://developer.android.com/studio/command-line/variables)

* Java 11 or newer

# Running

```bash
java -jar adbaas.jar
```

# Changelog

Visit [https://eaceto.github.io/ADBaaS/CHANGELOG.html](https://eaceto.github.io/ADBaaS/CHANGELOG.html) for a detailed list of changes in the lastest releases.

# API documentation

The online documentation is located at [https://eaceto.github.io/ADBaaS/swagger/index.html](https://eaceto.github.io/ADBaaS/swagger/index.html). The OpenAPI 3.0.0 spec is available at [https://eaceto.github.io/ADBaaS/openapi/openapi.yaml](https://eaceto.github.io/ADBaaS/openapi/openapi.yaml)

# Support or Contact

- [Blog](https://eaceto.dev)
- [Linkedin](https://www.linkedin.com/in/ezequielaceto/)
- [E-mail](mailto:eaceto@pm.me) 

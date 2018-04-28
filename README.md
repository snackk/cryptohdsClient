# cryptohdsClient

## Overview

Before anything, the following requirements must be met:
* **cryptohdsLibrary** - You've got to have the library locally installed for this to work. You can find the steps to install it here [cryptohdsLibrary](https://github.com/snackk/cryptohdsLibrary).

## Running Client (Dev)

To run the App on the terminal do the following:
```sh
$ mvn clean install
$ mvn spring-boot:run
```

## Running Client (Docker)

You need to pass the instance's Ip's
```sh
$ mvn clean install
$ mvn spring-boot:run -Drest.ip=127.0.0.1
```

On IntelliJ there's no need of maven vodu, it has a spring button to run.

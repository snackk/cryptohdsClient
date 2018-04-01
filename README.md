# cryptohdsClient

## Overview

Before anything, the following requirements must be met:
* **cryptohdsLibrary** - You've got to have the library locally installed for this to work. You can find the steps to install it here [cryptohdsLibrary](https://github.com/snackk/cryptohdsLibrary).

You have to create a directory named "KeyStore" under "src", this is where we store the client's keys.

## Running Client

To run the App on the terminal do the following:
```sh
$ mvn clean install 
$ mvn spring-boot:run
```

On IntelliJ there's no need of maven vodu, it has a spring button to run.

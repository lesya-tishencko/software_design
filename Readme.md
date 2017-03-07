# HW01 - Command Line Interpreter

**Shell** - main class. Has method main() to run CLI.

**Preprocess** - class to process line, which entered by user.

**Parser** - class to parse commands and their args from string. Take entered line and parse line. Save command and their args in fields commands and params. These fields has getter methods.

**Factory** - class to create commands. 
 - Has static method which get name of command and return object of type Factory (all commands - classes inherit Factory and override method execute()).
 - if command not found then create default Factory object and method his method execute run System process.
 - Derived class:
    - **Echo** - derived class from Factory. Override execute which method work as echo in command-line *nix system.
    - **Cat** - derived class from Factory. Override execute method which work as cat in command-line *nix system.
    - **Exit** - derived class from Factory. Call execute to exit.
    - **Wc** - derived class from Factory. Override execute method which work as wc in command-line *nix system.
    - **Pwd** - derived class from Factory. Override execute method which work as pwd in command-line *nix system.


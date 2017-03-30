# HW01 - Command Line Interpreter

**Shell** - main class. Has method main() to run CLI.

**Preprocess** - class to process line, which entered by user.

**Parser** - class to parse commands and their args from string. Take entered line and parse line. Save command and their args in fields commands and params. These fields has getter methods.

**Runnable** - interface for all commands.
 - Has static method which get name of command and return object of class which implements Runnable (all commands implement this interface).
 - if command not found then create object of default class **NewCommand**, his method execute run System process.
 - Classes which implement Runnable:
    - **Echo** override execute which method work as echo in command-line *nix system.
    - **Cat**  override execute method which work as cat in command-line *nix system.
    - **Exit** Call execute to exit.
    - **Wc**  override execute method which work as wc in command-line *nix system.
    - **Pwd** override execute method which work as pwd in command-line *nix system.
    - **Assignment** override execute method to assign variable with its value.

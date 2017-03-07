package ru.spbau.mit.sd;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Factory class to create object of required commands.
 * has method execute which must override in all inheritors
 */
class Factory {
    private static final HashSet<String> COMMANDS =
            new HashSet<>(Arrays.asList("echo", "cat", "wc", "pwd", "exit"));
    private static String commandName;

    /**
     * static method to create object of required command
     * if command not found in list of COMMANDS then create default command
     * which run system process
     * @param command name of command
     * @return object of command type
     */
    static Factory getCommand(String command) {
        if (COMMANDS.contains(command)) {
            try {
                String commandName = command.substring(0, 1).toUpperCase()
                        + command.substring(1);
                return (Factory) Class.forName("ru.spbau.mit.sd." + commandName).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            throw new IllegalStateException("Error while class loading");
        }
        commandName = command;
        return new Factory();
    }

    /**
     * execute System process for command which are not in our list COMMANDS
     * @param ios IOStream object for write and read
     * @param param parameters of command cat
     */
    public void execute(IOStream ios, String param) {
        try {
            Process process = new ProcessBuilder(commandName, param).start();
            process.waitFor();
            InputStream is = process.getInputStream();
            byte[] buf = new byte[is.available()];
            int len = is.read(buf);
            ios.write(buf, 0, len);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


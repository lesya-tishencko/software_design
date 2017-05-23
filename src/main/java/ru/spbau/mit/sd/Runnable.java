package ru.spbau.mit.sd;

import ru.spbau.mit.sd.command.NewCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Runnable class to create object of required commands.
 * has method execute which must override in all inheritors
 */
public interface Runnable {
     HashSet<String> COMMANDS =
            new HashSet<>(Arrays.asList("echo", "cat", "wc", "pwd", "exit", "assignment", "cd", "ls"));

    /**
     * static method to create object of required command
     * if command not found in list of COMMANDS then create NewCommand
     * which run system process
     * @param command name of command
     * @return object of command type
     */
    static Runnable getCommand(String command) {
        if (COMMANDS.contains(command)) {
            try {
                String commandName = command.substring(0, 1).toUpperCase()
                        + command.substring(1);
                return (Runnable) Class.forName("ru.spbau.mit.sd.command." + commandName).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            throw new IllegalStateException("Error while class loading");
        }
        return new NewCommand(command);
    }

    /**
     * execute command
     * @param ios IOStream object for write and read
     * @param param parameters of command cat
     */
    void execute(Environment env, InputStream inputStream,
                 OutputStream outputStream, String param);
}


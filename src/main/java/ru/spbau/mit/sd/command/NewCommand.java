package ru.spbau.mit.sd.command;

import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * class implements Runnable for system Process.
 */
public class NewCommand implements Runnable {
    private String commandName;

    public NewCommand(String commandName) {
        this.commandName = commandName;
    }

    /**
     * execute System process for command which are not in our list COMMANDS
     * @param env - Environment
     * @param inputStream - stream to read data
     * @param outputStream - stream to write result
     * @param param parameters of command cat
     */
    @Override
    public void execute(Environment env, InputStream inputStream,
                        OutputStream outputStream, String param) {
        try {
            Process process = new ProcessBuilder(commandName, param).start();
            process.waitFor();
            InputStream is = process.getInputStream();
            byte[] buf = new byte[is.available()];
            int len = is.read(buf);
            outputStream.write(buf, 0, len);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

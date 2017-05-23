package ru.spbau.mit.sd.command;


import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * class implements Runnable and show the currend directory.
 */
public class Pwd implements Runnable {

    /**
     * write current directory to outputstream
     * @param env - Environment
     * @param inputStream - stream to read data
     * @param outputStream - stream to write result
     * @param param parameters of command cat
     */
    @Override
    public void execute(Environment env, InputStream inputStream,
                        OutputStream outputStream, String param) {
        try {
            outputStream.write((env.get("PWD") + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            throw new CommandException("pwd: cannot write to outputstream");
        }
    }
}


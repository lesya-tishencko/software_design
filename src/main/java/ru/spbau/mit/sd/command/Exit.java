package ru.spbau.mit.sd.command;

import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;

import java.io.InputStream;
import java.io.OutputStream;
/**
 * class implements Runnable to exit from cli.
 */
public class Exit implements Runnable {

    /**
     * exit from command line interpreter
     * @param env - Environment
     * @param inputStream - stream to read data
     * @param outputStream - stream to write result
     * @param param parameters of command cat
     */
    @Override
    public void execute(Environment env, InputStream inputStream,
                        OutputStream outputStream, String param) {
        System.exit(0);
    }
}


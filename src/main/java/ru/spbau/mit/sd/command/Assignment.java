package ru.spbau.mit.sd.command;

import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * class to implement operator = in our shell.
 */
public class Assignment implements Runnable {

    /**
     *
     * @param env - Environment
     * @param inputStream - stream to read data
     * @param outputStream - stream to write result
     * @param param variable and its value separated by whitespace
     */
    @Override
    public void execute(Environment env, InputStream inputStream,
                 OutputStream outputStream, String param) {
        int ind = param.indexOf(" ");
        env.put(param.substring(0, ind), param.substring(ind + 1));
    }
}

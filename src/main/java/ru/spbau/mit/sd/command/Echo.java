package ru.spbau.mit.sd.command;

import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class implements Runnable and work as echo command in *nix systems.
 */
public class Echo implements Runnable {

    /**
     * First parse(param) - to delete quotes, redundant whitespace and etc.
     * Second write all data to outputStream
     * @param env - Environment
     * @param inputStream - stream to read data
     * @param outputStream - stream to write result
     * @param param parameters of command cat
     */
    @Override
    public void execute(Environment env, InputStream inputStream,
                        OutputStream outputStream, String param) {
        byte[] str = (parseParam(param) + System.lineSeparator()).getBytes();
        try {
            outputStream.write(str, 0, str.length);
        } catch (IOException e) {
            throw new CommandException("echo: cannot write to outputstream");
        }
    }

    private String parseParam(String param) {
        StringBuilder sb = new StringBuilder();
        param = param.trim();
        Matcher matcher = Pattern.compile("([\"\'])(.*?)(\\1)").matcher(param);
        int start = 0;
        while (matcher.find(start)) {
            sb.append(param.substring(start, matcher.start()));
            sb.append(matcher.group(2));
            start = matcher.end();
        }
        sb.append(param.substring(start));
        return sb.toString();
    }

}


package ru.spbau.mit.sd.command;
import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;

import java.io.*;

/**
 * class implements Runnable and work as cat command in *nix systems.
 */
public class Cat implements Runnable {

    private static final int bufSize = 1024;
    /**
     * Implement method in Runnable.
     * if @param.length == 0 then read from inputStream and write all data to outputStream
     * otherwise read data from file which names take as parameters (param)
     * and write data to ios
     * @param env - Environment
     * @param inputStream - stream to read data
     * @param outputStream - stream to write result
     * @param param parameters of command cat
     */
    @Override
    public void execute(Environment env, InputStream inputStream,
                        OutputStream outputStream, String param) {
        if (param.length() == 0) {
            byte[] buf = new byte[bufSize];
            int ch;
            int i = 0;
            try {
                while (inputStream.available() > 0 && (ch = inputStream.read()) != -1) {
                    buf[i] = (byte) ch;
                    ++i;
                }
                outputStream.write(buf, 0, i);
            } catch (IOException e) {
                throw new CommandException("cat: cannot read/write from/to stream");
            }
        } else {
            File file;
            int off = 0;
            for (String arg : param.trim().split("\\s+")) {
                try {
                    file = new File(arg);
                    if (file.isDirectory()) {
                        outputStream.write(("cat: " + arg + ": Is a directory"
                                + System.lineSeparator()).getBytes());
                        continue;
                    }
                    if (!file.exists()) {
                        outputStream.write(("cat: " + arg + ": No such file or directory"
                                + System.lineSeparator()).getBytes());
                    }
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buf = new byte[(int) file.length()];
                    int len = fis.read(buf, 0, buf.length);
                    outputStream.write(buf, off, len);
                    off = len;
                } catch (IOException e) {
                    throw new CommandException("cat: cannot read/write from/to filestream");
                }
            }
        }
    }
}


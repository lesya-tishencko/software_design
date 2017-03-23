package ru.spbau.mit.sd.command;


import ru.spbau.mit.sd.Environment;
import ru.spbau.mit.sd.Runnable;
import java.io.*;
import java.util.Vector;
/**
 * class implements Runnable and work as wc command in *nix systems.
 */
public class Wc implements Runnable {

    private static final int bufSize = 1024;

    private static void wcImpl(OutputStream outputStream, byte[] buf, int byteCount) throws IOException {
        int newLines = 0;
        int wordCount = 0;
        int size = byteCount;
        byte newline = System.lineSeparator().getBytes()[0];
        byte space = " ".getBytes()[0];
        for (int i = 0; i < size; ++i) {
            if (newline == buf[i]) {
                ++newLines;
                ++wordCount;
            }
            if (space == buf[i]) {
                ++wordCount;
            }
        }
        outputStream.write(("\t" + Integer.toString(newLines)
                + "\t" + Integer.toString(wordCount)
                + "\t" + Integer.toString(size)).getBytes());
    }

    /**
     * count the number of newlines, word and byte.
     * if no param then read data from ios
     * otherwise read param.
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
                wcImpl(outputStream, buf, i);
                outputStream.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                throw new CommandException("wc: cannot read/write from/to stream");
            }
        } else {
            for (String arg: parseParam(param)) {
                try {
                    File file = new File(arg);
                    if (file.isDirectory()) {
                        outputStream.write(("wc: " + arg + ": Is a directory"
                                + System.lineSeparator()).getBytes());
                        continue;
                    }
                    if (!file.exists()) {
                        outputStream.write(("wc: " + arg + ": No such file or directory"
                                + System.lineSeparator()).getBytes());
                    }
                    byte[] buf = new byte[(int) file.length()];
                    FileInputStream fis = new FileInputStream(file);
                    int temp = fis.read(buf, 0, buf.length);
                    wcImpl(outputStream, buf, temp);
                    outputStream.write((" " + arg + System.lineSeparator()).getBytes());
                } catch (IOException e) {
                     throw new CommandException("wc: cannot read/write from/to stream");
                }
            }
        }
    }

    private Vector<String> parseParam(String param) {
        Vector<String> args = new Vector<>();
        param = param.trim();
        for (int i = 0; i < param.length(); ++i) {
            if (param.charAt(i) == '\"' || param.charAt(i) == '\'') {
                int j = i;
                ++i;
                while (i < param.length() && param.charAt(i) != param.charAt(j)) {
                    ++i;
                }
                args.add(param.substring(j + 1, i));
            } else {
                if (param.charAt(i) != ' ') {
                    int j = i;
                    ++i;
                    while (i < param.length() && param.charAt(i) != ' ') {
                        ++i;
                    }
                    args.add(param.substring(j, i));
                }
            }
        }
        return args;
    }
}

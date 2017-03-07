package ru.spbau.mit.sd;


import java.io.*;
import java.util.Vector;

class Wc extends Factory {

    private static void wcImpl(IOStream ios, byte[] buf, int byteCount) {
        int newLines = 0;
        int wordCount = 1;
        int size;
        size = byteCount;
        byte newline = System.lineSeparator().getBytes()[0];
        byte space = " ".getBytes()[0];
        for (int i = 0; i < size; ++i) {
            if (newline == buf[i]) {
                ++newLines;
            }
            if (space == buf[i]) {
                ++wordCount;
            }
        }
        ios.write(("\t" + Integer.toString(newLines)
                + "\t" + Integer.toString(wordCount)
                + "\t" + Integer.toString(size)).getBytes());
    }

    /**
     * count the number of newlines, word and byte.
     * if no param then read data from ios
     * otherwise read param.
     * @param ios IOStream object for write and read
     * @param param parameters of command cat
     */
    @Override
    public void execute(IOStream ios, String param) {
        if (param.length() == 0) {
            byte[] buf = new byte[ios.bufSize()];
            int temp = 0;
            try {
                temp = ios.read(buf);
                wcImpl(ios, buf, temp);
                ios.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File file;
            for (String arg: parseParam(param)) {
                try {
                    file = new File(arg);
                    int temp = 0;
                    byte[] buf = new byte[(int) file.length()];
                    temp = ios.readFile(file, buf);
                    wcImpl(ios, buf, temp);
                    ios.write((" " + arg + System.lineSeparator()).getBytes());
                } catch (FileIsDirectoryException e) {
                    ios.write(("wc: " + arg + ": Is a directory"
                            + System.lineSeparator()).getBytes());
                } catch (FileNotFoundException e) {
                    ios.write(("wc: " + arg + ": No such file or directory"
                            + System.lineSeparator()).getBytes());
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

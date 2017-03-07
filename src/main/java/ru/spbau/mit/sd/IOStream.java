package ru.spbau.mit.sd;

import java.io.*;
import java.io.FileNotFoundException;

class IOStream {
    private static final int DEFAULT_BUF_SIZE = 4096;
    private PipedInputStream pis = new PipedInputStream(DEFAULT_BUF_SIZE);
    private PipedOutputStream pos = new PipedOutputStream();

    IOStream() {
        try {
            pis.connect(pos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int available() {
        int temp = 0;
        try {
            temp = pis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
    int read(byte[] buf) throws IOException {
        int available = 0;
        int res = 0;
        try {
            available = pis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (available > 0) {
            try {
                res = pis.read(buf);
            } catch (IOException e) {
                throw e;
            }
        } else {
            BufferedInputStream bufIn = new BufferedInputStream(System.in);
            res = bufIn.read(buf, 0, buf.length);
        }

        return res;
    }

    void write(byte[] buf) {
        try {
            pos.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int bufSize() {
        return DEFAULT_BUF_SIZE;
    }

    void write(byte[] buf, int off, int len) {
        try {
            pos.write(buf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int readLine(byte[] buf) throws IOException {
//        int available = 0;
        int res = 0;
//        try {
//            available = pis.available();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        BufferedInputStream bufIn = new BufferedInputStream(System.in);
//        if (available > 0) {
//            bufIn = new BufferedInputStream(pis);
//        } else {
//            bufIn = new BufferedInputStream(System.in);
//        }
        int temp = 0;
        byte newline = System.lineSeparator().getBytes()[0];
        while (res < buf.length) {
            buf[res] = (byte) bufIn.read();
            if (newline == buf[res]) {
                break;
            }
            ++res;
        }
        return res;
    }

    int readFile(File file, byte[] buf) throws FileIsDirectoryException, FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getName() + " not found");
        }
        if (!file.isFile()) {
            throw new FileIsDirectoryException(file.getName() + " is Directory!");
        }
        int res = 0;
        try {
            FileInputStream fin = new FileInputStream(file);
            res = fin.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}


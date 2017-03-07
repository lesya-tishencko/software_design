package ru.spbau.mit.sd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Echo extends Factory {

    /**
     * First parse(param) - to delete quotes, redundant whitespace and etc.
     * Second write all data to ios
     * @param ios IOStream object for write and read
     * @param param parameters of command cat
     */
    @Override
    public void execute(IOStream ios, String param) {
        byte[] str = (parseParam(param) + System.lineSeparator()).getBytes();
        ios.write(str, 0, str.length);
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


package ru.spbau.mit.sd;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for processing line
 */
public class Preprocessor {

    private static Pattern patternForSingleQuotes = Pattern.compile("\'.*?\'");
    private static Pattern patternForSetVar = Pattern.compile("(\\$(\\w+))");

    /**
     * preprocessing line to get variables
     * set variables value instead of $variables
     * @param env - Environment
     * @param line - input string
     * @return line after processing
     */
    String preprocess(Environment env, String line) {

        line = removeDoubleSpace(line);
        line = setVariable(env, line);
        return line;
    }

    private static String removeDoubleSpace(String line) {
        line = line.trim();
        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) == '"' || line.charAt(i) == '\'') {
                int j = i;
                ++i;
                while (i < line.length() && line.charAt(i) != line.charAt(j)) {
                    ++i;
                }
                --i;
            }
            if (line.charAt(i) == ' ') {
                int j = i + 1;
                while (i < line.length() && line.charAt(i) == ' ') {
                    ++i;
                }
                line = line.substring(0, j) + line.substring(i);
                i = j - 1;
            }
        }
        return line;
    }

    private String setVariable(Environment env, String line) {
        Matcher matchQuotes = patternForSingleQuotes.matcher(line);
        boolean hasQuotes = matchQuotes.find();
        Matcher matchVar = patternForSetVar.matcher(line);
        int varStart = 0;
        while (matchVar.find(varStart)) {

            if (hasQuotes && matchVar.start() > matchQuotes.start()
                    && matchVar.end() < matchQuotes.end()) {
                varStart = matchVar.end();
                continue;
            }

            if (hasQuotes && matchVar.start() > matchQuotes.end()) {
                hasQuotes = matchQuotes.find(matchQuotes.end());
                continue;
            }

            if (!env.containsKey(matchVar.group(2))) {
                line = line.substring(0, matchVar.start()).concat(line.substring(matchVar.end()));
                varStart = matchVar.start();
                matchVar = matchVar.reset(line);
                continue;
            }

            line = line.substring(0, matchVar.start()).concat(env.get(matchVar.group(2)))
                    .concat(line.substring(matchVar.end()));
            varStart = matchVar.start() + env.get(matchVar.group(2)).length();
            matchVar = matchVar.reset(line);
        }
        return line.trim();
    }

}


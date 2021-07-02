package fr.atesab.atiantengine;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartialRead {
    int left;
    int right;
    final String text;

    PartialRead(String text) {
        this.text = text;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public boolean canRead(String text) {
        if (text == null)
            return canRead(0);
        return canRead(text.length());
    }

    public boolean canRead(int n) {
        return right + n <= this.text.length();
    }

    public boolean next(String text) {
        return text == null || canRead(text) && this.text.substring(right).startsWith(text);
    }

    public boolean walk() {
        return walk(1);
    }

    public boolean walk(int value) {
        return (right = Math.min(text.length(), right + value)) < text.length();
    }

    public Matcher walkAndRead(Pattern pattern) {
        Matcher matcher = pattern.matcher(text).region(right, text.length());
        if (matcher.find() && matcher.start() == right) {
            int end = matcher.end();
            right = end;
            return matcher;
        }
        return null;
    }

    public String use() {
        String s = text.substring(left, right);
        left = right;
        return s;
    }

    public String walkAndUse(int value) {
        walk(value);
        return use();
    }

    public void walkSpaces() {
        while (canRead(1) && (Character.isWhitespace(text.charAt(right)) || text.charAt(right) == '\n'
                || text.charAt(right) == '\r')) {
            walk(1);
        }
    }

    public void useSpaces() {
        walkSpaces();
        use();
    }

    public String getLastState() {
        String before = text.substring(Math.max(left - 5, 0), left);
        String read = text.substring(left, right);
        String after = right != text.length() ? text.substring(right, Math.min(right + 5, text.length())) : "";

        return before + "|" + read + "|" + after;
    }
}
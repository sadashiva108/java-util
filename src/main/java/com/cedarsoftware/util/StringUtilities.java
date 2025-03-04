package com.cedarsoftware.util;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

/**
 * Useful String utilities for common tasks
 *
 * @author Ken Partlow
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         <a href="http://www.apache.org/licenses/LICENSE-2.0">License</a>
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public final class StringUtilities
{
    private static final char[] _hex = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    public static final String FOLDER_SEPARATOR = "/";

    public static final String EMPTY = "";

    /**
     * <p>Constructor is declared private since all methods are static.</p>
     */
    private StringUtilities()
    {
        super();
    }

    /**
     * Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters.
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is <strong>case-sensitive</strong>.</p>
     *
     * @param cs1  the first CharSequence, may be {@code null}
     * @param cs2  the second CharSequence, may be {@code null}
     * @return {@code true} if the CharSequences are equal (case-sensitive), or both {@code null}
     * @see #equalsIgnoreCase(CharSequence, CharSequence)
     */
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        // Step-wise comparison
        final int length = cs1.length();
        for (int i = 0; i < length; i++) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters, ignoring case.
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered equal. The comparison is <strong>case insensitive</strong>.</p>
     *
     * @param cs1  the first CharSequence, may be {@code null}
     * @param cs2  the second CharSequence, may be {@code null}
     * @return {@code true} if the CharSequences are equal (case-insensitive), or both {@code null}
     * @see #equals(CharSequence, CharSequence)
     */
    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        return regionMatches(cs1, true, 0, cs2, 0, cs1.length());
    }

    /**
     * Green implementation of regionMatches.
     *
     * @param cs the {@link CharSequence} to be processed
     * @param ignoreCase whether or not to be case-insensitive
     * @param thisStart the index to start on the {@code cs} CharSequence
     * @param substring the {@link CharSequence} to be looked for
     * @param start the index to start on the {@code substring} CharSequence
     * @param length character length of the region
     * @return whether the region matched
     */
    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length)    {
        Convention.throwIfNull(cs, "cs to be processed cannot be null");
        Convention.throwIfNull(substring, "substring cannot be null");

        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Check for invalid parameters
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }

        // Check that the regions are long enough
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The real same check as in String.regionMatches():
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equalsWithTrim(final String s1, final String s2)
    {
        if (s1 == null || s2 == null)
        {
            return s1 == s2;
        }
        return s1.trim().equals(s2.trim());
    }

    public static boolean equalsIgnoreCaseWithTrim(final String s1, final String s2)
    {
        if (s1 == null || s2 == null)
        {
            return s1 == s2;
        }
        return s1.trim().equalsIgnoreCase(s2.trim());
    }

    /**
     * Checks if a CharSequence is empty (""), null, or only whitespace.
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     */
    public static boolean isEmpty(CharSequence cs)
    {
        return isWhitespace(cs);
    }

    /**
     * Checks if a CharSequence is not empty (""), not null and not whitespace only.
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is
     *  not empty and not null and not whitespace only
     */
    public static boolean isNotWhitespace(final CharSequence cs) {
        return !isWhitespace(cs);
    }

    /**
     * Checks if a CharSequence is empty (""), null or whitespace only.
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace only
     */
    public static boolean isWhitespace(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a CharSequence is not null, not empty (""), and not only whitespace.
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isWhitespace(cs);
    }

    /**
     * Checks if a CharSequence is not empty (""), not null and not whitespace only.
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is
     *  not empty and not null and not whitespace only
     */
    public static boolean hasContent(final CharSequence cs) {
        return !isWhitespace(cs);
    }

    /**
     * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
     *
     * @param cs a CharSequence or {@code null}
     * @return CharSequence length or {@code 0} if the CharSequence is {@code null}.
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * @param s a String or {@code null}
     * @return the trimmed length of the String or 0 if the string is null.
     */
    public static int trimLength(final String s) {
        return trimToEmpty(s).length();
    }


    public static int lastIndexOf(String path, char ch) {
        if (path == null) {
            return -1;
        }
        return path.lastIndexOf(ch);
    }

    // Turn hex String into byte[]
    // If string is not even length, return null.

    public static byte[] decode(String s)
    {
        final int len = s.length();
        if (len % 2 != 0)
        {
            return null;
        }

        byte[] bytes = new byte[len / 2];
        int pos = 0;

        for (int i = 0; i < len; i += 2)
        {
            byte hi = (byte) Character.digit(s.charAt(i), 16);
            byte lo = (byte) Character.digit(s.charAt(i + 1), 16);
            bytes[pos++] = (byte) (hi * 16 + lo);
        }

        return bytes;
    }

    /**
     * Convert a byte array into a printable format containing a
     * String of hex digit characters (two per byte).
     *
     * @param bytes array representation
     */
    public static String encode(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder(bytes.length << 1);
        for (byte aByte : bytes)
        {
            sb.append(convertDigit(aByte >> 4));
            sb.append(convertDigit(aByte & 0x0f));
        }
        return sb.toString();
    }

    /**
     * Convert the specified value (0 .. 15) to the corresponding hex digit.
     *
     * @param value to be converted
     * @return '0'..'F' in char format.
     */
    private static char convertDigit(int value)
    {
        return _hex[value & 0x0f];
    }

    public static int count(String s, char c)
    {
        return count (s, EMPTY + c);
    }

    /**
     * Count the number of times that 'token' occurs within 'content'.
     * @return int count (0 if it never occurs, null is the source string, or null is the token).
     */
    public static int count(CharSequence content, CharSequence token)
    {
        if (content == null || token == null)
        {
            return 0;
        }

        String source = content.toString();
        if (source.isEmpty())
        {
            return 0;
        }
        String sub = token.toString();
        if (sub.isEmpty())
        {
            return 0;
        }

        int answer = 0;
        int idx = 0;

        while (true)
        {
            idx = source.indexOf(sub, idx);
            if (idx < answer)
            {
                return answer;
            }
            answer = ++answer;
            idx = ++idx;
        }
    }

    /**
     * Convert strings containing DOS-style '*' or '?' to a regex String.
     */
    public static String wildcardToRegexString(String wildcard)
    {
        final int len = wildcard.length();
        StringBuilder s = new StringBuilder(len);
        s.append('^');
        for (int i = 0; i < len; i++)
        {
            char c = wildcard.charAt(i);
            switch (c)
            {
                case '*':
                    s.append(".*");
                    break;

                case '?':
                    s.append('.');
                    break;

                // escape special regexp-characters
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '^':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    s.append('\\');
                    s.append(c);
                    break;

                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return s.toString();
    }

    /**
     * The Levenshtein distance is a string metric for measuring the difference between two sequences.
     * Informally, the Levenshtein distance between two words is the minimum number of single-character edits
     * (i.e. insertions, deletions or substitutions) required to change one word into the other. The phrase
     * 'edit distance' is often used to refer specifically to Levenshtein distance.
     *
     * @param s String one
     * @param t String two
     * @return the 'edit distance' (Levenshtein distance) between the two strings.
     */
    public static int levenshteinDistance(CharSequence s, CharSequence t)
    {
        // degenerate cases          s
        if (s == null || EMPTY.equals(s))
        {
            return t == null || EMPTY.equals(t) ? 0 : t.length();
        }
        else if (t == null || EMPTY.equals(t))
        {
            return s.length();
        }

        // create two work vectors of integer distances
        int[] v0 = new int[t.length() + 1];
        int[] v1 = new int[t.length() + 1];

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++)
        {
            v0[i] = i;
        }

        int sLen = s.length();
        int tLen = t.length();
        for (int i = 0; i < sLen; i++)
        {
            // calculate v1 (current row distances) from the previous row v0

            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < tLen; j++)
            {
                int cost = (s.charAt(i) == t.charAt(j)) ? 0 : 1;
                v1[j + 1] = (int) MathUtilities.minimum(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            System.arraycopy(v1, 0, v0, 0, v0.length);
        }

        return v1[t.length()];
    }

    /**
     * Calculate the Damerau-Levenshtein Distance between two strings.  The basic difference
     * between this algorithm and the general Levenshtein algorithm is that damerau-Levenshtein
     * counts a swap of two characters next to each other as 1 instead of 2.  This breaks the
     * 'triangular equality', which makes it unusable for Metric trees.  See Wikipedia pages on
     * both Levenshtein and Damerau-Levenshtein and then make your decision as to which algorithm
     * is appropriate for your situation.
     *
     * @param source Source input string
     * @param target Target input string
     * @return The number of substitutions it would take
     * to make the source string identical to the target
     * string
     */
    public static int damerauLevenshteinDistance(CharSequence source, CharSequence target)
    {
        if (source == null || EMPTY.equals(source))
        {
            return target == null || EMPTY.equals(target) ? 0 : target.length();
        }
        else if (target == null || EMPTY.equals(target))
        {
            return source.length();
        }

        int srcLen = source.length();
        int targetLen = target.length();
        int[][] distanceMatrix = new int[srcLen + 1][targetLen + 1];

        // We need indexers from 0 to the length of the source string.
        // This sequential set of numbers will be the row "headers"
        // in the matrix.
        for (int srcIndex = 0; srcIndex <= srcLen; srcIndex++)
        {
            distanceMatrix[srcIndex][0] = srcIndex;
        }

        // We need indexers from 0 to the length of the target string.
        // This sequential set of numbers will be the
        // column "headers" in the matrix.
        for (int targetIndex = 0; targetIndex <= targetLen; targetIndex++)
        {
            // Set the value of the first cell in the column
            // equivalent to the current value of the iterator
            distanceMatrix[0][targetIndex] = targetIndex;
        }

        for (int srcIndex = 1; srcIndex <= srcLen; srcIndex++)
        {
            for (int targetIndex = 1; targetIndex <= targetLen; targetIndex++)
            {
                // If the current characters in both strings are equal
                int cost = source.charAt(srcIndex - 1) == target.charAt(targetIndex - 1) ? 0 : 1;

                // Find the current distance by determining the shortest path to a
                // match (hence the 'minimum' calculation on distances).
                distanceMatrix[srcIndex][targetIndex] = (int) MathUtilities.minimum(
                        // Character match between current character in
                        // source string and next character in target
                        distanceMatrix[srcIndex - 1][targetIndex] + 1,
                        // Character match between next character in
                        // source string and current character in target
                        distanceMatrix[srcIndex][targetIndex - 1] + 1,
                        // No match, at current, add cumulative penalty
                        distanceMatrix[srcIndex - 1][targetIndex - 1] + cost);

                // We don't want to do the next series of calculations on
                // the first pass because we would get an index out of bounds
                // exception.
                if (srcIndex == 1 || targetIndex == 1)
                {
                    continue;
                }

                // transposition check (if the current and previous
                // character are switched around (e.g.: t[se]t and t[es]t)...
                if (source.charAt(srcIndex - 1) == target.charAt(targetIndex - 2) && source.charAt(srcIndex - 2) == target.charAt(targetIndex - 1))
                {
                    // What's the minimum cost between the current distance
                    // and a transposition.
                    distanceMatrix[srcIndex][targetIndex] = (int) MathUtilities.minimum(
                            // Current cost
                            distanceMatrix[srcIndex][targetIndex],
                            // Transposition
                            distanceMatrix[srcIndex - 2][targetIndex - 2] + cost);
                }
            }
        }

        return distanceMatrix[srcLen][targetLen];
    }

    /**
     * @param random Random instance
     * @param minLen minimum number of characters
     * @param maxLen maximum number of characters
     * @return String of alphabetical characters, with the first character uppercase (Proper case strings).
     */
    public static String getRandomString(Random random, int minLen, int maxLen)
    {
        StringBuilder s = new StringBuilder();
        final int len = minLen + random.nextInt(maxLen - minLen + 1);

        for (int i=0; i < len; i++)
        {
            s.append(getRandomChar(random, i == 0));
        }
        return s.toString();
    }

    public static String getRandomChar(Random random, boolean upper)
    {
        int r = random.nextInt(26);
        return upper ? EMPTY + (char)((int)'A' + r) : EMPTY + (char)((int)'a' + r);
    }

    /**
     * Convert a String into a byte[] with a particular encoding.
     * Preferable used when the encoding is one of the guaranteed Java types
     * and you don't want to have to catch the UnsupportedEncodingException
     * required by Java
     *
     * @param s        string to encode into bytes
     * @param encoding encoding to use
     */
    public static byte[] getBytes(String s, String encoding)
    {
        try
        {
            return s == null ? null : s.getBytes(encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException(String.format("Encoding (%s) is not supported by your JVM", encoding), e);
        }
    }


    /**
     * Convert a byte[] into a UTF-8 String.  Preferable used when the encoding
     * is one of the guaranteed Java types and you don't want to have to catch
     * the UnsupportedEncodingException required by Java
     *
     * @param bytes bytes to encode into a string
     */
    public static String createUtf8String(byte[] bytes)
    {
        return createString(bytes, "UTF-8");
    }

    /**
     * Convert a String into a byte[] encoded by UTF-8.
     *
     * @param s        string to encode into bytes
     */
    public static byte[] getUTF8Bytes(String s)
    {
        return getBytes(s, "UTF-8");
    }

    /**
     * Convert a byte[] into a String with a particular encoding.
     * Preferable used when the encoding is one of the guaranteed Java types
     * and you don't want to have to catch the UnsupportedEncodingException
     * required by Java
     *
     * @param bytes    bytes to encode into a string
     * @param encoding encoding to use
     */
    public static String createString(byte[] bytes, String encoding)
    {
        try
        {
            return bytes == null ? null : new String(bytes, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException(String.format("Encoding (%s) is not supported by your JVM", encoding), e);
        }
    }

    /**
     * Convert a byte[] into a UTF-8 encoded String.
     *
     * @param bytes    bytes to encode into a string
     */
    public static String createUTF8String(byte[] bytes)
    {
        return createString(bytes, "UTF-8");
    }

    /**
     * Get the hashCode of a String, insensitive to case, without any new Strings
     * being created on the heap.
     * @param s String input
     * @return int hashCode of input String insensitive to case
     */
    public static int hashCodeIgnoreCase(String s)
    {
        if (s == null)
        {
            return 0;
        }
        final int len = s.length();
        int hash = 0;
        for (int i = 0; i < len; i++)
        {
            hash = 31 * hash + Character.toLowerCase((int)s.charAt(i));
        }
        return hash;
    }

    /**
     * Removes control characters (char &lt;= 32) from both
     * ends of this String, handling {@code null} by returning
     * {@code null}.
     *
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     *
     * @param str  the String to be trimmed, may be null
     * @return the trimmed string, {@code null} if null String input
     */
    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }

    /**
     * Trims a string, its null safe and null will return empty string here..
     * @param value string input
     * @return String trimmed string, if value was null this will be empty
     */
    public static String trimToEmpty(String value) {
        return value == null ? EMPTY : value.trim();
    }

    /**
     * Trims a string, If the string trims to empty then we return null.
     * @param value string input
     * @return String, trimmed from value.  If the value was empty we return null.
     */
    public static String trimToNull(String value) {
        final String ts = trim(value);
        return isEmpty(ts) ? null : ts;
    }

    /**
     * Trims a string, If the string trims to empty then we return the default.
     * @param value string input
     * @param defaultValue value to return on empty or null
     * @return trimmed string, or defaultValue when null or empty
     */
    public static String trimEmptyToDefault(String value, String defaultValue) {
        return Optional.ofNullable(value).map(StringUtilities::trimToNull).orElse(defaultValue);
    }
}

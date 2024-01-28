package com.cedarsoftware.util.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.cedarsoftware.util.StringUtilities;

/**
 * @author Kenny Partlow (kpartlow@gmail.com)
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
public class NumberConversions {

    static byte toByte(Object from, Converter converter, ConverterOptions options) {
        return ((Number)from).byteValue();
    }

    static Byte toByteZero(Object from, Converter converter, ConverterOptions options) {
        return CommonValues.BYTE_ZERO;
    }

    static short toShort(Object from, Converter converter, ConverterOptions options) {
        return ((Number)from).shortValue();
    }

    static Short toShortZero(Object from, Converter converter, ConverterOptions options) {
        return CommonValues.SHORT_ZERO;
    }

    static int toInt(Object from, Converter converter, ConverterOptions options) {
        return ((Number)from).intValue();
    }

    static Integer toIntZero(Object from, Converter converter, ConverterOptions options) {
        return CommonValues.INTEGER_ZERO;
    }

    static long toLong(Object from, Converter converter, ConverterOptions options) {
        return toLong(from);
    }

    static long toLong(Object from) {
        return ((Number) from).longValue();
    }

    static int toInt(Object from) {
        return ((Number)from).intValue();
    }

    static Long toLongZero(Object from, Converter converter, ConverterOptions options) {
        return CommonValues.LONG_ZERO;
    }

    static float toFloat(Object from, Converter converter, ConverterOptions options) {
        return ((Number) from).floatValue();
    }

    static Float toFloatZero(Object from, Converter converter, ConverterOptions options) {
        return CommonValues.FLOAT_ZERO;
    }

    static String floatToString(Object from, Converter converter, ConverterOptions option) {
        return new DecimalFormat("#.####################").format(from);
    }

    static double toDouble(Object from, Converter converter, ConverterOptions options) {
        return toDouble(from);
    }

    static double toDouble(Object from) {
        return ((Number) from).doubleValue();
    }

    static Double toDoubleZero(Object from, Converter converter, ConverterOptions options) {
        return CommonValues.DOUBLE_ZERO;
    }

    static String doubleToString(Object from, Converter converter, ConverterOptions option) {
        return new DecimalFormat("#.####################").format(from);
    }

    static BigDecimal integerTypeToBigDecimal(Object from, Converter converter, ConverterOptions options) {
        return BigDecimal.valueOf(toLong(from));
    }
    static BigInteger integerTypeToBigInteger(Object from, Converter converter, ConverterOptions options) {
        return BigInteger.valueOf(toLong(from));
    }

    static AtomicLong toAtomicLong(Object from, Converter converter, ConverterOptions options) {
        return new AtomicLong(toLong(from));
    }

    static AtomicInteger toAtomicInteger(Object from, Converter converter, ConverterOptions options) {
        return new AtomicInteger(toInt(from));
    }

    static BigDecimal bigIntegerToBigDecimal(Object from, Converter converter, ConverterOptions options) {
        return new BigDecimal((BigInteger)from);
    }

    static BigInteger bigDecimalToBigInteger(Object from, Converter converter, ConverterOptions options) {
        return ((BigDecimal)from).toBigInteger();
    }

    static String bigDecimalToString(Object from, Converter converter, ConverterOptions converterOptions) {
        return ((BigDecimal) from).stripTrailingZeros().toPlainString();
    }

    static BigDecimal toBigDecimal(Object from, Converter converter, ConverterOptions options) {
        return new BigDecimal(StringUtilities.trimToEmpty(from.toString()));
    }

    static AtomicBoolean toAtomicBoolean(Object from, Converter converter, ConverterOptions options) {
        return new AtomicBoolean(toLong(from) != 0);
    }

    static BigDecimal floatingPointToBigDecimal(Object from, Converter converter, ConverterOptions options) {
        return BigDecimal.valueOf(toDouble(from));
    }

    static BigInteger floatingPointToBigInteger(Object from, Converter converter, ConverterOptions options) {
        double d = toDouble(from);
        String s = String.format("%.0f", (d > 0.0) ? Math.floor(d) : Math.ceil(d));
        return new BigInteger(s);
    }

    static boolean isIntTypeNotZero(Object from, Converter converter, ConverterOptions options) {
        return toLong(from) != 0;
    }

    static boolean isFloatTypeNotZero(Object from, Converter converter, ConverterOptions options) {
        return toDouble(from) != 0;
    }

    static boolean isBigIntegerNotZero(Object from, Converter converter, ConverterOptions options) {
        return ((BigInteger)from).compareTo(BigInteger.ZERO) != 0;
    }

    static boolean isBigDecimalNotZero(Object from, Converter converter, ConverterOptions options) {
        return ((BigDecimal)from).compareTo(BigDecimal.ZERO) != 0;
    }

    static BigInteger toBigInteger(Object from, Converter converter, ConverterOptions options) {
        return new BigInteger(StringUtilities.trimToEmpty(from.toString()));
    }

    static UUID bigIntegerToUUID(Object from, Converter converter, ConverterOptions options) {
        BigInteger bigInteger = (BigInteger) from;
        BigInteger mask = BigInteger.valueOf(Long.MAX_VALUE);
        long mostSignificantBits = bigInteger.shiftRight(64).and(mask).longValue();
        long leastSignificantBits = bigInteger.and(mask).longValue();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    static UUID bigDecimalToUUID(Object from, Converter converter, ConverterOptions options) {
        BigInteger bigInt = ((BigDecimal) from).toBigInteger();
        long mostSigBits = bigInt.shiftRight(64).longValue();
        long leastSigBits = bigInt.and(new BigInteger("FFFFFFFFFFFFFFFF", 16)).longValue();
        return new UUID(mostSigBits, leastSigBits);
    }

    /**
     * @param from Number instance to convert to char.
     * @return char that best represents the Number.  The result will always be a value between
     * 0 and Character.MAX_VALUE.
     * @throws IllegalArgumentException if the value exceeds the range of a char.
     */
    static char toCharacter(Object from) {
        long value = toLong(from);
        if (value >= 0 && value <= Character.MAX_VALUE) {
            return (char) value;
        }
        throw new IllegalArgumentException("Value: " + value + " out of range to be converted to character.");
    }

    /**
     * @param from      - object that is a number to be converted to char
     * @param converter - instance of converter mappings to use.
     * @param options   - optional conversion options, not used here.
     * @return char that best represents the Number.  The result will always be a value between
     * 0 and Character.MAX_VALUE.
     * @throws IllegalArgumentException if the value exceeds the range of a char.
     */
    static char toCharacter(Object from, Converter converter, ConverterOptions options) {
        return toCharacter(from);
    }

    static Date toDate(Object from, Converter converter, ConverterOptions options) {
        return new Date(toLong(from));
    }

    static Instant toInstant(Object from) { return Instant.ofEpochMilli(toLong(from)); }

    static Instant toInstant(Object from, Converter converter, ConverterOptions options) {
        return toInstant(from);
    }

    static java.sql.Date toSqlDate(Object from, Converter converter, ConverterOptions options) {
        return new java.sql.Date(toLong(from));
    }

    static Timestamp toTimestamp(Object from, Converter converter, ConverterOptions options) {
        return new Timestamp(toLong(from));
    }

    static Calendar toCalendar(Object from, Converter converter, ConverterOptions options) {
        return CalendarConversions.create(toLong(from), options);
    }

    static LocalDate toLocalDate(Object from, Converter converter, ConverterOptions options) {
        return toZonedDateTime(from, options).toLocalDate();
    }

    static LocalDateTime toLocalDateTime(Object from, Converter converter, ConverterOptions options) {
        return toZonedDateTime(from, options).toLocalDateTime();
    }

    static LocalTime toLocalTime(Object from, Converter converter, ConverterOptions options) {
        return toZonedDateTime(from, options).toLocalTime();
    }

    static ZonedDateTime toZonedDateTime(Object from, ConverterOptions options) {
        return toInstant(from).atZone(options.getZoneId());
    }

    static ZonedDateTime toZonedDateTime(Object from, Converter converter, ConverterOptions options) {
        return toZonedDateTime(from, options);
    }
}

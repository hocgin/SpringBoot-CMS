package org.springframework.data.mongodb.datatables.mapping;

import java.text.ParseException;
import java.util.Date;

import org.springframework.data.mongodb.datatables.repository.DateParser;
import org.springframework.util.StringUtils;

import lombok.Getter;

@Getter
public abstract class ColumnType {
    private static final String CODE_STRING = "string";
    private static final String CODE_INTEGER = "integer";
    private static final String CODE_DOUBLE = "double";
    private static final String CODE_DATE = "date";
    private static final String CODE_BOOLEAN = "boolean";

    public static final ColumnType STRING = new StringColumnType();
    public static final ColumnType INTEGER = new IntegerColumnType();
    public static final ColumnType DOUBLE = new DoubleColumnType();
    public static final ColumnType DATE = new DateColumnType();
    public static final ColumnType BOOLEAN = new BooleanColumnType();

    private String code;
    private boolean comparable;

    protected ColumnType(String code, boolean isComparable) {
        this.code = code;
        this.comparable = isComparable;
    }

    public static ColumnType parse(String text) {
        // default value is STRING
        ColumnType result = STRING;
        if (StringUtils.hasLength(text)) {
            switch (text.toLowerCase()) {
            case CODE_INTEGER:
                result = INTEGER;
                break;
            case CODE_DOUBLE:
                result = DOUBLE;
                break;
            case CODE_DATE:
                result = DATE;
                break;
            case CODE_BOOLEAN:
                result = BOOLEAN;
                break;
            default:
                result = STRING;
                break;
            }
        }
        return result;
    }

    public abstract Object tryConvert(String text);

    static final class StringColumnType extends ColumnType {
        StringColumnType() {
            super(CODE_STRING, true);
        }

        @Override
        public Object tryConvert(String text) {
            return text;
        }
    }

    static final class DateColumnType extends ColumnType {
        DateColumnType() {
            super(CODE_DATE, true);
        }

        @Override
        public Object tryConvert(String text) {
            Object result = text;
            try {
                Date parsedDate = DateParser.parse(text);
                result = parsedDate;
            } catch (ParseException pe) {
                result = text;
            }
            return result;
        }
    }

    static final class IntegerColumnType extends ColumnType {
        IntegerColumnType() {
            super(CODE_INTEGER, true);
        }

        @Override
        public Object tryConvert(String text) {
            Object result = text;
            try {
                Integer parsedInteger = Integer.parseInt(text);
                result = parsedInteger;
            } catch (NumberFormatException nfe) {
                result = text;
            }
            return result;
        }
    }

    static final class DoubleColumnType extends ColumnType {
        DoubleColumnType() {
            super(CODE_DOUBLE, true);
        }

        @Override
        public Object tryConvert(String text) {
            Object result = text;
            try {
                Double parsedDouble = Double.parseDouble(text);
                result = parsedDouble;
            } catch (NumberFormatException nfe) {
                result = text;
            }
            return result;
        }
    }

    static final class BooleanColumnType extends ColumnType {
        BooleanColumnType() {
            super(CODE_BOOLEAN, false);
        }

        @Override
        public Object tryConvert(String text) {
            Object result = text;
            Boolean parsedBoolean = Boolean.parseBoolean(text);
            result = parsedBoolean;
            return result;
        }
    }

}

/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.time.DateTimeException;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Map;

public final class OracleTemporalField {
    public static final TemporalField SIGNED_YEAR_OF_ERA = Field.SIGNED_YEAR_OF_ERA;

    private OracleTemporalField() {
        throw new AssertionError((Object)"Not instantiable");
    }

    private static enum Field implements TemporalField
    {
        SIGNED_YEAR_OF_ERA{

            @Override
            public TemporalUnit getBaseUnit() {
                return ChronoUnit.YEARS;
            }

            @Override
            public TemporalUnit getRangeUnit() {
                return ChronoUnit.FOREVER;
            }

            @Override
            public ValueRange range() {
                return ValueRange.of(-9223372036854775807L, Long.MAX_VALUE);
            }

            @Override
            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.YEAR) && Field.isIso(temporalAccessor);
            }

            @Override
            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (!this.isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: SignedYearOfEra");
                }
                return this.range();
            }

            @Override
            public long getFrom(TemporalAccessor temporalAccessor) {
                if (!this.isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: SignedYearOfEra");
                }
                long l2 = temporalAccessor.getLong(ChronoField.YEAR);
                return l2 < 1L ? l2 - 1L : l2;
            }

            @Override
            public <R extends Temporal> R adjustInto(R r2, long l2) {
                long l3 = this.getFrom(r2);
                if (l2 == 0L) {
                    throw new DateTimeException("0 is not a valid SignedYearOfEra");
                }
                return (R)r2.with(ChronoField.YEAR, l2 < 1L ? l2 + 1L : l2);
            }

            @Override
            public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
                Long l2 = map.get(this);
                if (l2 != null) {
                    if (l2 == 0L) {
                        new DateTimeException("0 is not a valid SignedYearOfEra");
                    }
                    if (!Field.isIso(temporalAccessor)) {
                        throw new DateTimeException("Resolve requires IsoChronology");
                    }
                    map.remove(this);
                    map.put(ChronoField.YEAR, l2 < 1L ? l2 + 1L : l2);
                }
                return null;
            }

            @Override
            public String toString() {
                return "SignedYearOfEra";
            }
        };


        @Override
        public boolean isDateBased() {
            return true;
        }

        @Override
        public boolean isTimeBased() {
            return false;
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            return this.range();
        }

        private static boolean isIso(TemporalAccessor temporalAccessor) {
            return Chronology.from(temporalAccessor).equals(IsoChronology.INSTANCE);
        }
    }
}


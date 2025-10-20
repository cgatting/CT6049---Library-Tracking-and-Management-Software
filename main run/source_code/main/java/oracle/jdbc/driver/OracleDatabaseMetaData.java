/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleResultSet;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleDatabaseMetaData
extends oracle.jdbc.OracleDatabaseMetaData
implements Monitor {
    static final int RSFS = 4284;
    static final boolean DEBUG = false;
    private static final long FOUR_GIG_MINUS_ONE = 0xFFFFFFFFL;
    private long maxLogicalLobSize = -1L;

    public OracleDatabaseMetaData(oracle.jdbc.internal.OracleConnection oracleConnection) {
        super(oracleConnection);
    }

    public OracleDatabaseMetaData(OracleConnection oracleConnection) {
        this((oracle.jdbc.internal.OracleConnection)oracleConnection);
    }

    @Override
    public ResultSet getColumns(String string, String string2, String string3, String string4) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string5;
            String string6;
            String string7;
            String string8;
            boolean bl = this.connection.getIncludeSynonyms();
            if (bl && string2 != null && !this.hasSqlWildcard(string2) && string3 != null && !this.hasSqlWildcard(string3)) {
                string8 = this.getColumnsNoWildcardsPlsql();
                string7 = this.stripSqlEscapes(string2);
                string6 = this.stripSqlEscapes(string3);
                string5 = string4 == null ? "%" : string4;
            } else {
                string8 = this.getColumnsWithWildcardsPlsql(bl);
                string7 = string2 == null ? "%" : string2;
                string6 = string3 == null ? "%" : string3;
                string5 = string4 == null ? "%" : string4;
            }
            Properties properties = new Properties();
            properties.setProperty("use_long_fetch", "true");
            CallableStatement callableStatement = this.connection.prepareCall(string8, properties);
            callableStatement.setString(1, string7);
            callableStatement.setString(2, string6);
            callableStatement.setString(3, string5);
            callableStatement.registerOutParameter(4, -10);
            callableStatement.closeOnCompletion();
            callableStatement.setPoolable(false);
            callableStatement.execute();
            ResultSet resultSet = ((OracleCallableStatement)callableStatement).getCursor(4);
            if (resultSet.getFetchSize() < 4284) {
                resultSet.setFetchSize(4284);
            }
            ResultSet resultSet2 = resultSet;
            return resultSet2;
        }
    }

    String getColumnsNoWildcardsPlsql() throws SQLException {
        String string = "declare\n  in_owner varchar2(256) := null;\n  in_name varchar2(256) := null;\n  my_user_name varchar2(256) := null;\n  cnt number := 0;\n  out_owner varchar2(256) := null;\n  out_name  varchar2(256):= null;\n  xxx SYS_REFCURSOR;\nbegin\n  in_owner := ?;\n  in_name := ?;\n  select user into my_user_name from dual;\n  if (my_user_name = in_owner) then\n    select count(*) into cnt from user_tables\n      where table_name = in_name;\n    if (cnt = 1) then\n      out_owner := in_owner;\n      out_name := in_name;\n    else\n      select count(*) into cnt from user_views\n        where view_name = in_name;\n      if (cnt = 1) then\n        out_owner := in_owner;\n        out_name := in_name;\n      else\n        begin\n          select table_owner, table_name into out_owner, out_name\n            from all_synonyms\n            where CONNECT_BY_ISLEAF = 1\n            and db_link is NULL\n            start with owner = in_owner and synonym_name = in_name\n            connect by prior table_name = synonym_name\n                    and prior table_owner = owner;\n        exception\n          when NO_DATA_FOUND then\n            out_owner := null;\n            out_name := null;\n        end;\n      end if;\n    end if;\n  else\n    select count(*) into cnt from all_tables\n      where owner = in_owner and table_name = in_name;\n    if (cnt = 1) then\n      out_owner := in_owner;\n      out_name := in_name;\n    else\n      select count(*) into cnt from all_views\n         where owner = in_owner and view_name = in_name;\n      if (cnt = 1) then\n        out_owner := in_owner;\n        out_name := in_name;\n      else\n        begin\n          select table_owner, table_name into out_owner, out_name\n            from all_synonyms\n            where CONNECT_BY_ISLEAF = 1\n            and db_link is NULL\n            start with owner = in_owner and synonym_name = in_name\n            connect by prior table_name = synonym_name\n                    and prior table_owner = owner;\n        exception\n          when NO_DATA_FOUND then\n            out_owner := null;\n            out_name := null;\n        end;\n      end if;\n    end if;\n  end if;\n";
        short s2 = this.connection.getVersionNumber();
        String string2 = "open xxx for SELECT NULL AS table_cat,\n";
        String string3 = "       in_owner AS table_schem,\n       in_name AS table_name,\n";
        String string4 = "         DECODE (t.data_type, 'CHAR', t.char_length,                   'VARCHAR', t.char_length,                   'VARCHAR2', t.char_length,                   'NVARCHAR2', t.char_length,                   'NCHAR', t.char_length,                   'NUMBER', 0,           t.data_length)";
        String string5 = "       t.column_name AS column_name,\n" + this.datatypeQuery(OracleDatabaseMetaData.DataTypeSource.COLS, "t") + "       t.data_type AS type_name,\n       DECODE (t.data_precision,                null, DECODE(t.data_type,                        'NUMBER', DECODE(t.data_scale,                                    null, " + (((PhysicalConnection)this.connection).j2ee13Compliant ? "38" : "0") + "                                   , 38), " + string4 + "                           ),         t.data_precision)\n              AS column_size,\n       0 AS buffer_length,\n       DECODE (t.data_type,                'NUMBER', DECODE(t.data_precision,                                 null, DECODE(t.data_scale,                                              null, " + (((PhysicalConnection)this.connection).j2ee13Compliant ? "0" : "-127") + "                                             , t.data_scale),                                  t.data_scale),                t.data_scale) AS decimal_digits,\n       10 AS num_prec_radix,\n       DECODE (t.nullable, 'N', 0, 1) AS nullable,\n";
        String string6 = "       c.comments AS remarks,\n";
        String string7 = "       NULL AS remarks,\n";
        String string8 = "       t.data_default AS column_def,\n       0 AS sql_data_type,\n       0 AS sql_datetime_sub,\n       t.data_length AS char_octet_length,\n       t.column_id AS ordinal_position,\n       DECODE (t.nullable, 'N', 'NO', 'YES') AS is_nullable,\n";
        String string9 = "       null as SCOPE_CATALOG,\n       null as SCOPE_SCHEMA,\n       null as SCOPE_TABLE,\n       null as SOURCE_DATA_TYPE,\n       'NO' as IS_AUTOINCREMENT,\n" + (s2 >= 12000 ? "       t.virtual_column as IS_GENERATEDCOLUMN\n" : "       null as IS_GENERATEDCOLUMN\n");
        String string10 = s2 >= 12000 ? "FROM all_tab_cols t" : "FROM all_tab_columns t";
        String string11 = ", all_col_comments c";
        String string12 = "WHERE t.owner = out_owner \n  AND t.table_name = out_name\n  AND t.column_name LIKE ? ESCAPE '/'\n" + (s2 >= 12000 ? "  AND t.user_generated = 'YES'\n" : "");
        String string13 = "  AND t.owner = c.owner (+)\n  AND t.table_name = c.table_name (+)\n  AND t.column_name = c.column_name (+)\n";
        String string14 = "ORDER BY table_schem, table_name, ordinal_position\n";
        String string15 = string2;
        string15 = string15 + string3;
        string15 = string15 + string5;
        string15 = this.connection.getRemarksReporting() ? string15 + string6 : string15 + string7;
        string15 = string15 + string8 + string9 + string10;
        if (this.connection.getRemarksReporting()) {
            string15 = string15 + string11;
        }
        string15 = string15 + "\n" + string12;
        if (this.connection.getRemarksReporting()) {
            string15 = string15 + string13;
        }
        string15 = string15 + "\n" + string14;
        String string16 = "; \n ? := xxx;\n end;";
        String string17 = string + string15 + string16;
        return string17;
    }

    String getColumnsWithWildcardsPlsql(boolean bl) throws SQLException {
        short s2 = this.connection.getVersionNumber();
        String string = "declare\n  in_owner varchar2(256) := null;\n  in_name varchar2(256) := null;\n  in_column varchar2(256) := null;\n  xyzzy SYS_REFCURSOR;\nbegin\n  in_owner := ?;\n  in_name := ?;\n  in_column := ?;\n";
        String string2 = "UNION ALL\n ";
        String string3 = "SELECT ";
        String string4 = "NULL AS table_cat,\n";
        String string5 = "";
        if (s2 >= 10200 & s2 < 11100 & bl) {
            string5 = "/*+ CHOOSE */";
        }
        String string6 = "       t.owner AS table_schem,\n       t.table_name AS table_name,\n";
        String string7 = "       REGEXP_SUBSTR(LTRIM(s.owner, '/'), '[^/]+') AS table_schem,\n       REGEXP_SUBSTR(LTRIM(s.synonym_name, '/'),\n                           '[^/]+') AS table_name,\n";
        String string8 = "       DECODE (t.data_type,                'CHAR', t.char_length,                'VARCHAR', t.char_length,                'VARCHAR2', t.char_length,                'NVARCHAR2', t.char_length,                'NCHAR', t.char_length,                'NUMBER', 0,                t.data_length)";
        String string9 = "       t.column_name AS column_name,\n" + this.datatypeQuery(OracleDatabaseMetaData.DataTypeSource.COLS, "t") + "       t.data_type AS type_name,\n       DECODE (t.data_precision,                null, DECODE(t.data_type,                        'NUMBER', DECODE(t.data_scale,                                    null, " + (((PhysicalConnection)this.connection).j2ee13Compliant ? "38" : "0") + "                                   , 38), " + string8 + "                           ),         t.data_precision)\n              AS column_size,\n       0 AS buffer_length,\n       DECODE (t.data_type,                'NUMBER', DECODE(t.data_precision,                                 null, DECODE(t.data_scale,                                              null, " + (((PhysicalConnection)this.connection).j2ee13Compliant ? "0" : "-127") + "                                             , t.data_scale),                                  t.data_scale),                t.data_scale) AS decimal_digits,\n       10 AS num_prec_radix,\n       DECODE (t.nullable, 'N', 0, 1) AS nullable,\n";
        String string10 = "       c.comments AS remarks,\n";
        String string11 = "       NULL AS remarks,\n";
        String string12 = "       t.data_default AS column_def,\n       0 AS sql_data_type,\n       0 AS sql_datetime_sub,\n       t.data_length AS char_octet_length,\n       t.column_id AS ordinal_position,\n       DECODE (t.nullable, 'N', 'NO', 'YES') AS is_nullable,\n";
        String string13 = "       null as SCOPE_CATALOG,\n       null as SCOPE_SCHEMA,\n       null as SCOPE_TABLE,\n       null as SOURCE_DATA_TYPE,\n       'NO' as IS_AUTOINCREMENT,\n" + (s2 >= 12000 ? "       t.virtual_column as IS_GENERATEDCOLUMN\n" : "       null as IS_GENERATEDCOLUMN\n");
        String string14 = s2 >= 12000 ? "FROM all_tab_cols t" : "FROM all_tab_columns t";
        String string15 = ", (SELECT SYS_CONNECT_BY_PATH(owner, '/') owner,\n          SYS_CONNECT_BY_PATH(synonym_name, '/')\n                              synonym_name,\n          table_owner, table_name\n  FROM all_synonyms\n   WHERE CONNECT_BY_ISLEAF = 1\n    AND db_link is NULL\n  START WITH owner LIKE in_owner ESCAPE '/'\n    AND synonym_name LIKE in_name ESCAPE '/'\n  CONNECT BY PRIOR table_name = synonym_name\n    AND PRIOR table_owner = owner) s";
        String string16 = ", all_col_comments c";
        String string17 = "WHERE t.owner LIKE in_owner ESCAPE '/'\n  AND t.table_name LIKE in_name ESCAPE '/'\n  AND t.column_name LIKE in_column ESCAPE '/'\n" + (s2 >= 12000 ? "  AND t.user_generated = 'YES'\n" : "");
        String string18 = "WHERE t.owner = s.table_owner\n  AND t.table_name = s.table_name\n  AND t.column_name LIKE in_column ESCAPE '/'\n" + (s2 >= 12000 ? "  AND t.user_generated = 'YES'\n" : "");
        String string19 = "  AND t.owner = c.owner (+)\n  AND t.table_name = c.table_name (+)\n  AND t.column_name = c.column_name (+)\n";
        String string20 = "ORDER BY table_schem, table_name, ordinal_position\n";
        String string21 = "open xyzzy for\n";
        String string22 = string21 + string3 + string5 + string4 + string6;
        string22 = string22 + string9;
        string22 = this.connection.getRemarksReporting() ? string22 + string10 : string22 + string11;
        string22 = string22 + string12 + string13 + string14;
        if (this.connection.getRemarksReporting()) {
            string22 = string22 + string16;
        }
        string22 = string22 + "\n" + string17;
        if (this.connection.getRemarksReporting()) {
            string22 = string22 + string19;
        }
        if (this.connection.getIncludeSynonyms()) {
            string22 = string22 + string2 + string3 + string5 + string4;
            string22 = string22 + string7;
            string22 = string22 + string9;
            string22 = this.connection.getRemarksReporting() ? string22 + string10 : string22 + string11;
            string22 = string22 + string12 + string13 + string14;
            string22 = string22 + string15;
            if (this.connection.getRemarksReporting()) {
                string22 = string22 + string16;
            }
            string22 = string22 + "\n" + string18;
            if (this.connection.getRemarksReporting()) {
                string22 = string22 + string19;
            }
        }
        string22 = string22 + string20;
        String string23 = "; \n ? := xyzzy;\n end;";
        String string24 = string + string22 + string23;
        return string24;
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        Statement statement = this.connection.createStatement();
        short s2 = this.connection.getVersionNumber();
        int n2 = this.connection.getVarTypeMaxLenCompat();
        int n3 = 2000;
        int n4 = 2000;
        int n5 = 4000;
        int n6 = 4000;
        int n7 = 2000;
        if (n2 == 2) {
            n3 = 2000;
            n4 = 2000;
            n5 = Short.MAX_VALUE;
            n6 = 32766;
            n7 = Short.MAX_VALUE;
        }
        String string = "union select\n 'CHAR' as type_name, 1 as data_type, " + n3 + " as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'CHAR' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string2 = "union select\n 'NCHAR' as type_name, -15 as data_type, " + n4 + " as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'NCHAR' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string3 = "union select\n 'VARCHAR2' as type_name, 12 as data_type, " + n5 + " as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'VARCHAR2' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string4 = "union select\n 'NVARCHAR2' as type_name, -9 as data_type, " + n6 + " as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'nVARCHAR2' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string5 = "union select\n 'DATE' as type_name, " + (((PhysicalConnection)this.connection).mapDateToTimestamp ? "93" : "91") + "as data_type, 7 as precision,\n 'DATE ''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'DATE' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string6 = "union select\n 'RAW' as type_name, -3 as data_type, " + n7 + " as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'RAW' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string7 = "-1";
        String string8 = "union select\n 'BLOB' as type_name, 2004 as data_type, " + string7 + " as precision,\n null as literal_prefix, null as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 0 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'BLOB' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string9 = "union select\n 'CLOB' as type_name, 2005 as data_type, " + string7 + " as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 0 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'CLOB' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string10 = "union select\n 'NCLOB' as type_name, 2011 as data_type, " + string7 + " as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 0 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'NCLOB' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n";
        String string11 = "select\n 'NUMBER' as type_name, 2 as data_type, 38 as precision,\n NULL as literal_prefix, NULL as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 1 as fixed_prec_scale, 0 as auto_increment,\n 'NUMBER' as local_type_name, -84 as minimum_scale, 127 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + string + string2 + string3 + string4 + string5 + "union select\n 'DATE' as type_name, 92 as data_type, 7 as precision,\n 'DATE ''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'DATE' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select\n 'TIMESTAMP' as type_name, 93 as data_type, 11 as precision,\n 'TIMESTAMP ''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'TIMESTAMP' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select\n 'TIMESTAMP WITH TIME ZONE' as type_name, -101 as data_type, 13 as precision,\n 'TIMESTAMP ''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'TIMESTAMP WITH TIME ZONE' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select\n 'TIMESTAMP WITH LOCAL TIME ZONE' as type_name, -102 as data_type, 11 as precision,\n 'TIMESTAMP ''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'TIMESTAMP WITH LOCAL TIME ZONE' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select\n 'INTERVALYM' as type_name, -103 as data_type, 5 as precision,\n 'INTERVAL ''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'INTERVALYM' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select\n 'INTERVALDS' as type_name, -104 as data_type, 4 as precision,\n 'INTERVAL ''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 3 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'INTERVALDS' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + string6 + "union select\n 'LONG' as type_name, -1 as data_type, 2147483647 as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 0 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'LONG' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select\n 'LONG RAW' as type_name, -4 as data_type, 2147483647 as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 0 as case_sensitive, 0 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'LONG RAW' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select 'NUMBER' as type_name, -7 as data_type, 1 as precision,\nNULL as literal_prefix, NULL as literal_suffix, \n'(1)' as create_params, 1 as nullable, 0 as case_sensitive, 3 as searchable,\n0 as unsigned_attribute, 1 as fixed_prec_scale, 0 as auto_increment,\n'NUMBER' as local_type_name, -84 as minimum_scale, 127 as maximum_scale,\nNULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select 'NUMBER' as type_name, -6 as data_type, 3 as precision,\nNULL as literal_prefix, NULL as literal_suffix, \n'(3)' as create_params, 1 as nullable, 0 as case_sensitive, 3 as searchable,\n0 as unsigned_attribute, 1 as fixed_prec_scale, 0 as auto_increment,\n'NUMBER' as local_type_name, -84 as minimum_scale, 127 as maximum_scale,\nNULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select 'NUMBER' as type_name, 5 as data_type, 5 as precision,\nNULL as literal_prefix, NULL as literal_suffix, \n'(5)' as create_params, 1 as nullable, 0 as case_sensitive, 3 as searchable,\n0 as unsigned_attribute, 1 as fixed_prec_scale, 0 as auto_increment,\n'NUMBER' as local_type_name, -84 as minimum_scale, 127 as maximum_scale,\nNULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select 'NUMBER' as type_name, 4 as data_type, 10 as precision,\nNULL as literal_prefix, NULL as literal_suffix, \n'(10)' as create_params, 1 as nullable, 0 as case_sensitive, 3 as searchable,\n0 as unsigned_attribute, 1 as fixed_prec_scale, 0 as auto_increment,\n'NUMBER' as local_type_name, -84 as minimum_scale, 127 as maximum_scale,\nNULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select 'NUMBER' as type_name, -5 as data_type, 38 as precision,\nNULL as literal_prefix, NULL as literal_suffix, \nNULL as create_params, 1 as nullable, 0 as case_sensitive, 3 as searchable,\n0 as unsigned_attribute, 1 as fixed_prec_scale, 0 as auto_increment,\n'NUMBER' as local_type_name, -84 as minimum_scale, 127 as maximum_scale,\nNULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select 'FLOAT' as type_name, 6 as data_type, 63 as precision,\nNULL as literal_prefix, NULL as literal_suffix, \nNULL as create_params, 1 as nullable, 0 as case_sensitive, 3 as searchable,\n0 as unsigned_attribute, 1 as fixed_prec_scale, 0 as auto_increment,\n'FLOAT' as local_type_name, -84 as minimum_scale, 127 as maximum_scale,\nNULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select 'REAL' as type_name, 7 as data_type, 63 as precision,\nNULL as literal_prefix, NULL as literal_suffix, \nNULL as create_params, 1 as nullable, 0 as case_sensitive, 3 as searchable,\n0 as unsigned_attribute, 1 as fixed_prec_scale, 0 as auto_increment,\n'REAL' as local_type_name, -84 as minimum_scale, 127 as maximum_scale,\nNULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + string8 + string9 + string10 + "union select\n 'REF' as type_name, 2006 as data_type, 0 as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 0 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'REF' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select\n 'ARRAY' as type_name, 2003 as data_type, 0 as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 0 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'ARRAY' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "union select\n 'STRUCT' as type_name, 2002 as data_type, 0 as precision,\n '''' as literal_prefix, '''' as literal_suffix, NULL as create_params,\n 1 as nullable, 1 as case_sensitive, 0 as searchable,\n 0 as unsigned_attribute, 0 as fixed_prec_scale, 0 as auto_increment,\n 'STRUCT' as local_type_name, 0 as minimum_scale, 0 as maximum_scale,\n NULL as sql_data_type, NULL as sql_datetime_sub, 10 as num_prec_radix\nfrom dual\n" + "order by data_type\n";
        statement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)statement.executeQuery(string11);
        return oracleResultSet;
    }

    @Override
    public String getAuditBanner() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            String string = ((PhysicalConnection)this.connection).getAuditBanner();
            return string;
        }
    }

    @Override
    public String getAccessBanner() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            String string = ((PhysicalConnection)this.connection).getAccessBanner();
            return string;
        }
    }

    @Override
    public boolean isServerBigSCN() throws SQLException {
        return ((PhysicalConnection)this.connection).isServerBigSCN();
    }

    @Override
    public boolean isCompatible122OrGreater() throws SQLException {
        return ((PhysicalConnection)this.connection).isCompatible122OrGreater();
    }

    @Override
    public long getMaxLogicalLobSize() throws SQLException {
        if (this.maxLogicalLobSize == -1L) {
            try (Statement statement = this.connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("select value from v$parameter where name = 'db_block_size'");){
                this.maxLogicalLobSize = resultSet.next() ? 0xFFFFFFFFL * resultSet.getLong(1) : 0L;
            }
            catch (SQLException sQLException) {
                this.maxLogicalLobSize = 0L;
                if (sQLException.getErrorCode() == 942) {
                    throw (SQLException)DatabaseError.createSqlException(295).fillInStackTrace();
                }
                throw sQLException;
            }
        }
        return this.maxLogicalLobSize;
    }

    @Override
    public boolean supportsRefCursors() throws SQLException {
        return true;
    }
}

